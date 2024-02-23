package cn.byteforge.openqq.ws.handler;

import cn.byteforge.openqq.ws.event.Event;
import cn.byteforge.openqq.ws.event.EventType;
import cn.hutool.core.lang.Assert;
import com.google.gson.JsonObject;
import lombok.SneakyThrows;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * API 响应处理
 * */
public class APICallbackHandler extends ChainHandler {

    // EventType: JsonData
    private final Map<String, BlockingQueue<JsonObject>> dataMap = new ConcurrentHashMap<>();

    public APICallbackHandler() {
        initDataMap();
    }

    private void initDataMap() {
        // TODO 枚举筛选注册
        List<String> eventTypes = Arrays.asList(EventType.READY, EventType.RESUMED);
        for (String eventType : eventTypes) {
            dataMap.computeIfAbsent(eventType, k -> new LinkedBlockingQueue<>());
        }
    }

    // Event -> Event
    @SneakyThrows
    @Override
    protected Object doHandle(Object o) {
        Event event = (Event) o;
        switch (event.getEventType()) {
            case EventType.READY: {
                dataMap.get(EventType.READY).put((JsonObject) event.getData());
                return null;
            }
            case EventType.RESUMED: {
                dataMap.get(EventType.RESUMED).put(new JsonObject());
                return null;
            }
            default:
                return o;
        }
    }

    @Nullable
    public JsonObject getCallback(@Nullable String eventName) throws InterruptedException {
        if (eventName == null) return null;
        BlockingQueue<JsonObject> queue = dataMap.get(eventName);
        Assert.notNull(queue, String.format("Unregistered event return type: %s", eventName));
        return queue.take();
    }

}
