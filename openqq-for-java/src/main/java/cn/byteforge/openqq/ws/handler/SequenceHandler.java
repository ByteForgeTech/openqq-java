package cn.byteforge.openqq.ws.handler;

import cn.byteforge.openqq.ws.event.Event;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 序列号处理
 * */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SequenceHandler {

    /**
     * 收到的序列号记录
     * */
    public static final class Received extends ChainHandler {

        // Event -> Event
        @Override
        protected Object doHandle(Object o) {
            Event event = (Event) o;
            Long sequenceNumber = event.getSequenceNumber();
            if (sequenceNumber != null) {
                getContext().getReceivedSeqMap().put(getUuid(), sequenceNumber);
            }
            return o;
        }

    }

    /**
     * 完成处理的序列号记录
     * */
    public static final class Handled extends ChainHandler {

        // Event -> Event
        @Override
        protected Object doHandle(Object o) {
            Event event = (Event) o;
            Long sequenceNumber = event.getSequenceNumber();
            if (sequenceNumber != null) {
                getContext().getHandledSeqMap().put(getUuid(), sequenceNumber);
            }
            return o;
        }

    }

}
