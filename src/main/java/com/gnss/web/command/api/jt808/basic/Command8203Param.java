package com.gnss.web.command.api.jt808.basic;

import com.gnss.core.annotations.DownCommand;
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

import javax.validation.constraints.NotEmpty;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Description: JT808 0x8203指令参数</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2018-12-14
 */
@ApiModel("0x8203人工确认报警消息")
@Getter
@Setter
@DownCommand(messageId = 0x8203, respMessageId = 0x0001, desc = "人工确认报警消息")
public class Command8203Param implements IDownCommandMessage {

    @ApiModelProperty(value = "报警消息流水号(0表示该报警类型所有消息)", position = 1)
    private int alarmMsgFlowId;

    @ApiModelProperty(value = "人工确认报警类型", required = true, position = 2)
    @NotEmpty(message = "人工确认报警类型不能为空")
    private List<Integer> flags;

    private Long alarmType;

    @Override
    public byte[] buildMessageBody(TerminalProto terminalInfo) throws Exception {
        ByteBuf msgBody = Unpooled.buffer(6);
        long alarmType = NumberUtil.bitsToLong(flags, 32);
        msgBody.writeShort(alarmMsgFlowId)
                .writeInt((int) alarmType);
        byte[] msgBodyArr = msgBody.array();
        ReferenceCountUtil.release(msgBody);
        return msgBodyArr;
    }

    @Override
    public String toString() {
        Map<String, Object> msgBodyItems = new LinkedHashMap<>();
        msgBodyItems.put("报警消息流水号", alarmMsgFlowId);
        msgBodyItems.put("人工确认报警类型", String.format("0x%08x", alarmType));
        return msgBodyItems.toString();
    }
}