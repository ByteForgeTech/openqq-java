package cn.byteforge.openqq.ws.entity;

import cn.byteforge.openqq.model.Certificate;
import cn.byteforge.openqq.ws.handler.ChainHandler;
import cn.hutool.core.lang.Assert;
import io.netty.channel.ChannelId;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

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
     * 将链接绑定机器人上下文
     * */
    public void bind(ChannelId channelId) {
        Assert.isNull(channelId, "BotContext with channel-%s is repeatedly bound to channel-id%s", this.channelId.asLongText(), channelId.asLongText());
        this.channelId = channelId;
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
