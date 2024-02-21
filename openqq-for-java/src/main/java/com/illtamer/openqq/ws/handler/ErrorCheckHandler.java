package com.illtamer.openqq.ws.handler;

import com.google.gson.JsonObject;
import com.illtamer.openqq.exception.WebSocketInvokeException;

/**
 * 检查错误码
 * */
public class ErrorCheckHandler extends ChainHandler {

    // JsonObject -> JsonObject
    @Override
    protected Object doHandle(Object object) {
        JsonObject json = (JsonObject) object;
        // TODO
        // https://bot.q.qq.com/wiki/develop/api-v2/dev-prepare/error-trace/websocket.html
        if (json.has("code")) {
            throw new WebSocketInvokeException(object);
        }
        return json;
    }

}
