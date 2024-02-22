package cn.byteforge.openqq.ws.handler;

import cn.byteforge.openqq.ws.BotContext;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

/**
 * 链式处理抽象类
 * */
public abstract class ChainHandler {

    // 当前机器人实例上下文对象
    @Setter
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
     * 当前处理实现
     * */
    protected abstract Object doHandle(Object o);

    /**
     * 记录下一个处理类并返回
     * */
    protected ChainHandler setNext(ChainHandler handler) {
        return this.nextHandler = handler;
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
