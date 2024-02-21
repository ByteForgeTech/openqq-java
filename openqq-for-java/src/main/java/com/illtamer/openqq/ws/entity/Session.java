package com.illtamer.openqq.ws.entity;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * 会话 Session
 * */
@Data
public class Session {

    /**
     * 版本号
     * */
    private Integer version;

    /**
     * 会话 Id
     * */
    @SerializedName("session_id")
    private String sessionId;

    /**
     * 当前用户
     * */
    private User user;

    @Data
    public static final class User {

        /**
         * 用户 Id
         * */
        private String id;

        /**
         * 用户名
         * */
        private String username;

        /**
         * 是否是机器人
         * */
        private Boolean bot;

    }

    /**
     * 当前分片
     * TODO 查看分片返回值，封装：获取全部分片再返回
     * */
    private Integer[] shard;

}
