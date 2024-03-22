package cn.byteforge.openqq.ws.event.type.friend;

import cn.byteforge.openqq.ws.entity.data.FriendOpData;
import cn.byteforge.openqq.ws.event.Event;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 用户添加机器人事件
 * */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class FriendAddEvent extends Event {

    /**
     * 事件字段数据
     * */
    private FriendOpData d;

}
