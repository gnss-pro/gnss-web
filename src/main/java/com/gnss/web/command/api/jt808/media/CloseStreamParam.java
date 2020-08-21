package com.gnss.web.command.api.jt808.media;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <p>Description: 关闭终端流媒体通道参数</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2020/4/13
 */
@ApiModel("关闭终端流媒体通道参数")
@Data
public class CloseStreamParam {

    @ApiParam("流类型")
    @NotNull(message = "流类型不能为空")
    private String streamApp;

    @ApiParam("终端号")
    @NotNull(message = "终端号不能为空")
    private String terminalNum;

    @ApiParam("通道号")
    @NotNull(message = "通道号不能为空")
    private Integer channelId;
}