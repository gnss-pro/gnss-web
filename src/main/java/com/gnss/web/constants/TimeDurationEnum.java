package com.gnss.web.constants;

import lombok.Getter;

/**
 * <p>Description: 时长</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2019-1-18
 */
@Getter
public enum TimeDurationEnum {
    /**
     * 年
     */
    YEAR(31536000, "年"),

    /**
     * 个月
     */
    MONTH(2592000, "个月"),

    /**
     * 天
     */
    DAY(86400, "天"),

    /**
     * 小时
     */
    HOUR(3600, "小时"),

    /**
     * 分
     */
    MINUTE(60, "分"),

    /**
     * 秒
     */
    SECOND(1, "秒");

    private final int value;

    private final String desc;

    TimeDurationEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

}