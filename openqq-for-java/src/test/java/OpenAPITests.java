import cn.byteforge.openqq.http.OpenAPI;
import cn.byteforge.openqq.http.entity.AccessToken;
import cn.byteforge.openqq.model.Certificate;
import cn.byteforge.openqq.ws.QQConnection;
import cn.byteforge.openqq.ws.WebSocketAPI;
import cn.byteforge.openqq.ws.BotContext;
import cn.byteforge.openqq.ws.entity.Session;
import cn.byteforge.openqq.ws.entity.Shard;
import cn.byteforge.openqq.ws.handler.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class OpenAPITests {

    private static Certificate certificate;

    @BeforeAll
    static void setup() throws Exception {
        String appId = new String(Files.readAllBytes(Paths.get("../secrets/appId.txt")));
        String clientSecret = new String(Files.readAllBytes(Paths.get("../secrets/clientSecret.txt")));
        AccessToken token = OpenAPI.getAppAccessToken(appId, clientSecret);
        certificate = new Certificate(appId, clientSecret, token);

        int intents = 33554432;
        String wssUrl = OpenAPI.getUniversalWssUrl(certificate);
        System.out.println(wssUrl);
        // TODO event bus, 可选事件监听
        ChainHandler chainHandler = ChainHandler.builder()
                .append(new ErrorCheckHandler())
                .append(new EventParseHandler())
                .append(new HeartbeatHandler())
                .append(new SequenceHandler())
                .append(new APICallbackHandler())
                .append(new ChainHandler() {
                    // Event ->
                    @Override
                    protected Object doHandle(Object o) {
                        System.out.println(o);
                        return null;
                    }
                }).build();
        BotContext context = BotContext.create(certificate);
        // BotContext.newStandalone();
        // BotContext.newShared();
        // 一个 context 多个connect(Shard)，每个 shard 一个 chainHandler
        QQConnection.connect(wssUrl, chainHandler, context, (id) -> {
            // TODO 提供不分片和分片两种创建方法
            Session session = WebSocketAPI.getSession(intents, Shard.STANDALONE, null, context);
//            context.addSession(session);
            System.out.println(session);
        });
    }

    @Test
    void testResume() throws Exception {

    }

    @Test
    void testInterrupt() throws Exception {
//        Runnable thread = () -> {
//            System.out.println("输出" + new Date().getSeconds());
//        };
//        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
//        ScheduledFuture<?> future = service.scheduleAtFixedRate(thread, 0L, 15L, TimeUnit.SECONDS);
//        Thread.sleep(5000L);
//        System.out.println("中断输出" + new Date().getSeconds());
//        future.cancel(true);
    }

}
