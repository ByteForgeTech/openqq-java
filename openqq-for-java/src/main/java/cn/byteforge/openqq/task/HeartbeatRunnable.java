package cn.byteforge.openqq.task;

import cn.byteforge.openqq.util.Maps;
import cn.byteforge.openqq.ws.BotContext;
import cn.byteforge.openqq.ws.WebSocketAPI;
import cn.byteforge.openqq.ws.entity.enumerate.OpCode;

import java.util.UUID;

/**
 * Heartbeat with fixed interval
 * */
public class HeartbeatRunnable implements Runnable {

    private final UUID uuid;
    private final BotContext context;

    public HeartbeatRunnable(UUID uuid, BotContext context) {
        this.uuid = uuid;
        this.context = context;
    }

    @Override
    public void run() {
        // first d is null
        WebSocketAPI.send(Maps.of(
                "op", OpCode.HEARTBEAT.getCode(),
                "d", context.getReceivedSeqMap().get(uuid)
        ), uuid, WebSocketAPI.NO_NEED_CALLBACK, context);
    }

}
