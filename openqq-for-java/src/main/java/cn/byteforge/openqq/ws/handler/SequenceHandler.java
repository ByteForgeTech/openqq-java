package cn.byteforge.openqq.ws.handler;

import cn.byteforge.openqq.ws.event.Event;

/**
 * 序列号处理
 * */
public class SequenceHandler extends ChainHandler {

    // Event -> Event
    @Override
    protected Object doHandle(Object o) {
        Event event = (Event) o;
        Long serialNumber = event.getSerialNumber();
        if (serialNumber != null) {
            getContext().setReceivedSequence(serialNumber);
        }
        return o;
    }

}
