package com.gnss.web.command.api;

import com.gnss.core.constants.CommandSendResultEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>Description: 指令结果信息</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2020/4/19
 */
@ApiModel("指令结果信息")
@Data
public class BaseCommandResultDTO {

    @ApiModelProperty(value = "指令发送结果", position = 1)
    private CommandSendResultEnum sendResult;

    @ApiModelProperty(value = "结果描述", position = 2)
    private String resultDesc;

    @ApiModelProperty(value = "属性", position = 3)
    private Map<String, Object> attributes;
}