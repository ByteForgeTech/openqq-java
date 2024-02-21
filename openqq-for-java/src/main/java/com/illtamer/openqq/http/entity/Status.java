package com.illtamer.openqq.http.entity;

import com.illtamer.openqq.exception.UnknownStatusException;
import lombok.Data;

public class Status {

    /**
     * HTTP状态码
     * */
    @Data
    public static final class Http {

        private final int code;
        private final String message;
        private final boolean success;

        public static Http parse(int code) {
            switch (code) {
                case 200:
                    return new Http(200, "成功", true);
                case 204:
                    return new Http(204, "成功，但是无包体，一般用于删除操作", true);
                case 201:
                case 202:
                    return new Http(code, "异步操作成功，虽然说成功，但是会返回一个 error body，需要特殊处理", false);
                case 401:
                    return new Http(401, "认证失败", false);
                case 404:
                    return new Http(404, "未找到 API", false);
                case 405:
                    return new Http(405, "http method 不允许", false);
                case 429:
                    return new Http(429, "频率限制", false);
                case 500:
                case 504:
                    return new Http(code, "处理失败", false);
                default:
                    throw new UnknownStatusException(code);
            }
        }

    }

}
