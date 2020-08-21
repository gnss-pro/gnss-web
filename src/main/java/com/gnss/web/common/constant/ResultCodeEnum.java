package com.gnss.web.common.constant;

import lombok.Getter;

/**
 * <p>Description: 响应结果编码</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2019-9-14
 */
@Getter
public enum ResultCodeEnum {
    /**
     * 未登录
     */
    NO_LOGIN(-1, "no login"),

    /**
     * 成功
     */
    SUCCESS(0, "success"),

    /**
     * 失败
     */
    FAIL(1, "fail"),

    /**
     * 异常
     */
    EXCEPTION(2, "exception"),

    /**
     * 没有权限
     */
    NO_PERMISSION(3, "no permission"),

    /**
     * 找不到终端信息
     */
    TERMINAL_NOT_FOUND(4, "terminal not found");

    private final int code;

    private final String desc;

    ResultCodeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}