import cn.byteforge.openqq.http.OpenAPI;
import cn.byteforge.openqq.http.entity.AccessToken;
import cn.byteforge.openqq.http.entity.RecommendShard;
import cn.byteforge.openqq.message.Message;
import cn.byteforge.openqq.message.MessageBuilder;
import cn.byteforge.openqq.model.Certificate;
import cn.byteforge.openqq.ws.BotContext;
import cn.byteforge.openqq.ws.QQConnection;
import cn.byteforge.openqq.ws.WebSocketAPI;
import cn.byteforge.openqq.ws.entity.Intent;
import cn.byteforge.openqq.ws.entity.Shard;
import cn.byteforge.openqq.ws.event.EventListener;
import cn.byteforge.openqq.ws.event.type.group.GroupAddRobotEvent;
import cn.byteforge.openqq.ws.handler.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
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
        RecommendShard shard = OpenAPI.getRecommendShardWssUrls(certificate);
        wssUrl = shard.getUrl();
        System.out.println(shard);
    }

    @Test
    void testStandalone() throws Exception {
        Intent intent = Intent.register().withAll().done();
        ChainHandler chainHandler = ChainHandler.builder()
                .append(new ErrorCheckHandler())
                .append(new EventParseHandler())
                .append(new HeartbeatHandler())
                .append(new SequenceHandler.Received())
                .append(new AutoReconnectHandler(wssUrl, uuid -> {
                    // do sth
                }))
                .append(new APICallbackHandler())
                .append(new EventDispatchHandler(new EventListener<GroupAddRobotEvent>() {
                    @Override
                    public void onEvent(GroupAddRobotEvent event) {
                        Message message = new MessageBuilder()
//                                .addTemplateMarkdown()
                                .build();
                    }

                    @Override
                    public Intent eventIntent() {
                        return Intent.register().withCustom(1 << 25).done();
                    }
                }))
                .append(new SequenceHandler.Handled())
                .build();

        QQConnection.connect(wssUrl, chainHandler, context,
                uuid -> WebSocketAPI.newShardSession(intent, uuid, Shard.STANDALONE, null, context),
                uuid -> {
                    // do sth
                });
    }

}
