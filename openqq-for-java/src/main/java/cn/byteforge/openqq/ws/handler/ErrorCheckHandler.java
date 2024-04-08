package cn.byteforge.openqq.ws.handler;

import cn.byteforge.openqq.exception.ErrorCheckException;
import cn.byteforge.openqq.exception.WebSocketInvokeException;
import cn.byteforge.openqq.ws.entity.enumerate.OpCode;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;

/**
 * 检查错误码
 * */
@Slf4j
public class ErrorCheckHandler extends ChainHandler {

    // JsonObject -> JsonObject
    @Override
    protected Object doHandle(Object object) {
        throw new ErrorCheckException(OpCode.INVALID_SESSION);
//        JsonObject json = (JsonObject) object;
//        // https://bot.q.qq.com/wiki/develop/api-v2/dev-prepare/error-trace/websocket.html
//        if (json.has("code")) {
//            throw new WebSocketInvokeException(object);
//        } else if (json.get("op").getAsInt() == OpCode.INVALID_SESSION.getCode()) {
//            throw new ErrorCheckException(OpCode.INVALID_SESSION);
//        }
//        return json;
    }

}
