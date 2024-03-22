package cn.byteforge.openqq.ws.event;

import cn.byteforge.openqq.ws.BotContext;
import com.google.gson.JsonObject;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 元事件
 * */
@EqualsAndHashCode
@ToString
@Data
public class MetaEvent {

    /**
     * 事件是否取消
     * */
    private transient boolean cancelled;

    /**
     * 事件的原始 json 数据
     * */
    @ToString.Exclude
    private JsonObject json;

    /**
     * 事件对应的机器人上下文
     * */
    @ToString.Exclude
    private transient BotContext context;

}
