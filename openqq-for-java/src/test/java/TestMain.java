import cn.byteforge.openqq.http.OpenAPI;
import cn.byteforge.openqq.http.entity.AccessToken;
import cn.byteforge.openqq.http.entity.RecommendShard;
import cn.byteforge.openqq.model.Certificate;
import cn.byteforge.openqq.ws.BotContext;
import cn.byteforge.openqq.ws.QQConnection;
import cn.byteforge.openqq.ws.WebSocketAPI;
import cn.byteforge.openqq.ws.entity.Intent;
import cn.byteforge.openqq.ws.event.EventListener;
import cn.byteforge.openqq.ws.event.type.group.GroupAtMessageEvent;
import cn.byteforge.openqq.ws.handler.ChainHandler;

import java.nio.file.Files;
import java.nio.file.Paths;

public class TestMain {

    private static BotContext context;

    public static void main(String[] args) throws Exception {
        String appId = new String(Files.readAllBytes(Paths.get("../secrets/appId.txt")));
        String clientSecret = new String(Files.readAllBytes(Paths.get("../secrets/clientSecret.txt")));
        AccessToken token = OpenAPI.getAppAccessToken(appId, clientSecret);
        Certificate certificate = new Certificate(appId, clientSecret, token);
        context = BotContext.create(certificate);
        RecommendShard shard = OpenAPI.getRecommendShardWssUrls(certificate);
        String wssUrl = shard.getUrl();

        Intent intent = Intent.register().withAll().done();
        ChainHandler chainHandler = ChainHandler.defaultChainGroup(wssUrl, null,
                new EventListener<GroupAtMessageEvent>() {
                    @Override
                    public void onEvent(GroupAtMessageEvent event) {
                        System.out.println(event);
                    }

                    @Override
                    public Intent eventIntent() {
                        return Intent.register().withCustom(1 << 25).done();
                    }
                });

        QQConnection.connect(wssUrl, chainHandler, context,
                uuid -> WebSocketAPI.newStandaloneSession(intent, uuid, null, context),
                uuid -> {
                    // do sth
                });
    }

}
