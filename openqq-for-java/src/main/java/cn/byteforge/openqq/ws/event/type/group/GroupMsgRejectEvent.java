package cn.byteforge.openqq.ws.event.type.group;

import cn.byteforge.openqq.ws.entity.data.GroupOpRobotData;
import cn.byteforge.openqq.ws.event.Event;
import com.google.gson.Gson;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 群聊拒绝机器人主动消息事件
 * */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class GroupMsgRejectEvent extends Event {

    /**
     * 获取事件字段数据
     * */
    public GroupOpRobotData getData() {
        return new Gson().fromJson(getD(), GroupOpRobotData.class);
    }

}
