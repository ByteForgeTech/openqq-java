package cn.byteforge.openqq.ws.handler;

import cn.byteforge.openqq.task.HeartbeatRunnable;
import cn.byteforge.openqq.ws.event.Event;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 心跳处理
 * TODO
 * */
@Slf4j
public class HeartbeatHandler extends ChainHandler {

    private final ScheduledExecutorService executor;
    private ScheduledFuture<?> scheduledFuture;

    public HeartbeatHandler() {
        executor = Executors.newSingleThreadScheduledExecutor();
    }

    // Event -> Event
    @Override
    protected Object doHandle(Object o) {
        Event event = (Event) o;
        switch (event.getOpcode()) {
            case HELLO: {
                if (scheduledFuture != null) {
                    scheduledFuture.cancel(true);
                    log.warn("Duplicate heartbeat thread detected !");
                }
                // start to heartbeat
                long interval = event.getData().get("heartbeat_interval").getAsLong();
                interval -= (interval / 10) * 2;
                scheduledFuture = executor.scheduleAtFixedRate(new HeartbeatRunnable(), 0L, interval, TimeUnit.MILLISECONDS);
                log.info("Heartbeat thread start");
                return null;
            }
            case HEARTBEAT: {
                log.debug("Received HEARTBEAT");
                return null;
            }
            case HEARTBEAT_ACK: {
                log.debug("Received HEARTBEAT_ACK");
                return null;
            }
        }
        return event;
    }

}
