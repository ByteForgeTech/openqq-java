package cn.byteforge.openqq.ws.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订阅事件类型枚举
 * */
@Getter
@AllArgsConstructor
public enum IntentEnum {

    // GUILDS
    GUILD_CREATE(Intent.register().withGuilds().done().getValue()),
    GUILD_UPDATE(Intent.register().withGuilds().done().getValue()),
    GUILD_DELETE(Intent.register().withGuilds().done().getValue()),
    CHANNEL_CREATE(Intent.register().withGuilds().done().getValue()),
    CHANNEL_UPDATE(Intent.register().withGuilds().done().getValue()),
    CHANNEL_DELETE(Intent.register().withGuilds().done().getValue()),

    // GUILD_MEMBERS
    GUILD_MEMBER_ADD(Intent.register().withGuildMembers().done().getValue()),
    GUILD_MEMBER_UPDATE(Intent.register().withGuildMembers().done().getValue()),
    GUILD_MEMBER_REMOVE(Intent.register().withGuildMembers().done().getValue()),

    // GUILD_MESSAGES
    MESSAGE_CREATE(Intent.register().withGuildMessages().done().getValue()),
    MESSAGE_DELETE(Intent.register().withGuildMessages().done().getValue()),

    // GUILD_MESSAGE_REACTIONS
    MESSAGE_REACTION_ADD(Intent.register().withGuildMessageReactions().done().getValue()),
    MESSAGE_REACTION_REMOVE(Intent.register().withGuildMessageReactions().done().getValue()),

    // DIRECT_MESSAGE
    DIRECT_MESSAGE_CREATE(Intent.register().withDirectMessage().done().getValue()),
    DIRECT_MESSAGE_DELETE(Intent.register().withDirectMessage().done().getValue()),

    // UNKNOWN(1 << 25)
    C2C_MESSAGE_CREATE(Intent.register().withCustom(1 << 25).done().getValue()),
    GROUP_AT_MESSAGE_CREATE(Intent.register().withCustom(1 << 25).done().getValue()),
    GROUP_MESSAGE_CREATE(Intent.register().withCustom(1 << 25).done().getValue()),
    GROUP_ADD_ROBBOT(Intent.register().withCustom(1 << 25).done().getValue()),
    GROUP_DEL_ROBBOT(Intent.register().withCustom(1 << 25).done().getValue()),
    GROUP_MSG_REJECT(Intent.register().withCustom(1 << 25).done().getValue()),
    GROUP_MSG_RECEIVE(Intent.register().withCustom(1 << 25).done().getValue()),
    FRIEND_ADD(Intent.register().withCustom(1 << 25).done().getValue()),
    FRIEND_DEL(Intent.register().withCustom(1 << 25).done().getValue()),
    C2C_MSG_REJECT(Intent.register().withCustom(1 << 25).done().getValue()),
    C2C_MSG_RECEIVE(Intent.register().withCustom(1 << 25).done().getValue()),

    // INTERACTION
    INTERACTION_CREATE(Intent.register().withInteraction().done().getValue()),

    // MESSAGE_AUDIT
    MESSAGE_AUDIT_PASS(Intent.register().withMessageAudit().done().getValue()),
    MESSAGE_AUDIT_REJECT(Intent.register().withMessageAudit().done().getValue()),

    // FORUMS_EVENT
    FORUM_THREAD_CREATE(Intent.register().withForumsEvent().done().getValue()),
    FORUM_THREAD_UPDATE(Intent.register().withForumsEvent().done().getValue()),
    FORUM_THREAD_DELETE(Intent.register().withForumsEvent().done().getValue()),
    FORUM_POST_CREATE(Intent.register().withForumsEvent().done().getValue()),
    FORUM_POST_DELETE(Intent.register().withForumsEvent().done().getValue()),
    FORUM_REPLY_CREATE(Intent.register().withForumsEvent().done().getValue()),
    FORUM_REPLY_DELETE(Intent.register().withForumsEvent().done().getValue()),
    FORUM_PUBLISH_AUDIT_RESULT(Intent.register().withForumsEvent().done().getValue()),

    // AUDIO_ACTION,
    AUDIO_START(Intent.register().withAudioAction().done().getValue()),
    AUDIO_FINISH(Intent.register().withAudioAction().done().getValue()),
    AUDIO_ON_MIC(Intent.register().withAudioAction().done().getValue()),
    AUDIO_OFF_MIC(Intent.register().withAudioAction().done().getValue()),

    // PUBLIC_GUILD_MESSAGES,
    AT_MESSAGE_CREATE(Intent.register().withPublicGuidMessages().done().getValue()),
    PUBLIC_MESSAGE_DELETE(Intent.register().withPublicGuidMessages().done().getValue()),

    ;

    private int intent;

}
