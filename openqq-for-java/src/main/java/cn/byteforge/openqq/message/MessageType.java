package cn.byteforge.openqq.message;

/**
 * 消息类型
 * */
public class MessageType {

    /**
     * 文本
     * */
    public static final int TEXT = 0;

    /**
     * markdown
     * */
    public static final int MARKDOWN = 2;

    /**
     * ark(json) 消息
     * */
    public static final int ARK = 3;

    /**
     * embed(special ark)
     * */
    public static final int EMBED = 4;

    /**
     * media 富媒体
     * */
    public static final int MEDIA = 7;

}
