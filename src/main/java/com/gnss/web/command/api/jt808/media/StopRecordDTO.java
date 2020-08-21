package com.gnss.web.command.api.jt808.media;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <p>Description: 停止录像</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2020/3/31
 */
@ApiModel("停止录像")
@Data
public class StopRecordDTO {

    @ApiModelProperty(value = "录像ID", required = true, position = 1)
    @NotNull(message = "录像ID不能为空")
    private String recordId;

    @ApiModelProperty(value = "终端ID", position = 2)
    private String terminalId;
}