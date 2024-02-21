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

    private final String AppId;

    private final String ClientSecret;

    private final AccessToken accessToken;

}
