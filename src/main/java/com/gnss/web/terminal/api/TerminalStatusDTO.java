package com.gnss.web.terminal.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>Description: 终端状态信息</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2019/10/14
 */
@ApiModel("终端状态信息")
@Data
public class TerminalStatusDTO {

    @ApiModelProperty(value = "终端ID", position = 1)
    private Long terminalId;

    @ApiModelProperty(value = "车牌号码", position = 2)
    private String vehicleNum;

    @ApiModelProperty(value = "车牌颜色", position = 3)
    private String vehiclePlateColor;

    @ApiModelProperty(value = "终端号码", position = 4)
    private String terminalNum;

    @ApiModelProperty(value = "sim卡号", position = 5)
    private String simNum;

    @ApiModelProperty(value = "是否在线(0:离线 1:在线)", position = 100)
    private String online;

    @ApiModelProperty(value = "车辆状态(0:离线,1:行驶,2:停车,3:怠速,4:报警,5:未定位,6:在线)", position = 101)
    private String vehicleStatus;

    @ApiModelProperty(value = "位置信息", position = 102)
    private LocationDTO location;
}