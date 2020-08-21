package com.gnss.web.constants;


import lombok.Getter;

/**
 * <p>Description: 报警来源</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2018-12-26
 */
public enum AlarmSourceEnum {

    /**
     * 终端
     */
    TERMINAL("终端"),

    /**
     * 平台
     */
    PLATFORM("平台"),

    /**
     * 上级平台
     */
    GOV_PLATFORM("上级平台");

    @Getter
    private final String desc;

    AlarmSourceEnum(String desc) {
        this.desc = desc;
    }
}