package cn.byteforge.openqq.exception;

/**
 * WebSocket 调用异常
 * TODO
 * */
public class WebSocketInvokeException extends RuntimeException {

    public WebSocketInvokeException(Object o) {
        super(o.toString());
    }

    /**
     * 	是否可以重试 RESUME
     * */
    public boolean canResume() {
        throw new UnsupportedOperationException();
    }

    /**
     * 是否可以重试 IDENTIFY
     * */
    public boolean canIdentify() {
        throw new UnsupportedOperationException();
    }

}
