package com.gnss.web.info.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>Description: 终端详细信息</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2019/10/14
 */
@ApiModel("终端详细信息")
@Data
public class TerminalDetailDTO {

    @ApiModelProperty(value = "终端ID", position = 1)
    private String id;

    @ApiModelProperty(value = "终端手机号", position = 2)
    private String phoneNum;

    @ApiModelProperty(value = "终端号码", position = 3)
    private String terminalNum;

    @ApiModelProperty(value = "车牌号码", position = 4)
    private String vehicleNum;

    @ApiModelProperty(value = "车牌颜色", position = 5)
    private String vehiclePlateColor;
}