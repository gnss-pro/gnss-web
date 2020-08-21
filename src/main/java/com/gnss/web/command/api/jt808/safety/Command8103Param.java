package com.gnss.web.command.api.jt808.safety;

import com.gnss.core.annotations.DownCommand;
import com.gnss.core.constants.safety.ActiveSafetyEnum;
import com.gnss.core.proto.TerminalProto;
import com.gnss.core.service.IDownCommandMessage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * <p>Description: JT808 0x8103指令参数</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2018-9-14
 */
@ApiModel("0x8103设置终端参数")
@Data
@Slf4j
@DownCommand(messageId = 0x8103, respMessageId = 0x0001, desc = "设置终端参数")
public class Command8103Param implements IDownCommandMessage {

    @ApiModelProperty(value = "主动安全类型", required = true, position = 1)
    @NotNull(message = "主动安全类型不能为空")
    private ActiveSafetyEnum activeSafetyType;

    @ApiModelProperty(value = "高级驾驶辅助系统ADAS参数", position = 2)
    @Valid
    private AdasConfig adasConfig;

    @ApiModelProperty(value = "驾驶员状态监测系统DSM参数", position = 2)
    @Valid
    private DsmConfig dsmConfig;

    @ApiModelProperty(value = "胎压监测系统TPMS参数", position = 3)
    @Valid
    private TpmsConfig tpmsConfig;

    @ApiModelProperty(value = "盲区监测系统BSD参数", position = 4)
    @Valid
    private BsdConfig bsdConfig;

    @Override
    public byte[] buildMessageBody(TerminalProto terminalInfo) throws Exception {
        byte[] msgBodyArr = null;
        if (activeSafetyType == ActiveSafetyEnum.ADAS) {
            msgBodyArr = adasConfig.buildMessageBody(terminalInfo);
        } else if (activeSafetyType == ActiveSafetyEnum.DSM) {
            msgBodyArr = dsmConfig.buildMessageBody(terminalInfo);
        } else if (activeSafetyType == ActiveSafetyEnum.TPMS) {
            msgBodyArr = tpmsConfig.buildMessageBody(terminalInfo);
        } else if (activeSafetyType == ActiveSafetyEnum.BSD) {
            msgBodyArr = bsdConfig.buildMessageBody(terminalInfo);
        }
        return msgBodyArr;
    }
}