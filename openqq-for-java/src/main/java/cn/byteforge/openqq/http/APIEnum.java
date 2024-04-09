package cn.byteforge.openqq.http;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum APIEnum {

    SEND_GROUP_MESSAGE("https://api.sgroup.qq.com/v2/groups/%s/messages", true),

    UPLOAD_FILE("https://api.sgroup.qq.com/v2/%s/%s/files", true),

    INTERACTION_CALLBACK("https://api.sgroup.qq.com/interactions/%s", true),

    GET_APP_ACCESS_TOKEN("https://bots.qq.com/app/getAppAccessToken", false),

    GET_WSS_URL("https://api.sgroup.qq.com/gateway", true),

    GET_SHARD_WSS_URL("https://api.sgroup.qq.com/gateway/bot", true),
    ;

    private final String url;
    private final boolean needAuth;

    public Url format(String ...args) {
        return new Url(url, args);
    }

    @Getter
    @AllArgsConstructor
    public static class Url {

        private final String url;
        private Object[] args;

        @Override
        public String toString() {
            return String.format(url, args);
        }

    }

}
