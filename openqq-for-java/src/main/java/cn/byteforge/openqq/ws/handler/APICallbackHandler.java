package cn.byteforge.openqq.ws.handler;

import cn.byteforge.openqq.ws.event.Event;
import cn.byteforge.openqq.ws.event.EventType;
import cn.hutool.core.lang.Assert;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * API 响应处理
 * */
@Getter
public class APICallbackHandler extends ChainHandler {

    private final int timeoutSeconds;
    // EventType: JsonData
    private final Map<String, BlockingQueue<JsonObject>> dataMap = new HashMap<>();

    public APICallbackHandler() {
        this(3);
    }

    public APICallbackHandler(int timeoutSeconds) {
        this.timeoutSeconds = timeoutSeconds;
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
                dataMap.get(EventType.READY).put(event.getJson().getAsJsonObject("d"));
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

    /**
     * @return 接口返回等待超时时返回为空
     * */
    @Nullable
    public JsonObject getCallback(@Nullable String eventName) throws InterruptedException {
        if (eventName == null) return null;
        BlockingQueue<JsonObject> queue = dataMap.get(eventName);
        Assert.notNull(queue, String.format("Unregistered event return type: %s", eventName));
        return queue.poll(timeoutSeconds, TimeUnit.SECONDS);
    }

}
