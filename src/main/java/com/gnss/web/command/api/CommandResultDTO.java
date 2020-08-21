package com.gnss.web.command.api;

import com.gnss.core.constants.CommandSendResultEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>Description: 指令发送结果</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2020/5/8
 */
@ApiModel("指令发送结果")
@Data
public class CommandResultDTO {

    @ApiModelProperty(value = "终端ID", position = 1)
    private String terminalId;

    @ApiModelProperty(value = "终端手机号", position = 2)
    private String simCode;

    @ApiModelProperty(value = "终端号码", position = 3)
    private String terminalNum;

    @ApiModelProperty(value = "车牌号码", position = 4)
    private String vehicleNum;

    @ApiModelProperty(value = "指令发送结果", position = 5)
    private CommandSendResultEnum sendResult;

    @ApiModelProperty(value = "结果描述", position = 6)
    private String resultDesc;

    @ApiModelProperty(value = "下行指令ID", position = 7)
    private String downCommandId;

    @ApiModelProperty(value = "下行指令描述", position = 8)
    private String downCommandDesc;

    @ApiModelProperty(value = "响应指令ID", position = 9)
    private String respCommandId;

    @ApiModelProperty(value = "响应内容", position = 10)
    private String responseParams;
}