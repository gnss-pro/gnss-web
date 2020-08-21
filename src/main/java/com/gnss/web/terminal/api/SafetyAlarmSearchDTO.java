package com.gnss.web.terminal.api;

import com.gnss.core.constants.AlarmTypeEnum;
import com.gnss.web.constants.HandleTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>Description: 主动安全报警查询条件</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2019/6/15
 */
@ApiModel("主动安全报警查询条件")
@Data
public class SafetyAlarmSearchDTO {

    @ApiModelProperty(value = "名称", position = 1)
    private String nameLike;

    @ApiModelProperty(value = "开始时间", position = 2)
    private Long startTime;

    @ApiModelProperty(value = "结束时间", position = 3)
    private Long endTime;

    @ApiModelProperty(value = "报警类型", position = 4)
    private List<AlarmTypeEnum> alarmTypeList;

    @ApiModelProperty(value = "是否处理", position = 5)
    private HandleTypeEnum isHandle;
}