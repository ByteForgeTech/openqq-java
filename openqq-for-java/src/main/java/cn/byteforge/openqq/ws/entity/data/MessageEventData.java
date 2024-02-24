package cn.byteforge.openqq.ws.entity.data;

import cn.byteforge.openqq.ws.entity.Author;
import lombok.Data;

@Data
public class MessageEventData {

    /**
     * 平台方消息 ID，可以用于被动消息发送
     * */
    private String id;

    /**
     * 发送者
     * */
    private Author author;

    /**
     * 消息内容
     * */
    private String content;

    /**
     * 消息生产时间（RFC3339）
     * */
    private String timestamp;

}
