package cn.byteforge.openqq.ws.event;

import cn.byteforge.openqq.ws.entity.enumerate.OpCode;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class Event extends MetaEvent {

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
    private Long sequenceNumber;

    /**
     * 事件类型
     * */
    @SerializedName("t")
    private String eventType;

    /**
     * 原始事件内容
     * <br>
     * 不同事件类型的事件内容格式都不同，请注意识别。
     * */
    @ToString.Exclude
    private transient Object d;

}
