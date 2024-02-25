package cn.byteforge.openqq.http;

import cn.byteforge.openqq.Global;
import cn.byteforge.openqq.exception.APIInvokeException;
import cn.byteforge.openqq.http.entity.*;
import cn.byteforge.openqq.message.Message;
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
public class OpenAPI {

    private static final Gson GSON = new GsonBuilder().create();

    /**
     * 发送群聊消息
     * @param groupId 群聊的 openid
     * @param message 消息实例
     * @param cert 访问凭证
     * */
    public static MessageResponse sendGroupMessage(String groupId, Message message, Certificate cert) {
        return getAuthResponse(String.format("https://api.sgroup.qq.com/v2/groups/%s/messages", groupId), message.getData(), Method.POST, cert, MessageResponse.class);
    }

    /**
     * 上传文件到群组
     * @param userId 用户的 openid
     * @param url 需要发送媒体资源的url
     * @param send 设置 true 会直接发送消息到目标端，且会占用主动消息频次
     * @param type 媒体类型：1 图片，2 视频，3 语音，4 文件（暂不开放）
     * 资源格式要求
     * 图片：png/jpg，视频：mp4，语音：silk
     * @param cert 访问凭证
     * */
    public static FileInfo uploadPrivateFile(String userId, String url, boolean send, UploadFileType type, Certificate cert) {
        return doUploadGroupFile("users", userId, url, send, type, cert);
    }

    /**
     * 上传文件到群组
     * @param groupId 群聊的 openid
     * @param url 需要发送媒体资源的url
     * @param send 设置 true 会直接发送消息到目标端，且会占用主动消息频次
     * @param type 媒体类型：1 图片，2 视频，3 语音，4 文件（暂不开放）
     * 资源格式要求
     * 图片：png/jpg，视频：mp4，语音：silk
     * @param cert 访问凭证
     * */
    public static FileInfo uploadGroupFile(String groupId, String url, boolean send, UploadFileType type, Certificate cert) {
        return doUploadGroupFile("groups", groupId, url, send, type, cert);
    }

    private static FileInfo doUploadGroupFile(String api, String openId, String url, boolean send, UploadFileType type, Certificate cert) {
        return getAuthResponse(String.format("https://api.sgroup.qq.com/v2/%s/%s/files", api, openId), Maps.of(
                "file_type", type.getValue(),
                "url", url,
                "srv_send_msg", send
        ), Method.POST, cert, FileInfo.class);
    }

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
     * @param cert 访问凭证
     * */
    public static String getUniversalWssUrl(Certificate cert) {
        return getAuthResponse("https://api.sgroup.qq.com/gateway", null, Method.GET, cert, JsonObject.class)
                .get("url").getAsString();
    }

    /**
     * 获取带推荐分片数的 WSS 接入点
     * @param cert 访问凭证
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
            // {"message":"send msg err","code":22013,"err_code":22013,"trace_id":"eaab6442592c90cffa1ca71ce5f7c670"}
            Status.Http httpStatus = Status.Http.parse(response.getStatus());
            if (!httpStatus.isSuccess() && response.bodyBytes().length == 0) {
                throw new APIInvokeException(httpStatus.getCode(), httpStatus.getMessage(), data);
            }
            JsonObject object = GSON.fromJson(response.body(), JsonObject.class);
            if (object.has("code")) {
                throw new APIInvokeException(object.get("code").getAsInt(), object.get("message").getAsString(), data);
            }
            return GSON.fromJson(object, clazz);
        }
    }

}