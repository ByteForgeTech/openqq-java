package cn.byteforge.openqq.ws.entity.data;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import org.jetbrains.annotations.Nullable;

@Data
public class InteractionData {

    /**
     * 平台方事件 ID，可以用于被动消息发送
     * */
    private String id;

    /**
     * 消息按钮： 11，单聊快捷菜单：12
     * */
    private Integer type;

    /**
     * 事件发生的场景：c2c、group、guild
     * */
    private String scene;

    /**
     * 0 频道场景，1 群聊场景，2 单聊场景
     * */
    @SerializedName("chat_type")
    private Integer chatType;

    /**
     * 触发时间 RFC 3339 格式
     * */
    private String timestamp;

    /**
     * 单聊单聊按钮触发x，的用户 openid，仅在单聊场景提供该字段
     * */
    @SerializedName("user_openid")
    @Nullable
    private String userOpenid;

    /**
     * 群的 openid，仅在群聊场景提供该字段
     * */
    @SerializedName("group_openid")
    @Nullable
    private String groupOpenid;

    /**
     * 按钮触发用户，群聊的群成员 openid，仅在群聊场景提供该字段
     * */
    @SerializedName("group_member_openid")
    @Nullable
    private String groupMemberOpenid;

    private Data data;

    private Integer version;

    /**
     * 机器人的 appid
     * */
    @SerializedName("application_id")
    private String applicationId;


    @lombok.Data
    public static final class Data {

        private Resolved resolved;

    }

    @lombok.Data
    public static final class Resolved {

        /**
         * 操作按钮的 data 字段值（在发送消息按钮时设置）
         * */
        @SerializedName("button_data")
        private String buttonData;

        /**
         * 操作按钮的 id 字段值（在发送消息按钮时设置）
         * */
        @SerializedName("button_id")
        private String buttonId;

        /**
         * 操作的用户 userid，仅频道场景提供该字段
         * */
        @Nullable
        @SerializedName("user_id")
        private String userId;

        /**
         * 操作按钮的 id 字段值，仅自定义菜单提供该字段（在管理端设置）
         * */
        @Nullable
        @SerializedName("feature_id")
        private String featureId;

        /**
         * 操作的消息id，目前仅频道场景提供该字段
         * */
        @Nullable
        @SerializedName("message_id")
        private String messageId;

    }

}
