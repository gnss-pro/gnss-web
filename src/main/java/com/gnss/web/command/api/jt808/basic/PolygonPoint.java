package com.gnss.web.command.api.jt808.basic;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>Description: 多边形区域顶点项</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2019/6/26
 */
@ApiModel("多边形区域顶点项")
@Getter
@Setter
public class PolygonPoint {

    @ApiModelProperty(value = "顶点纬度", required = true, position = 1)
    @NotNull(message = "顶点纬度不能为空")
    private Double lat;

    @ApiModelProperty(value = "顶点经度", required = true, position = 2)
    @NotNull(message = "顶点经度不能为空")
    private Double lng;

    @Override
    public String toString() {
        Map<String, Object> msgBodyItems = new LinkedHashMap<>();
        msgBodyItems.put("顶点纬度", lat);
        msgBodyItems.put("顶点经度", lng);
        return msgBodyItems.toString();
    }
}
