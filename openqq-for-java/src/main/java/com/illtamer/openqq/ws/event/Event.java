package com.illtamer.openqq.ws.event;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.illtamer.openqq.ws.entity.OpCode;
import lombok.Data;

@Data
public class Event {

    /**
     * 长连接维护 OpCode
     * */
    @SerializedName("op")
    private OpCode opcode;

    /**
     * 序列号
     * <br>
     * 下行消息都会有一个序列号，标识消息的唯一性，客户端需要再发送心跳的时候，携带客户端收到的最新的s。
     * */
    @SerializedName("s")
    private String serialNumber;

    /**
     * 事件类型
     * */
    @SerializedName("t")
    private String eventType;

    /**
     * 事件内容
     * <br>
     * 不同事件类型的事件内容格式都不同，请注意识别。
     * */
    @SerializedName("d")
    private JsonObject data;

}
