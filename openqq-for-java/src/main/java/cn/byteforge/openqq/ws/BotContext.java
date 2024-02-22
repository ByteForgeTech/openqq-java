package cn.byteforge.openqq.ws;

import cn.byteforge.openqq.model.Certificate;
import cn.byteforge.openqq.ws.handler.ChainHandler;
import cn.hutool.core.lang.Assert;
import io.netty.channel.ChannelId;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ConcurrentModificationException;

/**
 * Bot 上下文
 * */
@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class BotContext {

    /**
     * 访问凭证
     * */
    private final Certificate certificate;

    /**
     * 当前 wss 连接链式处理
     * */
    private final ChainHandler chainHandler;

    @Setter(AccessLevel.NONE)
    private ChannelId channelId;

    /**
     * 接收到的消息序号
     * */
    private Long receivedSerialNumber;

    /**
     * 处理到的消息序号
     * */
    private Long handledSerialNumber;

    /**
     * 将链接绑定机器人上下文
     * */
    protected void bind(ChannelId id) {
        if (channelId != null)
            throw new ConcurrentModificationException(String.format("BotContext with channel-%s is repeatedly bound to channel-id%s", channelId.asLongText(), id.asLongText()));
        this.channelId = id;
    }

    /**
     * 创建机器人上下文
     * */
    public static BotContext create(Certificate certificate, ChainHandler chainHandler) {
        BotContext context = new BotContext(certificate, chainHandler);
        ChainHandler now = chainHandler;
        while (now != null) {
            now.setContext(context);
            now = now.next();
        }
        return context;
    }

}
