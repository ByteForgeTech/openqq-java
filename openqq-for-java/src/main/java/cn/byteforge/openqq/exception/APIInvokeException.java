package cn.byteforge.openqq.exception;

/**
 * API 调用异常
 * */
public class APIInvokeException extends RuntimeException {

    public APIInvokeException(int code, String message, String body) {
        super(String.format("API invoke exception, code: %d, message: %s with payload:\n%s", code, message, body));
    }

}
