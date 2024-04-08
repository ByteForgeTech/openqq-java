import cn.byteforge.openqq.exception.ErrorCheckException;
import cn.byteforge.openqq.http.OpenAPI;
import cn.byteforge.openqq.http.entity.AccessToken;
import cn.byteforge.openqq.http.entity.InteractResult;
import cn.byteforge.openqq.http.entity.RecommendShard;
import cn.byteforge.openqq.message.Message;
import cn.byteforge.openqq.message.MessageBuilder;
import cn.byteforge.openqq.model.Certificate;
import cn.byteforge.openqq.ws.BotContext;
import cn.byteforge.openqq.ws.QQConnection;
import cn.byteforge.openqq.ws.WebSocketAPI;
import cn.byteforge.openqq.ws.entity.Intent;
import cn.byteforge.openqq.ws.entity.data.GroupAtMessageData;
import cn.byteforge.openqq.ws.entity.data.InteractionData;
import cn.byteforge.openqq.ws.event.EventListener;
import cn.byteforge.openqq.ws.event.type.group.GroupAtMessageEvent;
import cn.byteforge.openqq.ws.event.type.interact.InteractionEvent;
import cn.byteforge.openqq.ws.handler.ChainHandler;
import cn.byteforge.openqq.ws.handler.ErrorCheckHandler;
import com.google.gson.Gson;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Locale;

public class TestMain {

    private static final String rowsJson = "[\n" +
            "    {\n" +
            "      \"buttons\": [\n" +
            "        {\n" +
            "          \"id\": \"1\",\n" +
            "          \"render_data\": {\n" +
            "            \"label\": \"⬅\uFE0F上一页\",\n" +
            "            \"visited_label\": \"⬅\uFE0F上一页\"\n" +
            "          },\n" +
            "          \"action\": {\n" +
            "            \"type\": 1,\n" +
            "            \"permission\": {\n" +
            "              \"type\": 1,\n" +
            "              \"specify_role_ids\": [\n" +
            "                \"1\",\n" +
            "                \"2\",\n" +
            "                \"3\"\n" +
            "              ]\n" +
            "            },\n" +
            "            \"click_limit\": 10,\n" +
            "            \"unsupport_tips\": \"兼容文本\",\n" +
            "            \"data\": \"data\",\n" +
            "            \"at_bot_show_channel_list\": true\n" +
            "          }\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": \"2\",\n" +
            "          \"render_data\": {\n" +
            "            \"label\": \"➡\uFE0F下一页\",\n" +
            "            \"visited_label\": \"➡\uFE0F下一页\"\n" +
            "          },\n" +
            "          \"action\": {\n" +
            "            \"type\": 1,\n" +
            "            \"permission\": {\n" +
            "              \"type\": 1,\n" +
            "              \"specify_role_ids\": [\n" +
            "                \"1\",\n" +
            "                \"2\",\n" +
            "                \"3\"\n" +
            "              ]\n" +
            "            },\n" +
            "            \"click_limit\": 10,\n" +
            "            \"unsupport_tips\": \"兼容文本\",\n" +
            "            \"data\": \"data\",\n" +
            "            \"at_bot_show_channel_list\": true\n" +
            "          }\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"buttons\": [\n" +
            "        {\n" +
            "          \"id\": \"3\",\n" +
            "          \"render_data\": {\n" +
            "            \"label\": \"\uD83D\uDCC5 打卡（5）\",\n" +
            "            \"visited_label\": \"\uD83D\uDCC5 打卡（5）\"\n" +
            "          },\n" +
            "          \"action\": {\n" +
            "            \"type\": 1,\n" +
            "            \"permission\": {\n" +
            "              \"type\": 1,\n" +
            "              \"specify_role_ids\": [\n" +
            "                \"1\",\n" +
            "                \"2\",\n" +
            "                \"3\"\n" +
            "              ]\n" +
            "            },\n" +
            "            \"click_limit\": 10,\n" +
            "            \"unsupport_tips\": \"兼容文本\",\n" +
            "            \"data\": \"data\",\n" +
            "            \"at_bot_show_channel_list\": true\n" +
            "          }\n" +
            "        }\n" +
            "      ]\n" +
            "    }\n" +
            "  ]";
    private static BotContext context;

    public static void main(String[] args) throws Exception {
        runTest();
    }

    private static void runTest() throws Exception {
        String appId = new String(Files.readAllBytes(Paths.get("secrets/appId.txt")));
        String clientSecret = new String(Files.readAllBytes(Paths.get("secrets/clientSecret.txt")));
        AccessToken token = OpenAPI.getAppAccessToken(appId, clientSecret);
        Certificate certificate = new Certificate(appId, clientSecret, token);

        while (true) {
            context = BotContext.create(certificate);
            RecommendShard shard = OpenAPI.getRecommendShardWssUrls(certificate);
            String wssUrl = shard.getUrl();

            Intent intent = Intent.register()
                    .withCustom(1 << 25)
                    .withCustom(1 << 26)
                    .done();

            ChainHandler chainHandler = ChainHandler.defaultChainGroup(wssUrl, null,
                    new EventListener<GroupAtMessageEvent>() {
                        @Override
                        public void onEvent(GroupAtMessageEvent event) {
                            event.reply("1");
                            event.reply("2");
                            event.reply("3");
                            event.reply(new MessageBuilder()
                                    .addCustomMarkdownButton(rowsJson, certificate.getAppId())
                                    .build());
                        }

                        @Override
                        public Intent eventIntent() {
                            return Intent.register().withCustom(1 << 25).done();
                        }
                    }, new EventListener<InteractionEvent>() {
                        @Override
                        public void onEvent(InteractionEvent event) {
                            event.callback(InteractResult.SUCCESS);
                            // TODO
                        }

                        @Override
                        public Intent eventIntent() {
                            return Intent.register().withInteraction().done();
                        }
                    });

            try {
                QQConnection.connect(wssUrl, chainHandler, context,
                        uuid -> WebSocketAPI.newStandaloneSession(intent, uuid, null, context),
                        uuid -> {
                            // do sth
                        });
            } catch (Exception e) {
                System.out.println("重新连接，原因：" + e.getMessage());
            }
        }
    }

}
