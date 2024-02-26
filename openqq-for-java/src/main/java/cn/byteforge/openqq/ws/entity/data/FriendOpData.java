package cn.byteforge.openqq.ws.entity.data;

import lombok.Data;

/**
 * 好友操作机器人事件数据
 * */
@Data
public class FriendOpData {

    /**
     * 操作用户的 openid
     * */
    private String openid;

    /**
     * 操作时的时间戳
     * */
    private Integer timestamp;

}
