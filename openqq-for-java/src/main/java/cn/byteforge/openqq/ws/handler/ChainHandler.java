package cn.byteforge.openqq.ws.handler;

import cn.byteforge.openqq.ws.BotContext;
import cn.byteforge.openqq.ws.event.Event;
import cn.byteforge.openqq.ws.event.EventListener;
import cn.hutool.core.lang.Assert;
import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.Consumer;

/**
 * 链式处理抽象类
 * @apiNote 不负责状态维护，非线程安全
 * */
public abstract class ChainHandler {

    // 当前 ChainHandler 对应的 UUID
    @Getter(AccessLevel.PROTECTED)
    private UUID uuid;

    // 当前机器人实例上下文对象
    @Getter(AccessLevel.PROTECTED)
    private BotContext context;

    // 下一个链式调用对象
    private ChainHandler nextHandler;

    /**
     * 开始链式调用处理
     * @return 返回为空，则终止调用
     * */
    public Object handle(Object o) {
        if (o == null) return null;
        Object r = doHandle(o);
        if (nextHandler != null) {
            return nextHandler.handle(r);
        }
        return r;
    }

    /**
     * 获取下一个处理类
     * */
    public ChainHandler next() {
        return this.nextHandler;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public <T extends ChainHandler> T find(Class<T> clazz) {
        ChainHandler inst = this;
        while (inst != null) {
            if (clazz.isInstance(inst)) {
                return (T) inst;
            }
            inst = inst.nextHandler;
        }
        return null;
    }

    /**
     * 设置当前 ChainHandler 元数据
     * */
    public void setMetaData(UUID uuid, BotContext context) {
        Assert.isTrue(this.uuid == null && this.context == null, "ChainHandler metadata is set repeatedly");
        this.uuid = uuid;
        this.context = context;
    }

    /**
     * 当前处理实现
     * */
    protected abstract Object doHandle(Object o);

    /**
     * 记录下一个处理类并返回
     * */
    protected ChainHandler setNext(ChainHandler handler) {
        return this.nextHandler = handler;
    }

    /**
     * 创建默认链式调用组
     * @apiNote 默认链式调用组不保证线程安全，请自行注意
     * */
    @SafeVarargs
    public static ChainHandler defaultChainGroup(String wssUrl, @Nullable Consumer<UUID> reconnectCallback, EventListener<? extends Event> ...listeners) {
        return builder()
                .append(new ErrorCheckHandler())
                .append(new EventParseHandler())
                .append(new HeartbeatHandler())
                .append(new SequenceHandler.Received())
                .append(new AutoReconnectHandler(wssUrl, reconnectCallback))
                .append(new APICallbackHandler())
                .append(new EventDispatchHandler(listeners))
                .append(new SequenceHandler.Handled())
                .build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private ChainHandler start;
        private ChainHandler end;

        public Builder append(ChainHandler handler) {
            if (this.start == null) {
                this.start = (this.end = handler);
            } else {
                this.end = this.end.setNext(handler);
            }
            return this;
        }

        public ChainHandler build() {
            return this.start;
        }

    }

}
