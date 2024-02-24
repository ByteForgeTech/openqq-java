package cn.byteforge.openqq.ws.entity.data;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class GroupAtMessageEventData extends MessageEventData {

    /**
     * @see #groupOpenid
     * */
    @SerializedName("group_id")
    private String groupId;

    /**
     * 群聊的 openid
     * */
    @SerializedName("group_openid")
    private String groupOpenid;

}
