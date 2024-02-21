package com.illtamer.openqq.ws;

import com.illtamer.openqq.ws.handler.ChainHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.function.Consumer;

/**
 * 状态维护、登录鉴权、心跳维护、断线恢复重连
 * */
public class QQConnection {

    /**
     * 用于记录和管理所有客户端的channel
     * */
    public static final ChannelGroup CLIENT_GROUPS = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * @param url websocket链接
     * @param chainHandler 返回结果链式处理
     * @param callback 连接成功时回调执行，回传 ChannelId
     * */
    public static void connect(String url, ChainHandler chainHandler, @Nullable Consumer<ChannelId> callback) throws InterruptedException {
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

            URI uri = new URI(url);
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
            if (callback != null) callback.accept(channel.id());
            channel.closeFuture().sync();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } finally {
            group.shutdownGracefully();
        }
    }

}
