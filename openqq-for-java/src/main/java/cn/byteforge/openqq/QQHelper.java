package cn.byteforge.openqq;

import cn.byteforge.openqq.http.OpenAPI;
import cn.byteforge.openqq.http.entity.AccessToken;
import cn.byteforge.openqq.model.Certificate;
import cn.byteforge.openqq.ws.BotContext;
import cn.byteforge.openqq.ws.QQConnection;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;

@Slf4j
public class QQHelper {

    private static final Map<UUID, ScheduledExecutorService> EXECUTOR_MAP = new ConcurrentHashMap<>();

    /**
     * 开始自动更新 Token 任务
     * @param context 需要刷新 token 的上下文对象
     * */
    public static void startAutoRefreshToken(UUID uuid, BotContext context) {
        ScheduledExecutorService executor = EXECUTOR_MAP.get(uuid);
        if (executor != null) {
            executor.shutdownNow();
        }
        executor = Executors.newScheduledThreadPool(1);
        EXECUTOR_MAP.put(uuid, executor);
        executor.schedule(
                () -> {
                    log.info("Start trying to update AccessToken");
                    Certificate certificate = context.getCertificate();
                    if (!certificate.getAccessToken().expired(0)) {
                        log.error("AccessToken no expired, is it expired ?");
                        return;
                    }

                    AccessToken token = OpenAPI.getAppAccessToken(certificate.getAppId(), certificate.getClientSecret());
                    certificate.updateToken(token);
                    log.info("AccessToken auto refreshed: {}", token.getContent());
                },
                Integer.parseInt(context.getCertificate().getAccessToken().getExpiresIn()),
                TimeUnit.SECONDS
        );
    }

    /**
     * 关闭当前链接
     * */
    @SneakyThrows
    public static void closeChannel(UUID uuid, BotContext context) {
       QQConnection.CLIENT_GROUPS.find(context.getConnMap().get(uuid).getKey()).close().sync();
    }

}
