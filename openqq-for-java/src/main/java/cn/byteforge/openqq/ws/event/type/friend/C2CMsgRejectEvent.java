package cn.byteforge.openqq.ws.event.type.friend;

import cn.byteforge.openqq.ws.entity.data.FriendOpData;
import cn.byteforge.openqq.ws.event.Event;
import com.google.gson.Gson;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 拒绝机器人主动消息事件
 * */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class C2CMsgRejectEvent extends Event {

    /**
     * 获取事件字段数据
     * */
    public FriendOpData getData() {
        return new Gson().fromJson(getD(), FriendOpData.class);
    }

}
