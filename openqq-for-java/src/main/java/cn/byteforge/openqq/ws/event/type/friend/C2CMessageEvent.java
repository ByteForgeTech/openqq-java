package cn.byteforge.openqq.ws.event.type.friend;

import cn.byteforge.openqq.http.entity.MessageResponse;
import cn.byteforge.openqq.message.Message;
import cn.byteforge.openqq.ws.event.type.MessageEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 单聊消息
 * TODO
 * */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class C2CMessageEvent extends MessageEvent {

    // private C2CMessageData d;

    @Override
    public MessageResponse sendMessage(Message message) {
        throw new UnsupportedOperationException();
    }

}
