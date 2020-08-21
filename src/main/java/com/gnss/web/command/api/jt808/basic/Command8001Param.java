package com.gnss.web.command.api.jt808.basic;

import com.gnss.core.annotations.DownCommand;
import com.gnss.core.constants.jt808.Jt808ReplyResultEnum;
import com.gnss.core.proto.TerminalProto;
import com.gnss.core.service.IDownCommandMessage;
import com.gnss.core.utils.NumberUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCountUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>Description: JT808 0x8104指令参数</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2018-9-14
 */
@ApiModel("0x8001平台通用应答")
@Getter
@Setter
@DownCommand(messageId = 0x8001, desc = "平台通用应答")
public class Command8001Param implements IDownCommandMessage {

    @ApiModelProperty(value = "应答流水号", required = true, position = 1)
    private int replyMsgFlowId;

    @ApiModelProperty(value = "应答ID", required = true, position = 2)
    private int replyMsgId;

    @ApiModelProperty(value = "结果", required = true, position = 3)
    @NotNull(message = "结果不能为空")
    private Jt808ReplyResultEnum result;

    @Override
    public byte[] buildMessageBody(TerminalProto terminalInfo) throws Exception {
        ByteBuf msgBody = Unpooled.buffer(5);
        msgBody.writeShort(replyMsgFlowId);
        msgBody.writeShort(replyMsgId);
        msgBody.writeByte(result.getValue());
        byte[] msgBodyArr = msgBody.array();
        ReferenceCountUtil.release(msgBody);
        return msgBodyArr;
    }

    @Override
    public String toString() {
        Map<String, Object> msgBodyItems = new LinkedHashMap<>();
        msgBodyItems.put("应答流水号", replyMsgFlowId);
        msgBodyItems.put("应答ID", NumberUtil.formatIntNum(replyMsgId));
        msgBodyItems.put("结果", String.format("%d:%s", result.getValue(), result.getDesc()));
        return msgBodyItems.toString();
    }
}