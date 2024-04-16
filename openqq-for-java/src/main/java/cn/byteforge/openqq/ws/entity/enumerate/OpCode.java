package cn.byteforge.openqq.ws.entity.enumerate;

import cn.byteforge.openqq.exception.UnknownOpCodeException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Arrays;

@ToString
@Getter
@AllArgsConstructor
public enum OpCode {

    DISPATCH(0, "Dispatch", "Receive", "服务端进行消息推送"),

    HEARTBEAT(1, "Heartbeat", "Send/Receive", "客户端或服务端发送心跳"),

    IDENTIFY(2, "Identify", "Send", "客户端发送鉴权"),

    RESUME(6, "Resume", "Send", "客户端恢复连接"),

    RECONNECT(7, "Reconnect", "Receive", "服务端通知客户端重新连接"),

    INVALID_SESSION(9, "Invalid Session", "Receive", "当 identify 或 resume 的时候，如果参数有错，服务端会返回该消息"),

    HELLO(10, "Hello", "Receive", "当客户端与网关建立 ws 连接之后，网关下发的第一条消息"),

    HEARTBEAT_ACK(11, "Heartbeat ACK", "Receive/Reply", "当发送心跳成功之后，就会收到该消息"),

    HTTP_CALLBACK_ACK(12, "HTTP Callback ACK", "Reply", "仅用于 http 回调模式的回包，代表机器人收到了平台推送的数据");

    /**
     * CODE
     * */
    private final Integer code;

    /**
     * 名称
     * */
    private final String name;

    /**
     * 客户端行为
     * <br>
     * Receive 客户端接收到服务端 push 的消息
     * Send 客户端发送消息
     * Reply 客户端接收到服务端发送的消息之后的回包（HTTP 回调模式）
     * */
    private final String clientMethod;

    /**
     * 描述
     * */
    private final String description;

    public static OpCode parse(int code) {
        return Arrays.stream(OpCode.values())
                .filter(o -> o.code == code)
                .findFirst()
                .orElseThrow(() -> new UnknownOpCodeException(code));
    }

}
