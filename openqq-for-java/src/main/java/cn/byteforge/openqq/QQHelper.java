package cn.byteforge.openqq;

import cn.byteforge.openqq.http.OpenAPI;
import cn.byteforge.openqq.http.entity.AccessToken;
import cn.byteforge.openqq.model.Certificate;
import cn.byteforge.openqq.ws.BotContext;
import cn.byteforge.openqq.ws.QQConnection;
import cn.byteforge.openqq.ws.entity.Session;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public class QQHelper {

    /**
     * 创建更新 Token 任务
     * @param context 需要刷新 token 的上下文对象
     * */
    public static Runnable refreshTokenRunnable(UUID uuid, BotContext context) {
        return refreshTokenRunnable(0, uuid, context);
    }

    /**
     * 创建更新 Token 任务
     * @param context 需要刷新 token 的上下文对象
     * @param thresholdSeconds token 过期检测阈值 (单位: 秒)
     * */
    public static Runnable refreshTokenRunnable(final int thresholdSeconds, UUID uuid, BotContext context) {
        return () -> {
            log.info("Start trying to update AccessToken");
            Certificate certificate = context.getCertificate();
            if (!certificate.getAccessToken().expired(thresholdSeconds)) {
                log.warn("AccessToken no expired, is it expired ?");
                return;
            }

            AccessToken token = OpenAPI.getAppAccessToken(certificate.getAppId(), certificate.getClientSecret());
            certificate.updateToken(token);
            log.info("AccessToken auto refreshed: {}", token.getContent());
        };
    }

    /**
     * 关闭当前链接
     * */
    @SneakyThrows
    public static void closeChannel(UUID uuid, BotContext context) {
       QQConnection.CLIENT_GROUPS.find(context.getConnMap().get(uuid).getKey()).close().sync();
    }

}
