package cn.byteforge.openqq.http.entity;

import lombok.Data;

/**
 * 消息响应
 * */
@Data
public class MessageResponse {

    /**
     * 消息唯一 ID
     * */
    private String id;

    /**
     * 发送时间
     * */
    private String timestamp;

}
