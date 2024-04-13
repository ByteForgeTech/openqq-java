package cn.byteforge.openqq.ws.handler;

import cn.byteforge.openqq.ws.QQConnection;
import cn.byteforge.openqq.ws.entity.enumerate.OpCode;
import cn.byteforge.openqq.ws.event.Event;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;
import java.util.function.Consumer;

/**
 * 被动刷新 Session 处理
 * @apiNote 需要依赖服务器通知 (此 Handler 无法处理 Session / AccessToken 失效的情况)
 * */
@Slf4j
@AllArgsConstructor
public class AutoResumeHandler extends ChainHandler {

    private final String wssUrl;
    private final Consumer<UUID> reconnectCallback;

    // Event -> Event
    @SneakyThrows
    @Override
    protected Object doHandle(Object o) {
        Event event = (Event) o;
        if (event.getOpcode() == OpCode.RECONNECT) {
            QQConnection.resumeConnect(wssUrl, getUuid(), getContext(), reconnectCallback);
            log.info("Session of connection uuid-{} already auto resumed", getUuid());
            return null;
        }
        return o;
    }

}
