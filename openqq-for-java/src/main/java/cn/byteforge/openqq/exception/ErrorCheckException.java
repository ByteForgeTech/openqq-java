package cn.byteforge.openqq.exception;

import cn.byteforge.openqq.ws.entity.enumerate.OpCode;
import lombok.Getter;

/**
 * 错误检查异常
 * */
@Getter
public class ErrorCheckException extends RuntimeException {

    private final OpCode opCode;

    public ErrorCheckException(OpCode opCode) {
        super(String.format("%s: %s", opCode.getName(), opCode.getDescription()));
        this.opCode = opCode;
    }

}
