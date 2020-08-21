package com.gnss.web.common.constant;

import com.gnss.core.model.BaseEnum;
import lombok.Getter;

/**
 * <p>Description: 区域设置类型</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2019/2/3
 */
@Getter
public enum RegionConfigTypeEnum implements BaseEnum<String> {

    /**
     * 更新区域
     */
    TYPE_0(0, "0", "更新区域"),

    /**
     * 追加区域
     */
    TYPE_1(1, "1", "追加区域"),

    /**
     * 修改区域
     */
    TYPE_2(2, "2", "修改区域"),

    UNKNOWN(-1, "-1", "未知类型");

    private final int value;
    private final String code;
    private final String desc;

    RegionConfigTypeEnum(int value, String code, String desc) {
        this.value = value;
        this.code = code;
        this.desc = desc;
    }

    public static RegionConfigTypeEnum fromValue(int value) {
        for (RegionConfigTypeEnum regionConfigTypeEnum : values()) {
            if (value == regionConfigTypeEnum.getValue()) {
                return regionConfigTypeEnum;
            }
        }
        return UNKNOWN;
    }
}