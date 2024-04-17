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
//        String appId = new String(Files.readAllBytes(Paths.get("../secrets/appId.txt")));
//        String clientSecret = new String(Files.readAllBytes(Paths.get("../secrets/clientSecret.txt")));
//        AccessToken token = OpenAPI.getAppAccessToken(appId, clientSecret);
//        Certificate certificate = new Certificate(appId, clientSecret, token);
//        context = BotContext.create();
//        RecommendShard shard = OpenAPI.getRecommendShardWssUrls(certificate);
//        wssUrl = shard.getUrl();
//        System.out.println(shard);
    }

    @Test
    void testStandalone() throws Exception {
    }

}
