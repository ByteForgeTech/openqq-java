import cn.byteforge.openqq.http.OpenAPI;
import cn.byteforge.openqq.http.entity.AccessToken;
import cn.byteforge.openqq.model.Certificate;
import cn.byteforge.openqq.ws.QQConnection;
import cn.byteforge.openqq.ws.WebSocketAPI;
import cn.byteforge.openqq.ws.BotContext;
import cn.byteforge.openqq.ws.entity.Shard;
import cn.byteforge.openqq.ws.handler.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OpenAPITests {

    private static final ExecutorService SERVICE = Executors.newFixedThreadPool(3);
    private static BotContext context;
    private static String wssUrl;

    @BeforeAll
    static void setup() throws Exception {
        String appId = new String(Files.readAllBytes(Paths.get("../secrets/appId.txt")));
        String clientSecret = new String(Files.readAllBytes(Paths.get("../secrets/clientSecret.txt")));
        AccessToken token = OpenAPI.getAppAccessToken(appId, clientSecret);
        Certificate certificate = new Certificate(appId, clientSecret, token);
        context = BotContext.create(certificate);
        wssUrl = OpenAPI.getUniversalWssUrl(certificate);
    }

    private static UUID resumeUUID = null;
    @Test
    void testResume() throws Exception {
        int intents = 33554432;
        // TODO event bus, 可选事件监听
        ChainHandler chainHandler = ChainHandler.builder()
                .append(new ErrorCheckHandler())
                .append(new EventParseHandler())
                .append(new HeartbeatHandler())
                .append(new SequenceHandler.Received())
                .append(new APICallbackHandler())
                .append(new ChainHandler() {
                    // Event ->
                    @Override
                    protected Object doHandle(Object o) {
                        System.out.println(o);
                        return o;
                    }
                })
                .append(new SequenceHandler.Handled()).build();


        // 一个 context 多个connect(Shard)，每个 shard 一个 chainHandler
        QQConnection.connect(wssUrl, chainHandler, context, (uuid) -> {
            resumeUUID = uuid;
            WebSocketAPI.newShardSession(intents, uuid, Shard.STANDALONE, null, context);
            System.out.printf("QQ connection created with uuid-%s%n", uuid);

            try {
                System.out.println("正在断开连接");
                QQConnection.CLIENT_GROUPS.find(context.getConnMap().get(uuid).getKey()).close().sync();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        QQConnection.reconnect(wssUrl, resumeUUID, context, uuid -> {
            System.out.println("重连成功");
        });
    }

    @Test
    void testShard() throws Exception {
        int intents = 33554432;

        for (Shard shard : Shard.generate(3)) {
            SERVICE.submit(() -> {
                ChainHandler chainHandler = ChainHandler.builder()
                        .append(new ErrorCheckHandler())
                        .append(new EventParseHandler())
                        .append(new HeartbeatHandler())
                        .append(new SequenceHandler.Received())
                        .append(new APICallbackHandler())
                        .append(new ChainHandler() {
                            // Event ->
                            @Override
                            protected Object doHandle(Object o) {
                                System.out.println(o);
                                return null;
                            }
                        })
                        .append(new SequenceHandler.Handled())
                        .build();

                // 一个 context 多个connect(Shard)，每个 shard 一个 chainHandler
                try {
                    QQConnection.connect(wssUrl, chainHandler, context, (uuid) -> {
                        WebSocketAPI.newShardSession(intents, uuid, shard, null, context);
                        System.out.printf("QQ connection created with uuid-%s%n", uuid);
                    });
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            Thread.sleep(2000);
        }
        Thread.sleep(5000);
        System.out.println(context);
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
