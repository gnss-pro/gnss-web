package com.gnss.web.terminal.service;

import org.springframework.stereotype.Service;

/**
 * <p>Description: 地理编码服务</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2019-04-01
 */
@Service
public class GeocoderService {

    /**
     * 获取百度地图位置描述
     *
     * @param lat 纬度
     * @param lng 经度
     * @return
     */
    public String getBaiduAddress(double lat, double lng) {
        return "请实现位置描述";
    }

}