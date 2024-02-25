package cn.byteforge.openqq.exception;

import java.util.Map;

/**
 * API 调用异常
 * */
public class APIInvokeException extends RuntimeException {

    public APIInvokeException(int code, String message, Map<String, Object> data) {
        super(String.format("API invoke exception, code: %d, message: %s with payload:\n%s", code, message, data));
    }

}
