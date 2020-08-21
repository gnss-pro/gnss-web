package com.gnss.web.constants;

import com.fasterxml.jackson.annotation.JsonValue;
import com.gnss.core.exception.ApplicationException;
import com.gnss.core.model.BaseEnum;
import lombok.Getter;

/**
 * <p>Description: 文件存储位置</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2018-03-29
 */
public enum FileStorageEnum implements BaseEnum<String> {

    /**
     * 终端设备
     */
    TERMINAL("0", "终端设备"),

    /**
     * 存储服务器
     */
    STORAGE_SERVER("1", "存储服务器"),

    /**
     * 下载服务器
     */
    DOWNLOAD_SERVER("2", "下载服务器");

    private final String code;

    @Getter
    private final String desc;

    FileStorageEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    @JsonValue
    public String getCode() {
        return code;
    }

    public static FileStorageEnum fromValue(int value) {
        return fromValue(String.valueOf(value));
    }

    public static FileStorageEnum fromValue(String code) {
        for (FileStorageEnum storageEnum : values()) {
            if (storageEnum.getCode().equals(code)) {
                return storageEnum;
            }
        }
        throw new ApplicationException("找不到枚举" + code);
    }
}