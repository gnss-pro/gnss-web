package com.gnss.web.constants;

import com.fasterxml.jackson.annotation.JsonValue;
import com.gnss.core.exception.ApplicationException;
import com.gnss.core.model.BaseEnum;
import lombok.Getter;

/**
 * <p>Description: 处理情况</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2018-03-29
 */
public enum HandleTypeEnum implements BaseEnum<String> {

    /**
     * 未处理
     */
    NO("N", "0"),

    /**
     * 已处理
     */
    YES("Y", "1");

    @Getter
    private final String desc;

    private final String code;

    HandleTypeEnum(String desc, String code) {
        this.desc = desc;
        this.code = code;
    }

    @Override
    @JsonValue
    public String getCode() {
        return code;
    }

    public static HandleTypeEnum fromValue(int value) {
        return fromValue(String.valueOf(value));
    }

    public static HandleTypeEnum fromValue(String code) {
        for (HandleTypeEnum handleTypeEnum : values()) {
            if (handleTypeEnum.getCode().equals(code)) {
                return handleTypeEnum;
            }
        }
        throw new ApplicationException("找不到枚举" + code);
    }
}