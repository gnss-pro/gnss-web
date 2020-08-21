package com.gnss.web.common.constant;

import com.gnss.core.model.BaseEnum;
import lombok.Getter;

/**
 * <p>Description: 音频采样率</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2018-12-26
 */
@Getter
public enum AudioSamplingRateEnum implements BaseEnum<String> {
    /**
     * 8K
     */
    AUDIO_8K("8K", 0, "0"),

    /**
     * 11K
     */
    AUDIO_11K("11K", 1, "1"),

    /**
     * 23K
     */
    AUDIO_23K("23K", 2, "2"),

    /**
     * 32K
     */
    AUDIO_32K("32K", 3, "3"),

    /**
     * 其他保留
     */
    OTHER("保留", -1, "-1");

    private final String desc;

    private final int value;

    private final String code;

    AudioSamplingRateEnum(String desc, int value, String code) {
        this.desc = desc;
        this.value = value;
        this.code = code;
    }
}