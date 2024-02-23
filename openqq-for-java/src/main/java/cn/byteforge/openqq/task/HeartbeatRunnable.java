package cn.byteforge.openqq.task;

import cn.byteforge.openqq.util.Maps;
import cn.byteforge.openqq.ws.BotContext;
import cn.byteforge.openqq.ws.WebSocketAPI;
import cn.byteforge.openqq.ws.entity.OpCode;

/**
 * Heartbeat with fixed interval
 * */
public class HeartbeatRunnable implements Runnable {

    private final BotContext context;

    public HeartbeatRunnable(BotContext context) {
        this.context = context;
    }

    @Override
    public void run() {
        // first d is null
        WebSocketAPI.send(Maps.of(
                "op", OpCode.HEARTBEAT.getCode(),
                "d", context.getReceivedSequence()
        ), null, WebSocketAPI.NO_NEED_CALLBACK, context);
    }

}
