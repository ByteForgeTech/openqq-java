package cn.byteforge.openqq.ws.handler;

import cn.byteforge.openqq.ws.entity.enumerate.IntentEnum;
import cn.byteforge.openqq.ws.event.EventListener;
import cn.byteforge.openqq.ws.event.type.MessageEvent;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 消息事件分发处理
 * */
@Slf4j
public class MessageEventDispatchHandler extends ChainHandler {

    private final EventListener<? extends MessageEvent>[] listeners;

    @SafeVarargs
    public MessageEventDispatchHandler(EventListener<? extends MessageEvent> ...listeners) {
        this.listeners = listeners;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    protected Object doHandle(Object o) {
        if (!(o instanceof MessageEvent)) {
            return o;
        }
        MessageEvent event = (MessageEvent) o;
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
            try {
                listener.onEvent(event);
            } catch (ClassCastException e) {
                log.error("Type conversion exception(event: {}), please check the method input parameters", event, e);
            } catch (Exception e) {
                log.error("Exception occurred when dispatching event({}) to listener#{}", event, listener.getClass().getCanonicalName(), e);
            }
        }
        return o;
    }

}
