package cn.byteforge.openqq.ws.handler;

import cn.byteforge.openqq.ws.entity.enumerate.IntentEnum;
import cn.byteforge.openqq.ws.event.Event;
import cn.byteforge.openqq.ws.event.EventListener;
import cn.hutool.core.lang.Assert;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 消息事件分发处理
 * */
@Slf4j
public class EventDispatchHandler extends ChainHandler {

    private final EventListener<? extends Event>[] listeners;

    @SafeVarargs
    public EventDispatchHandler(EventListener<? extends Event> ...listeners) {
        this.listeners = listeners;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    protected Object doHandle(Object o) {
        Event event = (Event) o;
        String eventType = event.getEventType();
        Optional<IntentEnum> optional = Arrays.stream(IntentEnum.values())
                .filter(type -> type.name().equals(eventType))
                .findFirst();
        if (!optional.isPresent()) {
            log.warn("Unknown event type: {}, dispatch skipped, is it up-to-data ?", eventType);
            return o;
        }
        int intent = optional.get().getIntent();
        for (EventListener listener : Arrays.stream(listeners)
                .filter(l -> (l.eventIntent().getValue() & intent) != 0)
                .collect(Collectors.toList())) {
            if (listener.ignoreCancelled() && event.isCancelled()) continue;
            try {
                Class<? extends Event> listenedEventType = getListenedEventType(listener);
                Assert.notNull(listenedEventType);
                if (Objects.requireNonNull(listenedEventType).isInstance(event)) {
                    listener.onEvent(event);
                }
            } catch (ClassCastException e) {
                log.error("Type conversion exception(event: {}), please check the method input parameters", event, e);
            } catch (Exception e) {
                log.error("Exception occurred when dispatching event({}) to listener#{}", event, listener.getClass().getName(), e);
            }
        }
        return o;
    }

    private static Class<?> getListenedEventType(EventListener<? extends Event> listener) {
        Type[] genericParameterTypes = listener.getClass().getGenericInterfaces();

        if (genericParameterTypes.length != 1 || !(genericParameterTypes[0] instanceof ParameterizedType)) return null;
        Type[] actualTypeArguments = ((ParameterizedType) genericParameterTypes[0])
                .getActualTypeArguments();

        if (actualTypeArguments.length != 1) return null;
        return (Class<?>) actualTypeArguments[0];
    }

}
