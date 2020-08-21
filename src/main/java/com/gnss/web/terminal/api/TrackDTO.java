package com.gnss.web.terminal.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>Description: 位置信息</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2019/10/14
 */
@ApiModel("位置信息")
@Data
public class TrackDTO extends LocationDTO {

    @ApiModelProperty(value = "车牌号", position = 100)
    private String vehicleNum;

    @ApiModelProperty(value = "车牌颜色", position = 101)
    private String plateColor;

    @ApiModelProperty(value = "终端手机号", position = 102)
    private String simNum;
}