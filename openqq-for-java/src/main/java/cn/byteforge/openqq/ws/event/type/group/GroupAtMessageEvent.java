package cn.byteforge.openqq.ws.event.type.group;

import cn.byteforge.openqq.http.OpenAPI;
import cn.byteforge.openqq.http.entity.MessageResponse;
import cn.byteforge.openqq.message.Message;
import cn.byteforge.openqq.ws.entity.data.GroupAtMessageData;
import cn.byteforge.openqq.ws.event.type.MessageEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 群聊@机器人
 * */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class GroupAtMessageEvent extends MessageEvent {

    /**
     * 事件字段数据
     * */
    private GroupAtMessageData d;

    /**
     * 发送群聊消息（默认主动）
     * */
    @Override
    public MessageResponse sendMessage(Message message) {
        return OpenAPI.sendGroupMessage(d.getGroupId(), message, getContext().getCertificate());
    }

}
