package cn.byteforge.openqq.http.entity;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * 分片 WSS 接入点
 * */
@Data
public class SliceShard {

    /**
     * WebSocket 的连接地址
     * */
    private String url;

    /**
     * 建议的 shard 数
     * */
    private Integer shards;

    /**
     * 目前连接数使用情况
     * */
    @SerializedName("session_start_limit")
    private SessionStartLimit sessionStartLimit;

    @Data
    public static final class SessionStartLimit {

        /**
         * 每 24 小时可创建 Session 数
         * */
        private Integer total;

        /**
         * 目前还可以创建的 Session 数
         * */
        private Integer remaining;

        /**
         * 重置计数的剩余时间(ms)
         * */
        @SerializedName("reset_after")
        private Integer resetAfter;

        /**
         * 每 5s 可以创建的 Session 数
         * */
        @SerializedName("max_concurrency")
        private Integer maxConcurrency;

    }

}
