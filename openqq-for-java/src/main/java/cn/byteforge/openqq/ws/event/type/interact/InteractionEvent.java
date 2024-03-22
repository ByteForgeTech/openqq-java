package cn.byteforge.openqq.ws.event.type.interact;

import cn.byteforge.openqq.http.OpenAPI;
import cn.byteforge.openqq.http.entity.InteractResult;
import cn.byteforge.openqq.ws.entity.data.InteractionData;
import cn.byteforge.openqq.ws.event.Event;
import com.google.gson.Gson;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Markdown 按钮交互事件
 * */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class InteractionEvent extends Event {

    /**
     * 事件ID
     * */
    private String id;

    /**
     * 事件字段数据
     * */
    private InteractionData d;

    /**
     * 回调 QQ 后台，告知交互事件已经收到
     * @param result 交互结果
     * */
    public void callback(InteractResult result) {
        OpenAPI.callbackInteraction(d.getId(), result, getContext().getCertificate());
    }

}
