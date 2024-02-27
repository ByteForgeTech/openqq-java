package cn.byteforge.openqq.ws.handler;

import cn.byteforge.openqq.ws.QQConnection;
import cn.byteforge.openqq.ws.entity.enumerate.OpCode;
import cn.byteforge.openqq.ws.event.Event;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;
import java.util.function.Consumer;

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
            QQConnection.reconnect(wssUrl, getUuid(), getContext(), reconnectCallback);
            log.info("QQ connection of uuid-{} already auto reconnected", getUuid());
            return null;
        }
        return o;
    }

}
