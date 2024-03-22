package cn.byteforge.openqq.ws.event.type.group;

import cn.byteforge.openqq.ws.entity.data.GroupOpRobotData;
import cn.byteforge.openqq.ws.event.Event;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 机器人退出群聊事件
 * */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class GroupDelRobotEvent extends Event {

    /**
     * 事件字段数据
     * */
    private GroupOpRobotData d;

}
