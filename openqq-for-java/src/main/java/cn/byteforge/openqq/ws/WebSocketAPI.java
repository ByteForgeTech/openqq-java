package cn.byteforge.openqq.ws;

import cn.byteforge.openqq.Global;
import cn.byteforge.openqq.ws.entity.Shard;
import cn.byteforge.openqq.ws.event.EventType;
import cn.byteforge.openqq.ws.handler.APICallbackHandler;
import cn.hutool.core.lang.Assert;
import com.google.gson.JsonObject;
import cn.byteforge.openqq.util.Maps;
import cn.byteforge.openqq.ws.entity.OpCode;
import cn.byteforge.openqq.ws.entity.Session;
import cn.byteforge.openqq.ws.handler.EventParseHandler;
import io.netty.channel.ChannelFuture;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * WebSocket API 调用
 * */
@Slf4j
public class WebSocketAPI {

    /**
     * 获取登录态 Session
     * @param intents 监听的事件
     * @param shard 分片
     * @param properties 暂时无用
     * @param context 机器人上下文实例
     * */
    @SneakyThrows
    public static Session getSession(int intents, Shard shard, @Nullable Map<String, Object> properties, BotContext context) {
        CompletableFuture<JsonObject> future = send(Maps.of(
                "op", OpCode.IDENTIFY.getCode(),
                "d", Maps.of(
                        "token", String.format(Global.Authorization, context.getCertificate().getAccessToken().getContent()),
                        "intents", intents,
                        "shard", shard.toArray(),
                        "properties", properties
                )
        ), EventType.READY, context);
        return EventParseHandler.GSON.fromJson(future.join(), Session.class);
    }

    /**
     * 恢复登录态 Session
     * @param context 机器人上下文实例
     * */
    public static void resumeSession(String sessionId, BotContext context) {

    }

    /**
     * 发送请求并接收消息回调
     * @return d
     * */
    public static CompletableFuture<JsonObject> send(Object payload, @Nullable String callbackName, BotContext context) {
        String json = EventParseHandler.GSON.toJson(payload);
        log.info("Send json object: {}", json);
        ChannelFuture channelFuture = QQConnection.CLIENT_GROUPS.find(context.getChannelId())
                .writeAndFlush(new TextWebSocketFrame(json));
        return CompletableFuture.supplyAsync(() -> {
            try {
                channelFuture.sync();
                APICallbackHandler handler = context.getChainHandler().find(APICallbackHandler.class);
                Assert.notNull(handler, "APICallbackHandler must be appended before invoke ws api");
                return Objects.requireNonNull(handler).getCallback(callbackName);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
