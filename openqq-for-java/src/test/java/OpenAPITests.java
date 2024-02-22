import cn.byteforge.openqq.http.OpenAPI;
import cn.byteforge.openqq.http.entity.AccessToken;
import cn.byteforge.openqq.model.Certificate;
import cn.byteforge.openqq.ws.QQConnection;
import cn.byteforge.openqq.ws.WebSocketAPI;
import cn.byteforge.openqq.ws.entity.BotContext;
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

//    @BeforeAll
    static void setup() throws IOException {
        String appId = new String(Files.readAllBytes(Paths.get("../secrets/appId.txt")));
        String clientSecret = new String(Files.readAllBytes(Paths.get("../secrets/clientSecret.txt")));
        AccessToken token = OpenAPI.getAppAccessToken(appId, clientSecret);
        certificate = new Certificate(appId, clientSecret, token);
    }

    @Test
    void testFind() {
        ChainHandler chainHandler = ChainHandler.builder()
                .append(new ErrorCheckHandler())
                .append(new EventParseHandler())
                .append(new HeartbeatHandler())
//                .next(new SequenceHandler())
                .append(new APICallbackHandler()).build();
        System.out.println(chainHandler.find(EventParseHandler.class));
    }

    @Test
    void testGetUniversalWssUrl() throws Exception {
        int intents = 513;
        String wssUrl = OpenAPI.getUniversalWssUrl(certificate);
        ChainHandler chainHandler = ChainHandler.builder()
                .append(new ErrorCheckHandler())
                .append(new EventParseHandler())
                .append(new HeartbeatHandler())
//                .next(new SequenceHandler())
                .append(new APICallbackHandler())
                .append(new ChainHandler() {
                    // Event ->
                    @Override
                    protected Object doHandle(Object o) {
                        System.out.println(o);
                        return null;
                    }
                }).build();
        BotContext context = BotContext.create(certificate, chainHandler);
        QQConnection.connect(wssUrl, context, (id) -> {

            Session session = WebSocketAPI.getSession(intents, Shard.STANDALONE, null, context);

        });
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
