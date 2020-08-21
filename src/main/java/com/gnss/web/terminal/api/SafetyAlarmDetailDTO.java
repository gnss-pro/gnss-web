package com.gnss.web.terminal.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>Description: 主动安全报警明细</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2019/6/8
 */
@ApiModel("主动安全报警明细")
@Data
public class SafetyAlarmDetailDTO {

    @ApiModelProperty(value = "ID")
    private String alarmId;

    @ApiModelProperty(value = "车牌号", position = 1)
    private String vehicleNum;

    @ApiModelProperty(value = "车牌颜色", position = 2)
    private String plateColor;

    @ApiModelProperty(value = "终端手机号", position = 3)
    private String simNum;

    @ApiModelProperty(value = "主动安全类型", position = 4)
    private String activeSafetyType;

    @ApiModelProperty(value = "主动安全类型描述", position = 5)
    private String activeSafetyTypeDesc;

    @ApiModelProperty(value = "报警类型", position = 6)
    private String alarmType;

    @ApiModelProperty(value = "报警级别", position = 7)
    private Integer alarmLevel;

    @ApiModelProperty(value = "时间", position = 8)
    private String time;

    @ApiModelProperty(value = "速度", position = 9)
    private Integer speed;

    @ApiModelProperty(value = "高程", position = 10)
    private Integer altitude;

    @ApiModelProperty(value = "纬度", position = 11)
    private Double lat;

    @ApiModelProperty(value = "经度", position = 12)
    private Double lon;

    @ApiModelProperty(value = "车辆状态", position = 13)
    private Integer vehicleStatus;

    @ApiModelProperty(value = "车辆状态位JSON", position = 14)
    private String vehicleStatusBits;

    @ApiModelProperty(value = "已完成上传的附件数量", position = 15)
    private Integer completedCount;

    @ApiModelProperty(value = "附件数量", position = 16)
    private Integer attachmentCount;

    @ApiModelProperty(value = "扩展数据JSON", position = 17)
    private String extraData;
}