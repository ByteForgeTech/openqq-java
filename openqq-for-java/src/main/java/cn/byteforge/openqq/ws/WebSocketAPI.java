package cn.byteforge.openqq.ws;

import com.google.gson.JsonObject;
import cn.byteforge.openqq.model.Certificate;
import cn.byteforge.openqq.util.Maps;
import cn.byteforge.openqq.ws.entity.OpCode;
import cn.byteforge.openqq.ws.entity.Session;
import cn.byteforge.openqq.ws.handler.EventParseHandler;
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
