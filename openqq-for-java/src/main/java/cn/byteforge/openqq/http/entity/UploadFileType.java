package cn.byteforge.openqq.http.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 上传文件类型
 * */
@Getter
@AllArgsConstructor
public enum UploadFileType {

    /**
     * 图片
     * */
    IMAGE(1),

    /**
     * 视频
     * */
    VIDEO(2),

    /**
     * 语音
     * */
    RECORD(3),

    /**
     * 文件（暂未开放）
     * */
    FILE(4);

    private final int value;

}
