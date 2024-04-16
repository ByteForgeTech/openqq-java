package cn.byteforge.openqq.ws;

import cn.byteforge.openqq.exception.InvalidShardException;
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

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * Bot 上下文
 * */
@Slf4j
@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class BotContext {

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
            Assert.isNull(shardsConfigured[shard.getIndex()]);
            shardsConfigured[shard.getIndex()] = new Pair<>(uuid, shard);
        } catch (Exception e) {
            throw new InvalidShardException(shard, shardsConfigured);
        }
    }

    /**
     * 将链接绑定机器人上下文
     * @return 唯一分片连接唯一标识
     */
    protected UUID bindChannel(ChannelId id, ChainHandler chainHandler) {
        UUID uuid = UUID.randomUUID();
        Pair<ChannelId, ChainHandler> put = connMap.put(uuid, new Pair<>(id, chainHandler));
        Assert.isNull(put, "Duplicated uuid generated: %s", uuid);
        return uuid;
    }

    /**
     * 更新机器人链接
     * @apiNote 用于重连
     * */
    protected void updateChannel(UUID uuid, ChannelId id) {
        Pair<ChannelId, ChainHandler> record = connMap.get(uuid);
        Assert.notNull(record, "Channel should be bind but was updated with uuid: %s", uuid);
        connMap.put(uuid, new Pair<>(id, record.getValue()));
    }

    /**
     * 创建机器人上下文
     * */
    public static BotContext create(Certificate certificate) {
        return new BotContext(certificate);
    }

}
