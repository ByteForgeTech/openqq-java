package cn.byteforge.openqq.http.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * InteractResult 操作结果枚举
 * */
@Getter
@AllArgsConstructor
public enum InteractResult {

    /**
     * 成功
     * */
    SUCCESS(0),

    /**
     * 操作失败
     * */
    FAIL(1),

    /**
     * 操作频繁
     * */
    FREQUENT(2),

    /**
     * 重复操作
     * */
    REPEAT(3),

    /**
     * 没有权限
     * */
    NO_PERMISSION(4),

    /**
     * 仅管理员操作
     * */
    ONLY_ADMIN(5);

    private final Integer code;

}
