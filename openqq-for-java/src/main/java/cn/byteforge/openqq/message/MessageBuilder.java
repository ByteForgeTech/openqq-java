package cn.byteforge.openqq.message;

import cn.byteforge.openqq.exception.ConflictMessageTypeException;
import cn.byteforge.openqq.http.entity.entry.LinkedTextEntry;
import cn.byteforge.openqq.util.Maps;
import com.google.gson.JsonArray;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class MessageBuilder {

    private final Map<String, Object> data;

    public MessageBuilder() {
        this.data = new HashMap<>();
        data.put("content", new StringBuilder());
    }

    // 文本消息

    /**
     * 添加文本内容
     * */
    public MessageBuilder addText(String text) {
        ((StringBuilder) data.get("content")).append(text);
        updateMsgType(MessageType.TEXT);
        return this;
    }

    /**
     * 使用 @ 能力
     * @ 某人｜群聊、文字子频道可用
     * @param userId 用户 openId
     * @deprecated 无法解析
     * */
    @Deprecated
    public MessageBuilder addAtText(String userId) {
        ((StringBuilder) data.get("content")).append("<@").append(userId).append('>');
        updateMsgType(MessageType.TEXT);
        return this;
    }

    // Markdown 消息

    /**
     * 从消息模版发送 Markdown 消息
     * @param templateId markdown 模版id，申请模版后获得
     * @param keyValues {key: xxx, values: xxx}，模版内变量与填充值的kv映射
     * */
    public MessageBuilder addTemplateMarkdown(String templateId, JsonArray keyValues) {
        return addTemplateMarkdown(templateId, keyValues.asList());
    }

    /**
     * 从消息模版发送 Markdown 消息
     * @param templateId markdown 模版id，申请模版后获得
     * @param keyValues {key: xxx, values: xxx}，模版内变量与填充值的kv映射
     * */
    public MessageBuilder addTemplateMarkdown(String templateId, List<?> keyValues) {
        data.put("markdown", Maps.of(
                "custom_template_id", templateId,
                "params", keyValues
        ));
        updateMsgType(MessageType.MARKDOWN);
        return this;
    }

    /**
     * 自定义 markdown 消息
     * @param content 原生 markdown 文本内容
     * */
    public MessageBuilder addCustomMarkdown(String content) {
        data.put("markdown", Maps.of(
                "content", content
        ));
        updateMsgType(MessageType.MARKDOWN);
        return this;
    }

    // ARK 消息

    /**
     * 发送 链接+文本列表模板 ark 消息
     * */
    public MessageBuilder addLinkedTextListArk(String desc, String prompt, List<LinkedTextEntry> entryList) {
        List<Map<String, Object>> obj = new LinkedList<>();
        for (LinkedTextEntry entry : entryList) {
            obj.add(Maps.of("obj_kv", Arrays.asList(
                    Maps.of("key", "desc", "value", entry.getDesc()),
                    Maps.of("key", "link", "value", entry.getLink())
            )));
        }
        return addArk(23, Arrays.asList(
                Maps.of("key", "#DESC#", "value", desc),
                Maps.of("key", "#PROMPT#", "value", prompt),
                Maps.of("key", "#LIST#", "obj", obj)
        ));
    }

    /**
     * 发送 文本+缩略图模板 消息
     * @param desc 描述
     * @param prompt 提示文本
     * @param title 标题
     * @param metaDesc 详情描述
     * @param img 图片链接
     * @param link 跳转链接
     * @param subTitle 来源
     * */
    public MessageBuilder addSmallPicArk(String desc, String prompt, String title, String metaDesc, String img, String link, String subTitle) {
        return addArk(24, Arrays.asList(
                Maps.of("key", "#DESC#", "value", desc),
                Maps.of("key", "#PROMPT#", "value", prompt),
                Maps.of("key", "#TITLE#", "value", title),
                Maps.of("key", "#METADESC#", "value", metaDesc),
                Maps.of("key", "#IMG#", "value", img),
                Maps.of("key", "#LINK#", "value", link),
                Maps.of("key", "#SUBTITLE#", "value", subTitle)
        ));
    }

    /**
     * 发送 大图模板 消息
     * @param prompt 提示消息
     * @param metaTitle 标题
     * @param metaSubTitle 子标题
     * @param metaCover 大图，尺寸为 975*540
     * @param metaAurl 跳转链接
     * */
    public MessageBuilder addBigPicArk(String prompt, String metaTitle, String metaSubTitle, String metaCover, String metaAurl) {
        return addArk(37, Arrays.asList(
                Maps.of("key", "#PROMPT#", "value", prompt),
                Maps.of("key", "#METATITLE#", "value", metaTitle),
                Maps.of("key", "#METASUBTITLE#", "value", metaSubTitle),
                Maps.of("key", "#METACOVER#", "value", metaCover),
                Maps.of("key", "#METAURL#", "value", metaAurl)
        ));
    }

    /**
     * 发送 ark 消息
     * @param templateId 模版 id，管理端可获得或内邀申请获得。以下默认可使用：
     * <br>
     * - 23 链接+文本列表模板 {@link #addLinkedTextListArk(String, String, List)}
     * <br>
     * - 24 文本+缩略图模板
     * <br>
     * - 37 大图模板
     * @param keyValues {key: xxx, value: xxx}，模版内变量与填充值的kv映射
     * @apiNote 需提前配置模板
     * */
    public MessageBuilder addArk(int templateId, JsonArray keyValues) {
        return addArk(templateId, keyValues.asList());
    }

    /**
     * 发送 ark 消息
     * @param templateId 模版 id，管理端可获得或内邀申请获得。以下默认可使用：
     * <br>
     * - 23 链接+文本列表模板 {@link #addLinkedTextListArk(String, String, List)}
     * <br>
     * - 24 文本+缩略图模板
     * <br>
     * - 37 大图模板
     * @param keyValues {key: xxx, value: xxx}，模版内变量与填充值的kv映射
     * @apiNote 需提前配置模板
     * */
    public MessageBuilder addArk(int templateId, List<?> keyValues) {
        data.put("ark", Maps.of(
                "template_id", templateId,
                "kv", keyValues
        ));
        updateMsgType(MessageType.ARK);
        return this;
    }

    // Media 富媒体

    public MessageBuilder addMedia(String fileInfo) {
        data.put("media", Maps.of(
                "file_info", fileInfo
        ));
        updateMsgType(MessageType.MEDIA);
        return this;
    }

    /**
     * 设置为被动消息
     * @param msgId 前置收到的用户发送过来的消息 ID，用于发送被动消息（回复）
     * */
    public MessageBuilder setPassive(String msgId) {
        return setPassive(msgId, null);
    }

    /**
     * 设置为被动消息
     * @param msgId 前置收到的用户发送过来的消息 ID，用于发送被动消息（回复）
     * @param msgSeq 回复消息的序号，与 msg_id 联合使用，避免相同消息id回复重复发送，不填默认是 1。相同的 msg_id + msg_seq 重复发送会失败。
     * */
    public MessageBuilder setPassive(String msgId, @Nullable Integer msgSeq) {
        data.put("msg_id", msgId);
        data.put("msg_seq", msgSeq);
        return this;
    }

//    /**
//     * 添加表情
//     * */
//    public MessageBuilder addFace() {
//
//    }

    /**
     * 构建消息
     * */
    public Message build() {
        return new Message(data);
    }

    private void updateMsgType(int msgType) {
        Object recordType = data.get("msg_type");
        if (recordType == null) {
            data.put("msg_type", msgType);
            return;
        }
        if ((int) recordType == msgType) return;
        throw new ConflictMessageTypeException(msgType, (int) recordType);
    }

}
