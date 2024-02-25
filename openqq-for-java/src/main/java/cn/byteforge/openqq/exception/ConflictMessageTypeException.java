package cn.byteforge.openqq.exception;

/**
 * 消息类型冲突异常
 * */
public class ConflictMessageTypeException extends RuntimeException {

    public ConflictMessageTypeException(int msgType, int recordType) {
        super(String.format("Message type %d conflicts with record type %d", msgType, recordType));
    }

}
