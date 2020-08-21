package com.gnss.web.common.constant;

import com.gnss.core.model.BaseEnum;
import lombok.Getter;

/**
 * <p>Description: 事件设置类型</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2019/2/3
 */
@Getter
public enum EventConfigTypeEnum implements BaseEnum<String> {

    /**
     * 删除终端所有事件
     */
    TYPE_0(0, "0", "删除终端所有事件"),

    /**
     * 更新事件
     */
    TYPE_1(1, "1", "更新事件"),

    /**
     * 追加事件
     */
    TYPE_2(2, "2", "追加事件"),

    /**
     * 修改事件
     */
    TYPE_3(3, "3", "修改事件"),

    /**
     * 删除特定几项事件
     */
    TYPE_4(4, "4", "删除指定事件"),

    UNKNOWN(-1, "-1", "未知类型");

    private final int value;
    private final String code;
    private final String desc;

    EventConfigTypeEnum(int value, String code, String desc) {
        this.value = value;
        this.code = code;
        this.desc = desc;
    }

    public static EventConfigTypeEnum fromValue(int value) {
        for (EventConfigTypeEnum eventConfigTypeEnum : values()) {
            if (value == eventConfigTypeEnum.getValue()) {
                return eventConfigTypeEnum;
            }
        }
        return UNKNOWN;
    }
}