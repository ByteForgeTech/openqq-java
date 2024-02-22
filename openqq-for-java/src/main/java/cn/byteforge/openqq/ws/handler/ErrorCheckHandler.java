package cn.byteforge.openqq.ws.handler;

import cn.byteforge.openqq.exception.ErrorCheckException;
import cn.byteforge.openqq.ws.entity.OpCode;
import com.google.gson.JsonObject;
import cn.byteforge.openqq.exception.WebSocketInvokeException;

/**
 * 检查错误码
 * */
public class ErrorCheckHandler extends ChainHandler {

    // JsonObject -> JsonObject
    @Override
    protected Object doHandle(Object object) {
        JsonObject json = (JsonObject) object;
        // https://bot.q.qq.com/wiki/develop/api-v2/dev-prepare/error-trace/websocket.html
        if (json.has("code")) {
            throw new WebSocketInvokeException(object);
        } else if (json.get("op").getAsInt() == OpCode.INVALID_SESSION.getCode()) {
            throw new ErrorCheckException(String.format("%s: %s", OpCode.INVALID_SESSION.getName(), OpCode.INVALID_SESSION.getDescription()));
        }
        return json;
    }

}
