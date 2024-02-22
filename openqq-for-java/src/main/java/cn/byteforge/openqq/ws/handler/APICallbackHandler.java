package cn.byteforge.openqq.ws.handler;

import cn.byteforge.openqq.ws.event.Event;

/**
 * API 响应处理
 * */
public class APICallbackHandler extends ChainHandler {

    // Event -> Event
    @Override
    protected Object doHandle(Object o) {
        Event event = (Event) o;

        return null;
    }

}
