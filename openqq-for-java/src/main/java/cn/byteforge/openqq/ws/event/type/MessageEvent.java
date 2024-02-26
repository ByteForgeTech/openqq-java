package cn.byteforge.openqq.ws.event.type;

import cn.byteforge.openqq.ws.entity.data.MessageData;
import cn.byteforge.openqq.ws.event.Event;
import com.google.gson.Gson;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 消息事件
 * */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public abstract class MessageEvent extends Event {

    /**
     * 	平台方消息ID，可以用于被动消息发送
     * */
    private String id;

    /**
     * 获取事件字段数据
     * */
    public MessageData getData() {
        return new Gson().fromJson(getD(), MessageData.class);
    }

}
