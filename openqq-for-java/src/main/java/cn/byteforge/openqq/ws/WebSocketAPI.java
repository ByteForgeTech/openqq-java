package cn.byteforge.openqq.ws;

import cn.byteforge.openqq.Global;
import cn.byteforge.openqq.util.Maps;
import cn.byteforge.openqq.ws.entity.Intent;
import cn.byteforge.openqq.ws.entity.Session;
import cn.byteforge.openqq.ws.entity.Shard;
import cn.byteforge.openqq.ws.entity.enumerate.OpCode;
import cn.byteforge.openqq.ws.event.EventType;
import cn.byteforge.openqq.ws.handler.APICallbackHandler;
import cn.byteforge.openqq.ws.handler.ChainHandler;
import cn.byteforge.openqq.ws.handler.EventParseHandler;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Pair;
import com.google.gson.JsonObject;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelId;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * WebSocket API 调用
 * */
@Slf4j
public class WebSocketAPI {

    public static final String NO_NEED_CALLBACK = null;

    /**
     * 创建登录态 Session
     * @param intent 监听的事件类型
     * @param uuid session 绑定的连接 id
     * @param properties 暂时无用
     * @param context 机器人上下文实例
     * */
    public static Session newStandaloneSession(Intent intent, UUID uuid, @Nullable Map<String, Object> properties, BotContext context) {
        return newShardSession(intent, uuid, Shard.STANDALONE, properties, context);
    }

    /**
     * 创建当前分片的登录态 Session
     * @param intent 监听的事件类型
     * @param uuid session 绑定的连接 id
     * @param shard 分片
     * @param properties 暂时无用
     * @param context 机器人上下文实例
     * */
    @SneakyThrows
    public static Session newShardSession(Intent intent, UUID uuid, Shard shard, @Nullable Map<String, Object> properties, BotContext context) {
        context.configureShard(uuid, shard);
        CompletableFuture<JsonObject> future = send(Maps.of(
                "op", OpCode.IDENTIFY.getCode(),
                "d", Maps.of(
                        "token", String.format(Global.Authorization, context.getCertificate().getAccessToken().getContent()),
                        "intents", intent.getValue(),
                        "shard", shard.toArray(),
                        "properties", properties
                )
        ), uuid, EventType.READY, context);
        JsonObject json = future.join();
        Assert.notNull(json);
        Session session = EventParseHandler.GSON.fromJson(json, Session.class);
        if (context.getSessionMap().containsKey(uuid)) {
            log.info("Duplicated session detected, is this in resume session or reconnect ?");
        }
        return session;
    }

    /**
     * 恢复登录态 Session
     * @param uuid session 绑定的连接 id
     * @param context 机器人上下文实例
     * */
    public static void resumeSession(UUID uuid, BotContext context) {
        Map<UUID, Session> sessionMap = context.getSessionMap();
        Session oldSession = sessionMap.get(uuid);
        Assert.notNull(oldSession);
        CompletableFuture<JsonObject> future = send(Maps.of(
                "op", OpCode.RESUME.getCode(),
                "d", Maps.of(
                        "token", String.format(Global.Authorization, context.getCertificate().getAccessToken().getContent()),
                        "session_id", oldSession.getSessionId(),
                        "seq", context.getHandledSeqMap().get(uuid)
                )
        ), uuid, EventType.RESUMED, context);
        Assert.notNull(future.join(), "Resume session failed, is it invalid ?");
        log.info("Resume session: {}", oldSession);
    }

    /**
     * 发送请求并接收消息回调
     * @return d
     * */
    public static CompletableFuture<@Nullable JsonObject> send(Object payload, UUID uuid, @Nullable String callbackName, BotContext context) {
        String json = EventParseHandler.GSON.toJson(payload);
        log.info("Send json object: {}", json);
        Pair<ChannelId, ChainHandler> chainPair = context.getConnMap().get(uuid);
        ChannelFuture channelFuture = QQConnection.CLIENT_GROUPS.find(chainPair.getKey())
                .writeAndFlush(new TextWebSocketFrame(json));
        return CompletableFuture.supplyAsync(() -> {
            try {
                channelFuture.sync();
                APICallbackHandler handler = chainPair.getValue().find(APICallbackHandler.class);
                Assert.notNull(handler, "APICallbackHandler must be appended before invoke ws api");
                return Objects.requireNonNull(handler).getCallback(callbackName);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
