import cn.byteforge.openqq.http.OpenAPI;
import cn.byteforge.openqq.http.entity.AccessToken;
import cn.byteforge.openqq.http.entity.FileInfo;
import cn.byteforge.openqq.http.entity.RecommendShard;
import cn.byteforge.openqq.http.entity.UploadFileType;
import cn.byteforge.openqq.http.entity.entry.KeyValuesEntry;
import cn.byteforge.openqq.message.MessageBuilder;
import cn.byteforge.openqq.model.Certificate;
import cn.byteforge.openqq.util.Maps;
import cn.byteforge.openqq.ws.BotContext;
import cn.byteforge.openqq.ws.QQConnection;
import cn.byteforge.openqq.ws.WebSocketAPI;
import cn.byteforge.openqq.ws.entity.Intent;
import cn.byteforge.openqq.ws.entity.Shard;
import cn.byteforge.openqq.ws.entity.data.GroupAtMessageData;
import cn.byteforge.openqq.ws.event.EventListener;
import cn.byteforge.openqq.ws.event.type.group.*;
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
        ChainHandler chainHandler = ChainHandler.defaultChainGroup(wssUrl, null,
                new EventListener<GroupAtMessageEvent>() {
            @Override
            public void onEvent(GroupAtMessageEvent event) {
                GroupAtMessageData data = event.getD();
                FileInfo fileInfo = OpenAPI.uploadGroupFile(data.getGroupId(), "https://ubot.byteforge.cn/static/bg.png", false, UploadFileType.IMAGE, context.getCertificate());
                OpenAPI.sendGroupMessage(data.getGroupId(), new MessageBuilder()
                        .setPassive(data.getId())
                        .build(), context.getCertificate());
            }

            @Override
            public Intent eventIntent() {
                return Intent.register().withCustom(1 << 25).done();
            }
        },
                new EventListener<GroupMsgRejectEvent>() {
                    @Override
                    public void onEvent(GroupMsgRejectEvent event) {
        //                        Message message = new MessageBuilder()
        ////                                .addTemplateMarkdown()
        //                                .build();
                        System.out.println("已被群组禁言：" + event);
                    }

                    @Override
                    public Intent eventIntent() {
                        return Intent.register().withAll().done();
                    }
        });

        QQConnection.connect(wssUrl, chainHandler, context,
                uuid -> WebSocketAPI.newShardSession(intent, uuid, Shard.STANDALONE, null, context),
                uuid -> {
                    // do sth
                });
    }

}
