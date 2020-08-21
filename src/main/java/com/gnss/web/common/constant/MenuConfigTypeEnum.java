package com.gnss.web.common.constant;

import com.gnss.core.model.BaseEnum;
import lombok.Getter;

/**
 * <p>Description: 信息点播菜单设置类型</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2019/2/3
 */
@Getter
public enum MenuConfigTypeEnum implements BaseEnum<String> {

    /**
     * 删除终端全部信息项
     */
    TYPE_0(0, "0", "删除终端全部信息项"),

    /**
     * 更新菜单
     */
    TYPE_1(1, "1", "更新菜单"),

    /**
     * 追加菜单
     */
    TYPE_2(2, "2", "追加菜单"),

    /**
     * 修改菜单
     */
    TYPE_3(3, "3", "修改菜单"),

    UNKNOWN(-1, "-1", "未知类型");

    private final int value;
    private final String code;
    private final String desc;

    MenuConfigTypeEnum(int value, String code, String desc) {
        this.value = value;
        this.code = code;
        this.desc = desc;
    }

    public static MenuConfigTypeEnum fromValue(int value) {
        for (MenuConfigTypeEnum menuConfigTypeEnum : values()) {
            if (value == menuConfigTypeEnum.getValue()) {
                return menuConfigTypeEnum;
            }
        }
        return UNKNOWN;
    }
}