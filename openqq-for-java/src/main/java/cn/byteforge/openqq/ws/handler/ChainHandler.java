package cn.byteforge.openqq.ws.handler;

/**
 * 链式处理抽象类
 * */
public abstract class ChainHandler {

    private ChainHandler nextHandler;

    /**
     * 开始链式调用处理
     * @return 返回为空，则终止调用
     * */
    public Object handle(Object o) {
        Object r = doHandle(o);
        if (nextHandler != null) {
            return nextHandler.handle(r);
        }
        return r;
    }

    /**
     * 当前处理实现
     * */
    protected abstract Object doHandle(Object o);

    /**
     * 记录下一个处理类并返回
     * */
    public ChainHandler next(ChainHandler handler) {
        return this.nextHandler = handler;
    }

}
