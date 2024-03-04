package cn.byteforge.openqq.ws.event.type.interact;

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
     * 获取事件字段数据
     * */
    public InteractionData getData() {
        return new Gson().fromJson(getD(), InteractionData.class);
    }


}
