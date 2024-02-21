package com.illtamer.openqq.ws;

import com.google.gson.JsonObject;
import com.illtamer.openqq.model.Certificate;
import com.illtamer.openqq.util.Maps;
import com.illtamer.openqq.ws.entity.OpCode;
import com.illtamer.openqq.ws.entity.Session;
import com.illtamer.openqq.ws.handler.EventParseHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelId;
import lombok.SneakyThrows;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * WebSocket API 调用
 * */
public class WebSocketAPI {

    @SneakyThrows
    public static Session getSession(int intents, int[] shard, @Nullable Map<String, Object> properties, Certificate cert, ChannelId id) {
        CompletableFuture<JsonObject> future = send(Maps.of(
                "op", OpCode.IDENTIFY.getCode(),
                "d", Maps.of(
                        "token", cert.getAccessToken().getContent(),
                        "intents", intents,
                        "shard", shard,
                        "properties", properties
                )
        ), id);
        return EventParseHandler.GSON.fromJson(future.get(), Session.class);
    }

    public static CompletableFuture<JsonObject> send(Object payload, ChannelId id) {
        ChannelFuture channelFuture = QQConnection.CLIENT_GROUPS.find(id)
                .writeAndFlush(EventParseHandler.GSON.toJson(payload));
        return CompletableFuture.supplyAsync(() -> {
            try {
                channelFuture.sync();
                // TODO get return data from chain handler
                return null;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
