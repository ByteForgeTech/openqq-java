package cn.byteforge.openqq.exception;

/**
 * 未知OpCode异常
 * */
public class UnknownOpCodeException extends RuntimeException {

    public UnknownOpCodeException(int code) {
        super(String.format("Unknown opcode: %d", code));
    }

}
