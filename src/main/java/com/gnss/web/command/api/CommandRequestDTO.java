package com.gnss.web.command.api;

import com.gnss.core.constants.CommandRequestTypeEnum;
import com.gnss.core.service.IDownCommandMessage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * <p>Description: 指令请求信息</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2019-8-18
 */
@ApiModel("指令请求信息")
@Data
public class CommandRequestDTO<T extends IDownCommandMessage> {

    @ApiModelProperty(value = "终端ID", required = true, position = 1)
    @NotNull(message = "终端ID不能为空")
    private Long terminalId;

    @ApiModelProperty(value = "请求方式(同步/异步)", position = 3)
    private CommandRequestTypeEnum requestType = CommandRequestTypeEnum.SYNC;

    @ApiModelProperty(value = "超时时间(毫秒,请求方式为同步时有效)", position = 4)
    private Integer responseTimeout;

    @ApiModelProperty(value = "指令内容实体", position = 5)
    @Valid
    private T paramsEntity;

    public Class getParamsEntityClass() {
        return paramsEntity.getClass();
    }

}