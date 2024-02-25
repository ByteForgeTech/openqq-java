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

    private final AccessToken accessToken;

}
