package cn.byteforge.openqq.ws.handler;

import cn.byteforge.openqq.ws.BotContext;
import cn.byteforge.openqq.ws.QQConnection;
import cn.byteforge.openqq.ws.entity.Session;
import cn.byteforge.openqq.ws.entity.enumerate.OpCode;
import cn.byteforge.openqq.ws.event.Event;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;
import java.util.function.Consumer;

/**
 * 服务器重连通知处理
 *      可以重新发送认证信息，激活websocket
 * @apiNote 需要依赖服务器通知 (此 Handler 无法处理 Session / AccessToken 失效的情况)
 * */
@Slf4j
@AllArgsConstructor
public class AutoReconnectHandler extends ChainHandler {

    private final String wssUrl;
    private final Consumer<UUID> reconnectCallback;

    // Event -> Event
    @SneakyThrows
    @Override
    protected Object doHandle(Object o) {
        Event event = (Event) o;
        if (event.getOpcode() == OpCode.RECONNECT) {
            // TODO 新开线程，转移consumer
            return null;
        }
        return o;
    }

}
