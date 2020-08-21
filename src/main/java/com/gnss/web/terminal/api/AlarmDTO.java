package com.gnss.web.terminal.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>Description: 报警明细</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2020/6/8
 */
@ApiModel("报警明细查询")
@Data
public class AlarmDTO {

    @ApiModelProperty(value = "ID")
    private String alarmId;

    @ApiModelProperty(value = "车牌号", position = 1)
    private String vehicleNum;

    @ApiModelProperty(value = "车牌颜色", position = 2)
    private String plateColor;

    @ApiModelProperty(value = "终端手机号", position = 3)
    private String simNum;

    @ApiModelProperty(value = "报警来源", position = 4)
    private String alarmSource;

    @ApiModelProperty(value = "报警类型", position = 5)
    private String alarmType;

    @ApiModelProperty(value = "报警时长", position = 6)
    private String alarmDuration;

    @ApiModelProperty(value = "开始时间", position = 7)
    private String startTime;

    @ApiModelProperty(value = "结束时间", position = 8)
    private String endTime;

    @ApiModelProperty(value = "开始位置", position = 9)
    private String startLocation;

    @ApiModelProperty(value = "结束位置", position = 10)
    private String endLocation;

    @ApiModelProperty(value = "处理情况", position = 11)
    private String isHandle;

    @ApiModelProperty(value = "处理用户", position = 12)
    private String handleUserName;
}