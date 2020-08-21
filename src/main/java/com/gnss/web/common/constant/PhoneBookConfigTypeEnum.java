package com.gnss.web.common.constant;

import com.gnss.core.model.BaseEnum;
import lombok.Getter;

/**
 * <p>Description: 设置电话本设置类型</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2019/2/3
 */
@Getter
public enum PhoneBookConfigTypeEnum implements BaseEnum<String> {

    /**
     * 删除终端上所有存储的联系人
     */
    TYPE_0(0, "0", "删除终端所有联系人"),

    /**
     * 更新电话本
     */
    TYPE_1(1, "1", "更新电话本"),

    /**
     * 追加电话本
     */
    TYPE_2(2, "2", "追加电话本"),

    /**
     * 修改电话本
     */
    TYPE_3(3, "3", "修改电话本"),

    UNKNOWN(-1, "-1", "未知类型");

    private final int value;
    private final String code;
    private final String desc;

    PhoneBookConfigTypeEnum(int value, String code, String desc) {
        this.value = value;
        this.code = code;
        this.desc = desc;
    }

    public static PhoneBookConfigTypeEnum fromValue(int value) {
        for (PhoneBookConfigTypeEnum phoneBookConfigTypeEnum : values()) {
            if (value == phoneBookConfigTypeEnum.getValue()) {
                return phoneBookConfigTypeEnum;
            }
        }
        return UNKNOWN;
    }
}