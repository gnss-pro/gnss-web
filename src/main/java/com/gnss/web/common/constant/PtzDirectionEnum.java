package com.gnss.web.common.constant;

import lombok.Getter;

/**
 * <p>Description: JT1078云台旋转方向</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2019/2/3
 */
@Getter
public enum PtzDirectionEnum {

    /**
     * 停止
     */
    STOP(0x00, "0", "停止"),

    /**
     * 上
     */
    UP(0x01, "1", "上"),

    /**
     * 下
     */
    DOWN(0x02, "2", "下"),

    /**
     * 左
     */
    LEFT(0x03, "3", "左"),

    /**
     * 右
     */
    SIZE_1024_768(0x04, "4", "右"),

    UNKNOWN(-1, "-1", "未知方向");

    private final int value;
    private final String hexValue;
    private final String desc;

    PtzDirectionEnum(int value, String hexValue, String desc) {
        this.value = value;
        this.hexValue = hexValue;
        this.desc = desc;
    }

    public static PtzDirectionEnum fromValue(int value) {
        for (PtzDirectionEnum ptzDirectionEnum : values()) {
            if (value == ptzDirectionEnum.getValue()) {
                return ptzDirectionEnum;
            }
        }
        return UNKNOWN;
    }
}