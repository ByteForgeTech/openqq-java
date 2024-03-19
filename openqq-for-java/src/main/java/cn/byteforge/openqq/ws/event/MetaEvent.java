package cn.byteforge.openqq.ws.event;

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

}
