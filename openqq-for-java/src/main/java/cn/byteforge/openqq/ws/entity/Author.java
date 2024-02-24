package cn.byteforge.openqq.ws.entity;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * 消息发送者
 * */
@Data
public class Author {

    /**
     * @see #memberOpenid
     * */
    private String id;

    /**
     * 用户在本群的 member_openid
     * */
    @SerializedName("member_openid")
    private String memberOpenid;

}
