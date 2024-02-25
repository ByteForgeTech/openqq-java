package cn.byteforge.openqq.ws.handler;

import cn.byteforge.openqq.ws.QQConnection;
import cn.byteforge.openqq.ws.entity.enumerate.OpCode;
import cn.byteforge.openqq.ws.event.Event;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.util.UUID;
import java.util.function.Consumer;

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
            return null;
        }
        return o;
    }

}
