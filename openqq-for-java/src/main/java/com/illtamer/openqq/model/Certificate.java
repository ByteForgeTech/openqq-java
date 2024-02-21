package com.illtamer.openqq.model;

import com.illtamer.openqq.http.entity.AccessToken;
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
