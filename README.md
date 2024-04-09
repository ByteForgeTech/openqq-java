# openqq-java

> 本实现暂不支持频道相关 API

openqq-java 是基于官方qq机器人协议的 java sdk 实现。本实现将在保留官方设计理念的前提下额外对onebot、satori等社区协议进行的适配实现。

## Notice

- 官方事件数据无法正确标识部分富文本内容在消息中的位置，如 `图片` 等类型会被提取成 `attachments` 字段，其他文本内容将直接在 `content` 中进行拼接
- 官方消息类型 `图文消息` 暂时被分解实现为
  - 收: `图片` + `文本消息`
  - 发: `Markdown 消息` / `ARK 消息`

## API

### 特性

- [ ] Markdown 指令

### 消息发送

- SendType
  - [x] Group
  - [ ] Private

- MessageType
  - [x] Text
  - [x] Image
  - [x] Record
  - [x] Video
  - [ ] Emotion
  - [x] ARK 消息
  - [ ] Embed 消息
  - [x] Markdown 消息

### 消息接收

- [ ] Media
- [ ] Emotion

## Usage

```xml
  <dependency>
      <groupId>cn.byteforge.openqq</groupId>
      <artifactId>openqq-for-java</artifactId>
      <version>0.2.2</version>
  </dependency>
```

```java
  public class TestMain {
  
    private static BotContext context;
  
    public static void main(String[] args) throws Exception {
      String appId = new String(Files.readAllBytes(Paths.get("secrets/appId.txt")));
      String clientSecret = new String(Files.readAllBytes(Paths.get("secrets/clientSecret.txt")));
      // register global check hook
      OpenAPI.addBeforeGetAuthResponseCheck(APIEnum.SEND_GROUP_MESSAGE, data -> {
        System.out.println("预检查数据(不许返回!)" + data);
        return false;
      });
      
      try {
        AccessToken token = OpenAPI.getAppAccessToken(appId, clientSecret);
        Certificate certificate = new Certificate(appId, clientSecret, token);
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
                    GroupAtMessageData data = event.getData();
                    Message message = new MessageBuilder()
                            .addTemplateMarkdownButton("")
                            .setPassive(data.getId())
                            .build();
                    OpenAPI.sendGroupMessage(data.getGroupId(), message, certificate);
                  }

                  @Override
                  public Intent eventIntent() {
                    return Intent.register().withCustom(1 << 25).done();
                  }
                }, new EventListener<InteractionEvent>() {
                  @Override
                  public void onEvent(InteractionEvent event) {
                    System.out.println("收到：" + event);
                  }

                  @Override
                  public Intent eventIntent() {
                    return Intent.register().withInteraction().done();
                  }
                });

        QQConnection.connect(wssUrl, chainHandler, context,
                uuid -> WebSocketAPI.newStandaloneSession(intent, uuid, null, context),
                uuid -> {
                  // do sth
                });
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        System.out.println("Connection closed, try to re-generate token and reconnect ...");
      }
    }
  
  }
```

## Contributions

<a href="https://github.com/ByteForgeTech/openqq-java/graphs/contributors">
  <img src="https://contrib.rocks/image?repo=ByteForgeTech/openqq-java" />
</a>