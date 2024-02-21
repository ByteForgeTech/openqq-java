import com.illtamer.openqq.http.OpenAPI;
import com.illtamer.openqq.http.entity.AccessToken;
import com.illtamer.openqq.model.Certificate;
import com.illtamer.openqq.ws.QQConnection;
import com.illtamer.openqq.ws.handler.ChainHandler;
import com.illtamer.openqq.ws.handler.ErrorCheckHandler;
import com.illtamer.openqq.ws.handler.EventParseHandler;
import com.illtamer.openqq.ws.handler.HeartbeatHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class OpenAPITests {

    private static Certificate certificate;

    @BeforeAll
    static void setup() throws IOException {
        String appId = new String(Files.readAllBytes(Paths.get("../secrets/appId.txt")));
        String clientSecret = new String(Files.readAllBytes(Paths.get("../secrets/clientSecret.txt")));
        AccessToken token = OpenAPI.getAppAccessToken(appId, clientSecret);
        certificate = new Certificate(appId, clientSecret, token);
    }

    @Test
    void testGetUniversalWssUrl() throws Exception {
        String wssUrl = OpenAPI.getUniversalWssUrl(certificate);
//        ChainContext context = new ChainContext();
        ChainHandler checkHandler = new ErrorCheckHandler()
                .next(new EventParseHandler())
                .next(new HeartbeatHandler())
//                .next(new APICallbackHandler())
                .next(new ChainHandler() {
                    // Event ->
                    @Override
                    protected Object doHandle(Object o) {
                        System.out.println(o);
                        return null;
                    }
                });
        QQConnection.connect(wssUrl, checkHandler, (id) -> {
            ;
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
