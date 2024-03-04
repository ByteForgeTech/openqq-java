package cn.byteforge.openqq.ws.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 机器人接收消息订阅参数生成类
 * <a href="https://bot.q.qq.com/wiki/develop/api-v2/dev-prepare/interface-framework/event-emit.html#%E4%BA%8B%E4%BB%B6%E8%AE%A2%E9%98%85intents">事件订阅Intents</a>
 * */
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Intent {

    // 计算结果
    private final int value;

    public static Register register() {
        return new Register();
    }

    /**
     * 订阅注册类
     * */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Register {

        private int intents;

        public Register withGuilds() {
            intents = intents | (1);
            return this;
        }

        public Register withGuildMembers() {
            intents = intents | (1 << 1);
            return this;
        }

        /**
         * 消息事件，仅 *私域* 机器人能够设置此 intents。
         * */
        public Register withGuildMessages() {
            intents = intents | (1 << 9);
            return this;
        }

        public Register withGuildMessageReactions() {
            intents = intents | (1 << 10);
            return this;
        }

        public Register withDirectMessage() {
            intents = intents | (1 << 12);
            return this;
        }

        /**
         * Markdown 按钮点击回调事件监听
         * */
        public Register withInteraction() {
            intents = intents | (1 << 26);
            return this;
        }

        public Register withMessageAudit() {
            intents = intents | (1 << 27);
            return this;
        }

        /**
         * 论坛事件，仅 *私域* 机器人能够设置此 intents。
         * */
        public Register withForumsEvent() {
            intents = intents | (1 << 28);
            return this;
        }

        public Register withAudioAction() {
            intents = intents | (1 << 29);
            return this;
        }

        /**
         * 消息事件，此为公域的消息事件
         * */
        public Register withPublicGuidMessages() {
            intents = intents | (1 << 30);
            return this;
        }

        /**
         * 自定义注册事件行为
         * */
        public Register withCustom(int op) {
            intents = intents | op;
            return this;
        }

        /**
         * 注册所有支持的消息事件类型
         * */
        public Register withAll() {
            return this.withGuilds()
                    .withGuildMembers()
                    .withGuildMessages()
                    .withGuildMessageReactions()
                    .withDirectMessage()
                    .withForumsEvent()
                    .withCustom(1 << 18)
                    .withCustom(1 << 19)
                    .withCustom(1 << 25) // Group @
                    .withInteraction()
                    .withMessageAudit()
                    .withForumsEvent()
                    .withAudioAction()
                    .withPublicGuidMessages();
        }

        public Intent done() {
            return new Intent(intents);
        }

    }

}
