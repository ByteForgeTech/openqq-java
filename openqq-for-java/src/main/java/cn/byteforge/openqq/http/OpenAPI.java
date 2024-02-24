package cn.byteforge.openqq.http;

import cn.byteforge.openqq.Global;
import cn.byteforge.openqq.exception.APIInvokeException;
import cn.byteforge.openqq.http.entity.AccessToken;
import cn.byteforge.openqq.http.entity.RecommendShard;
import cn.byteforge.openqq.http.entity.Status;
import cn.byteforge.openqq.model.Certificate;
import cn.byteforge.openqq.util.Maps;
import cn.hutool.http.HttpGlobalConfig;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.Method;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * QQ 机器人 服务端开放的 openapi 接口对接
 * */
// TODO 更改url储存位置
// TODO 异步更新token
public class OpenAPI {

    private static final Gson GSON = new GsonBuilder().create();

    /**
     * 获取调用凭证
     * @param appId 在开放平台管理端上获得。
     * @param clientSecret 在开放平台管理端上获得。
     * */
    public static AccessToken getAppAccessToken(String appId, String clientSecret) {
        return getResponse("https://bots.qq.com/app/getAppAccessToken", Maps.of(
                "appId", appId,
                "clientSecret", clientSecret
        ), Method.POST, AccessToken.class, null);
    }

    /**
     * 获取通用 WSS 接入点
     * */
    public static String getUniversalWssUrl(Certificate cert) {
        return getAuthResponse("https://api.sgroup.qq.com/gateway", null, Method.GET, cert, JsonObject.class)
                .get("url").getAsString();
    }

    /**
     * 获取带推荐分片数的 WSS 接入点
     * */
    public static RecommendShard getRecommendShardWssUrls(Certificate cert) {
        return getAuthResponse("https://api.sgroup.qq.com/gateway/bot", null, Method.GET, cert, RecommendShard.class);
    }

    private static <T> T getAuthResponse(String url, @NotNull Map<String, Object> data, Method method, Certificate cert, Class<T> clazz) {
        return getResponse(url, data, method, clazz, Maps.of(
                "Authorization", String.format(Global.Authorization, cert.getAccessToken().getContent()),
                "X-Union-Appid", cert.getAppId()
        ));
    }

    /**
     * <a href="https://bot.q.qq.com/wiki/develop/api-v2/dev-prepare/error-trace/openapi.html">错误码介绍</a>
     * */
    private static <T> T getResponse(String url, Map<String, Object> data, Method method, Class<T> clazz, Map<String, String> headers) {
        try (HttpResponse response = HttpRequest.of(url)
                .method(method)
                .timeout(HttpGlobalConfig.getTimeout())
                .body(GSON.toJson(data))
                .headerMap(headers, true)
                .execute())
        {
            Status.Http httpStatus = Status.Http.parse(response.getStatus());
            if (!httpStatus.isSuccess()) {
                throw new APIInvokeException(httpStatus.getCode(), httpStatus.getMessage());
            }
            JsonObject object = GSON.fromJson(response.body(), JsonObject.class);
            if (object.has("code")) {
                throw new APIInvokeException(object.get("code").getAsInt(), object.get("message").getAsString());
            }
            return GSON.fromJson(object, clazz);
        }
    }

}