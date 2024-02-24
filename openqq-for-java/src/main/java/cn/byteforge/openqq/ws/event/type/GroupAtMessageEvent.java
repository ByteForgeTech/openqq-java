package cn.byteforge.openqq.ws.event.type;

import cn.byteforge.openqq.ws.entity.data.GroupAtMessageEventData;
import com.google.gson.Gson;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 群聊@机器人
 * */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class GroupAtMessageEvent extends MessageEvent {

    /**
     * 获取事件字段数据
     * */
    public GroupAtMessageEventData getData() {
        return new Gson().fromJson(getD(), GroupAtMessageEventData.class);
    }

}
