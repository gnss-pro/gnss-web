package com.gnss.web.terminal.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>Description: 压力测试信息</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2019/10/14
 */
@ApiModel("压力测试信息")
@Data
public class LoadTestInfoDTO {

    @ApiModelProperty(value = "终端总数", position = 1)
    private long terminalTotalCount;

    @ApiModelProperty(value = "终端在线数", position = 2)
    private long terminalOnlineCount;

    @ApiModelProperty(value = "位置数量", position = 3)
    private long locationCount;
}