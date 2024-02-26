package cn.byteforge.openqq.ws.entity.data;

import lombok.Data;

/**
 * 群操作机器人事件数据
 * */
@Data
public class GroupOpRobotData {

    /**
     * 触发的群的 openid
     * */
    private String groupId;

    /**
     * 操作机器人的群成员 openid
     * */
    private String opMemberId;

    /**
     * 操作的时间戳
     * */
    private Integer timestamp;

}
