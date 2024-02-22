package cn.byteforge.openqq.ws;

import cn.byteforge.openqq.ws.entity.BotContext;
import cn.byteforge.openqq.ws.entity.Shard;
import cn.byteforge.openqq.ws.handler.APICallbackHandler;
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
    public static Session getSession(int intents, Shard shard, @Nullable Map<String, Object> properties, BotContext context) {
        CompletableFuture<JsonObject> future = send(Maps.of(
                "op", OpCode.IDENTIFY.getCode(),
                "d", Maps.of(
                        "token", context.getCertificate().getAccessToken().getContent(),
                        "intents", intents,
                        "shard", shard.toArray(),
                        "properties", properties
                )
        ), context);
        return EventParseHandler.GSON.fromJson(future.get().getAsJsonObject("d"), Session.class);
    }

    /**
     * @return total payload
     * */
    public static CompletableFuture<JsonObject> send(Object payload, BotContext context) {
        ChannelFuture channelFuture = QQConnection.CLIENT_GROUPS.find(context.getChannelId())
                .writeAndFlush(EventParseHandler.GSON.toJson(payload));
        return CompletableFuture.supplyAsync(() -> {
            try {
                channelFuture.sync();
                // TODO get return data from chain handler
                APICallbackHandler handler = context.getChainHandler().find(APICallbackHandler.class);

                return null;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
