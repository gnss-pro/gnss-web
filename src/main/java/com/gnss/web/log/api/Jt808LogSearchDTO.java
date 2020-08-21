package com.gnss.web.log.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>Description: JT808日志查询条件</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2019/10/14
 */
@ApiModel("JT808日志查询条件")
@Data
public class Jt808LogSearchDTO {

    @ApiModelProperty(value = "终端手机号", position = 1)
    private String phoneNum;

    @ApiModelProperty(value = "开始时间", position = 2)
    private Long startTime;

    @ApiModelProperty(value = "结束时间", position = 3)
    private Long endTime;

    @ApiModelProperty(value = "上次查询最大ID", position = 4)
    private String lastId;
}