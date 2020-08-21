package com.gnss.web.command.api;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Description: 通用应答</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author menghuan
 * @version 1.0.1
 * @date 2019/2/14
 */
@Data
public class CommonReplyParam {

    /**
     * 应答消息ID
     */
    private Integer replyMessageId;

    /**
     * 字符串应答消息ID
     */
    private String strReplyMessageId;

    /**
     * 结果
     */
    private String result;

    /**
     * 附加信息
     */
    private Map<String, Object> extraInfo = new HashMap<>();

    public Map<String, Object> setExtraInfo(String key, Object value) {
        extraInfo.put(key, value);
        return extraInfo;
    }
}
