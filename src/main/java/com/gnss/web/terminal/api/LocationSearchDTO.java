package com.gnss.web.terminal.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>Description: 位置查询条件</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2018-08-25
 */
@ApiModel("位置查询条件")
@Data
public class LocationSearchDTO {

    @ApiModelProperty(value = "终端ID", position = 1)
    private Long terminalId;

    @ApiModelProperty(value = "名称", position = 2)
    private String nameLike;

    @ApiModelProperty(value = "开始时间", position = 3)
    private Long startTime;

    @ApiModelProperty(value = "结束时间", position = 4)
    private Long endTime;
}