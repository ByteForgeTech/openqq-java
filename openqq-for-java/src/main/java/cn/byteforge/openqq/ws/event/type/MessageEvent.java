package cn.byteforge.openqq.ws.event.type;

import cn.byteforge.openqq.http.entity.MessageResponse;
import cn.byteforge.openqq.message.Message;
import cn.byteforge.openqq.message.MessageBuilder;
import cn.byteforge.openqq.ws.entity.data.MessageData;
import cn.byteforge.openqq.ws.event.Event;
import cn.hutool.core.lang.Assert;
import lombok.*;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 消息事件
 * */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public abstract class MessageEvent extends Event {

    /**
     * 事件ID
     * */
    private String id;

    /**
     * 事件字段数据
     * @apiNote 仅用于占位，使用时调用 get 方法获取子类重载后的字段值
     * */
    private transient MessageData d;

    /**
     * 当前发送的消息序列
     * */
    @Getter(AccessLevel.PROTECTED)
    private transient final AtomicInteger msgSeq = new AtomicInteger(1);

    /**
     * 发送回复（被动）消息
     * @apiNote 只需传入要发送的消息，无需设置 msg_id、msg_seq 等字段
     * */
    public MessageResponse reply(String message) {
        return reply(new MessageBuilder().addText(message).build());
    }

    /**
     * 发送回复（被动）消息
     * @apiNote 只需传入要发送的消息，无需设置 msg_id、msg_seq 等字段
     * */
    public MessageResponse reply(Message message) {
        Assert.isTrue(getMsgSeq().get() <= 4, "达到最大回复次数限制");
        return sendMessage(new MessageBuilder()
                .addMessage(message)
                .setPassive(getD().getId(), getMsgSeq().getAndIncrement())
                .build());
    }

    /**
     * 发送消息（默认主动）
     * */
    public MessageResponse sendMessage(String message) {
        return sendMessage(new MessageBuilder().addText(message).build());
    }

    /**
     * 发送消息（默认主动）
     * */
    public abstract MessageResponse sendMessage(Message message);

}
