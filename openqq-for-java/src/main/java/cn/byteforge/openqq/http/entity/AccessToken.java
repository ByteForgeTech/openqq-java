package cn.byteforge.openqq.http.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 调用凭证
 * */
@Data
@NoArgsConstructor
public class AccessToken {

    @Expose
    private final long createTime = System.currentTimeMillis();

    /**
     * 获取到的凭证
     * */
    @SerializedName("access_token")
    private String content;

    /**
     * 凭证有效时间，单位：秒。目前是7200秒之内的值
     * */
    @SerializedName("expires_in")
    private String expiresIn;

    /**
     * 检查凭证是否在指定时间后过期
     * @param seconds 指定的时间秒数
     * */
    public boolean expired(int seconds) {
        long now = System.currentTimeMillis();
        long expirationTime = createTime + (Long.parseLong(expiresIn) * 1000);
        long threshold = now + (seconds * 1000L);
        return threshold >= expirationTime;
    }

}
