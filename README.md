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
      <version>0.2.7</version>
  </dependency>
```

```java
  public class TestMain {
  
    private static BotContext context;
  
    public static void main(String[] args) throws Exception {
      String appId = new String(Files.readAllBytes(Paths.get("openqq-for-java/secrets/appId.txt")));
      String clientSecret = new String(Files.readAllBytes(Paths.get("openqq-for-java/secrets/clientSecret.txt")));
      OpenAPI.addBeforeGetAuthResponseCheck(APIEnum.SEND_GROUP_MESSAGE, data -> {
        System.out.println("全局预检查数据函数 ... " + data);
        return true;
      });
      BotContext context = BotContext.create(appId, clientSecret, Executors.newFixedThreadPool(20));

      Supplier<ChainHandler> handlerSupplier = () -> {
        return ChainHandler.defaultChainGroup(null,
                new EventListener<GroupAtMessageEvent>() {
                  @Override
                  public void onEvent(GroupAtMessageEvent event) {
                    if (event.getD().getContent().contains("session test")) {
                      event.reply("1");
                    }
                  }

                  @Override
                  public Intent eventIntent() {
                    return Intent.register().withCustom(1 << 25).done();
                  }
                });
      };

      QQConnection.connect(context, handlerSupplier,
              uuid -> WebSocketAPI.newStandaloneSession(Intent.register().withAll().done(), uuid, null, context),
              uuid -> {
                // TODO embed
              });
      // If you are running in main, you need to block the main thread
      System.in.read();
    }
  
  }
```

## Contributions

<a href="https://github.com/ByteForgeTech/openqq-java/graphs/contributors">
  <img src="https://contrib.rocks/image?repo=ByteForgeTech/openqq-java" />
</a>