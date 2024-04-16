package cn.byteforge.openqq.ws.handler;

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
        JsonObject json = (JsonObject) object;
        // https://bot.q.qq.com/wiki/develop/api-v2/dev-prepare/error-trace/websocket.html
        if (json.has("code")) {
            throw new WebSocketInvokeException(object);
        } else if (json.get("op").getAsInt() == OpCode.INVALID_SESSION.getCode()) {
            log.error("检测到 Session 失效, 此时 token 是否已刷新 ? {}", OpCode.INVALID_SESSION);
            // TODO 是否要在此 new Session (前面new的session也过期了??
            // 关注 {"op":9,"d":false}, 看看 d 有没有其他值
            return null;
        }
        return json;
    }

}
