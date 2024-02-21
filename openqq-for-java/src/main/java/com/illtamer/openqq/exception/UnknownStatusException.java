package com.illtamer.openqq.exception;

/**
 * 未知状态异常
 * */
public class UnknownStatusException extends RuntimeException {

    public UnknownStatusException(int code) {
        super(String.format("Unknown status code: %d", code));
    }

}
