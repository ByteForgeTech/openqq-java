package cn.byteforge.openqq.ws;

import cn.byteforge.openqq.QQHelper;
import cn.byteforge.openqq.http.OpenAPI;
import cn.byteforge.openqq.http.entity.AccessToken;
import cn.byteforge.openqq.http.entity.RecommendShard;
import cn.byteforge.openqq.model.Certificate;
import cn.byteforge.openqq.ws.entity.Session;
import cn.byteforge.openqq.ws.handler.ChainHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URI;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 状态维护、登录鉴权、心跳维护、断线恢复重连
 * */
@Slf4j
public class QQConnection {

    /**
     * 用于记录和管理所有客户端的channel
     * */
    public static final ChannelGroup CLIENT_GROUPS = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * 重建 WebSocket 连接
     * @param context 机器人上下文
     * @param callback 连接成功时回调执行，回传 UUID，用于标识分片链接
     * */
    public static void reconnect(UUID uuid, BotContext context, @Nullable Consumer<UUID> callback) {
        doConnect(context, null, null, uuid, callback);
    }

    /**
     * 建立 WebSocket 连接
     * @param context 机器人上下文
     * @param handlerSupplier 链式处理 Supplier，使用分片连接时请分别生成每个链接对应的 ChainHandler，确保线程安全
     * @param callback 连接成功时回调执行，回传 UUID，用于标识分片链接
     * */
    public static void connect(BotContext context, Supplier<ChainHandler> handlerSupplier, Function<UUID, Session> sessionFunction, @Nullable Consumer<UUID> callback) {
        doConnect(context, handlerSupplier, sessionFunction, null, callback);
    }

    /**
     * @apiNote connect 需要绑定 session 和 chainHandler，reconnect 需要根据 uuid 重建 ws 连接
     * */
    private static void doConnect(
            BotContext context,
            @Nullable Supplier<ChainHandler> handlerSupplier,
            @Nullable Function<UUID, Session> sessionFunction,
            @Nullable final UUID _uuid,
            @Nullable Consumer<UUID> callback
    ) {
        context.getExecutor().submit(() -> {
            // refresh token first
            Certificate cert = context.getCertificate();
            AccessToken token = OpenAPI.getAppAccessToken(cert.getAppId(), cert.getClientSecret());
            cert.updateToken(token);

            UUID uuid = _uuid != null ? _uuid : UUID.randomUUID();
            ChainHandler chainHandler = getChainHandler(handlerSupplier, uuid, context);
            EventChannelHandler eventHandler = new EventChannelHandler(chainHandler);
            EventLoopGroup group = new NioEventLoopGroup();
            try {
                Bootstrap bootstrap = new Bootstrap()
                        .group(group)
                        .option(ChannelOption.TCP_NODELAY, true)
                        .option(ChannelOption.SO_KEEPALIVE, true)
                        .channel(NioSocketChannel.class)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @SneakyThrows
                            @Override
                            protected void initChannel(@NotNull SocketChannel ch) {
                                ch.pipeline()
                                        .addLast(new SslHandler(SslContextBuilder.forClient().build().newEngine(ch.alloc())))
                                        .addLast(new HttpClientCodec())
                                        // 添加一个用于支持大数据流的支持
                                        .addLast(new ChunkedWriteHandler())
                                        // 添加一个聚合器，这个聚合器主要是将HttpMessage聚合成FullHttpRequest/Response
                                        .addLast(new HttpObjectAggregator(1024 * 1024))
                                        .addLast(eventHandler);
                            }
                        });

                RecommendShard shard = OpenAPI.getRecommendShardWssUrls(cert);
                URI uri = new URI(shard.getUrl());
                WebSocketClientHandshaker handshake = WebSocketClientHandshakerFactory
                        .newHandshaker(uri, WebSocketVersion.V13, null, true, null);
                Channel channel = bootstrap
                        .connect(uri.getHost(), 443)
                        .sync()
                        .channel();
                eventHandler.setHandshaker(handshake);
                handshake.handshake(channel);
                // block until handshake success
                eventHandler.getHandshakeFuture().sync();
                CLIENT_GROUPS.add(channel);

                // bind channel to uuid
                context.bindChannel(uuid, channel.id(), chainHandler);

                // set context to ChainHandler
                ChainHandler next = chainHandler;
                while (next != null) {
                    next.setMetaData(uuid, context);
                    next = next.next();
                }

                // init session
                context.initSession(uuid, sessionFunction);

                // auto refresh token thread
                QQHelper.startAutoRefreshToken(uuid, context);

                if (callback != null) callback.accept(uuid);
                channel.closeFuture().sync();
            } catch (Exception e) {
                log.error("Exception occurred in wss connection", e);
            } finally {
                ChainHandler next = chainHandler;
                while (next != null) {
                    try {
                        next.onClose();
                    } catch (Exception e) {
                        log.error("Error occurred when close handler: {}", next.getClass().getName(), e);
                    }
                    next = next.next();
                }
                Channel channel = CLIENT_GROUPS.find(context.getConnMap().get(uuid).getKey());
                CLIENT_GROUPS.remove(channel);
                try {
                    group.shutdownGracefully().sync();
                } catch (InterruptedException ignore) {}
                log.info("Connection closed, please make sure that there is a another connection established ...");
            }
        });
    }

    @NotNull
    private static ChainHandler getChainHandler(@Nullable Supplier<ChainHandler> handlerSupplier, UUID uuid, BotContext context) {
        // 初始化
        if (handlerSupplier != null) {
            context.getChainSupplierMap().put(uuid, handlerSupplier);
            return handlerSupplier.get();
        }
        // 重连
        return context.getChainSupplierMap().get(uuid).get();
    }

}
