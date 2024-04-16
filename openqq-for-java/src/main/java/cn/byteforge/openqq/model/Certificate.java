package cn.byteforge.openqq.model;

import cn.byteforge.openqq.http.entity.AccessToken;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 访问凭证
 * */
@Getter
@AllArgsConstructor
@ToString
public class Certificate {

    private final String appId;

    private final String clientSecret;

    /**
     * 申请的 AccessToken
     * @apiNote 同一 appId 同一时间段对应唯一 token, 过期后方可更新
     * */
    private AccessToken accessToken;

    /**
     * 更新 AccessToken
     * */
    public void updateToken(AccessToken token) {
        this.accessToken = token;
    }

}
