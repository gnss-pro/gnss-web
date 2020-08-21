package com.gnss.web.command.api.jt808.basic;

import com.gnss.core.annotations.DownCommand;
import com.gnss.core.proto.TerminalProto;
import com.gnss.core.service.IDownCommandMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
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
 * <p>Description: JT808 0x8700指令参数</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2019-9-14
 */
@ApiModel("0x8700行驶记录数据采集命令")
@Getter
@Setter
@DownCommand(messageId = 0x8700, respMessageId = 0x0700, desc = "行驶记录数据采集命令")
public class Command8700Param implements IDownCommandMessage {

    @ApiModelProperty(value = "命令字", required = true, position = 1)
    @NotNull(message = "命令字不能为空")
    private Integer cmdType;

    @ApiModelProperty(value = "数据块", position = 2)
    private byte[] dataBlock;

    @Override
    public byte[] buildMessageBody(TerminalProto terminalInfo) throws Exception {
        if (dataBlock == null) {
            return new byte[]{cmdType.byteValue()};
        }
        ByteBuf msgBody = Unpooled.buffer(1 + dataBlock.length);
        msgBody.writeByte(cmdType).writeBytes(dataBlock);
        byte[] msgBodyArr = msgBody.array();
        ReferenceCountUtil.release(msgBody);
        return msgBodyArr;
    }

    @Override
    public String toString() {
        Map<String, Object> msgBodyItems = new LinkedHashMap<>();
        msgBodyItems.put("命令字", cmdType);
        if (dataBlock != null) {
            msgBodyItems.put("数据块", ByteBufUtil.hexDump(dataBlock));
        }
        return msgBodyItems.toString();
    }
}
