package cn.byteforge.openqq.ws;

import cn.byteforge.openqq.exception.InvalidShardException;
import cn.byteforge.openqq.http.OpenAPI;
import cn.byteforge.openqq.http.entity.AccessToken;
import cn.byteforge.openqq.http.entity.RecommendShard;
import cn.byteforge.openqq.model.Certificate;
import cn.byteforge.openqq.ws.entity.Session;
import cn.byteforge.openqq.ws.entity.Shard;
import cn.byteforge.openqq.ws.handler.ChainHandler;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Pair;
import io.netty.channel.ChannelId;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Bot 上下文
 * */
@Slf4j
@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class BotContext {

    private final ExecutorService executor;

    /**
     * 访问凭证
     * */
    private final Certificate certificate;

    /**
     * UUID: Pair<通道 id: 当前 wss 连接链式处理>
     * */
    @Setter(AccessLevel.NONE)
    private Map<UUID, Pair<ChannelId, ChainHandler>> connMap;

    /**
     * Chain Supplier Map
     * */
    @Setter(AccessLevel.NONE)
    private Map<UUID, Supplier<ChainHandler>> chainSupplierMap;

    /**
     * 分片注册数组
     * */
    @Setter(AccessLevel.NONE)
    private Pair<UUID, Shard>[] shardsConfigured;

    /**
     * Session Map
     * */
    @Setter(AccessLevel.NONE)
    private Map<UUID, Session> sessionMap;

    /**
     * SessionFunc Map
     * */
    @Setter(AccessLevel.NONE)
    private Map<UUID, Function<UUID, Session>> sessionFuncMap;

    /**
     * 接收到的消息序号 Map
     * */
    @Setter(AccessLevel.NONE)
    private Map<UUID, Long> receivedSeqMap;

    /**
     * 处理到的消息序号 Map
     * @apiNote reconnect 时，从此 seq 起补发中间遗漏的事件
     * */
    @Setter(AccessLevel.NONE)
    private Map<UUID, Long> handledSeqMap;

    {
        this.connMap = new ConcurrentHashMap<>();
        this.chainSupplierMap = new ConcurrentHashMap<>();
        this.sessionMap = new ConcurrentHashMap<>();
        this.sessionFuncMap = new ConcurrentHashMap<>();
        this.receivedSeqMap = new ConcurrentHashMap<>();
        this.handledSeqMap = new ConcurrentHashMap<>();
    }

    /**
     * 配置并检查当前上下文分片
     * */
    @SuppressWarnings("unchecked")
    protected void configureShard(UUID uuid, Shard shard) {
        try {
            if (shardsConfigured == null) {
                shardsConfigured = new Pair[shard.getSize()];
            }
            // TODO 目前暂时只支持单分片自动重连
            // Assert.isNull(shardsConfigured[shard.getIndex()]);
            shardsConfigured[shard.getIndex()] = new Pair<>(uuid, shard);
        } catch (Exception e) {
            throw new InvalidShardException(shard, shardsConfigured);
        }
    }

    /**
     * 将链接绑定机器人上下文
     */
    protected void bindChannel(UUID uuid, ChannelId id, ChainHandler chainHandler) {
        connMap.put(uuid, new Pair<>(id, chainHandler));
    }

    /**
     * 初始化 Session
     * */
    protected void initSession(UUID uuid, @Nullable Function<UUID, Session> sessionFunction) {
        if (sessionFunction != null) {
            sessionFuncMap.put(uuid, sessionFunction);
        } else {
            sessionFunction = sessionFuncMap.get(uuid);
        }
        sessionMap.put(uuid, sessionFunction.apply(uuid));
    }

    /**
     * 创建机器人上下文
     * @param executor 托管 wss 连接的线程池 (core >= 2)
     * */
    public static BotContext create(String appId, String clientSecret, ExecutorService executor) {
        // initialize with empty token
        Certificate certificate = new Certificate(appId, clientSecret, null);
        return new BotContext(executor, certificate);
    }

}
