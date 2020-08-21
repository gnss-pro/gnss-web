package com.gnss.web.common.constant;

import lombok.Getter;

/**
 * <p>Description: 直播协议类型</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2019-9-14
 */
@Getter
public enum LiveProtocolEnum {

    /**
     * http-flv协议
     */
    HTTP_FLV("http"),

    /**
     * WebSocket-flv协议
     */
    WEBSOCKET_FLV("ws"),

    /**
     * rtmp协议
     */
    RTMP("rtmp");

    private String protocol;

    LiveProtocolEnum(String protocol) {
        this.protocol = protocol;
    }

}