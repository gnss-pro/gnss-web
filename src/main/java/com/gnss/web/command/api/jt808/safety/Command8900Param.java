package com.gnss.web.command.api.jt808.safety;

import com.gnss.core.annotations.DownCommand;
import com.gnss.core.constants.safety.ActiveSafetyEnum;
import com.gnss.core.constants.TransparentTypeEnum;
import com.gnss.core.proto.TerminalProto;
import com.gnss.core.service.IDownCommandMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCountUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.LinkedHashSet;

/**
 * <p>Description: JT808 0x8900指令参数</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2018-9-14
 */
@ApiModel("0x8900查询基本信息")
@Data
@DownCommand(messageId = 0x8900, respMessageId = 0x0900, desc = "查询基本信息")
public class Command8900Param implements IDownCommandMessage {

    @ApiModelProperty(value = "透传消息类型", required = true, position = 1)
    @NotNull(message = "透传消息类型不能为空")
    private TransparentTypeEnum msgType;

    @ApiModelProperty(value = "主动安全外设列表", required = true, position = 2)
    @NotEmpty(message = "主动安全外设列表不能为空")
    private LinkedHashSet<ActiveSafetyEnum> activeSafetyTypeList;

    @Override
    public byte[] buildMessageBody(TerminalProto terminalInfo) throws Exception {
        int itemCount = activeSafetyTypeList.size();
        ByteBuf msgBody = Unpooled.buffer(2 + itemCount);
        msgBody.writeByte(msgType.getValue()).writeByte(itemCount);
        for (ActiveSafetyEnum activeSafetyType : activeSafetyTypeList) {
            msgBody.writeByte(activeSafetyType.getCode());
        }
        byte[] msgBodyArr = msgBody.array();
        ReferenceCountUtil.release(msgBody);
        return msgBodyArr;
    }
}
