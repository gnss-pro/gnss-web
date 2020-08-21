package com.gnss.web.command.api;

import com.gnss.core.constants.CommandSendResultEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>Description: 流媒体指令结果信息</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2019-07-24
 */
@ApiModel("流媒体指令结果信息")
@Data
public class MediaCommandResultDTO {

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

    @ApiModelProperty(value = "播放流地址", position = 7)
    private String streamUrl;
}
