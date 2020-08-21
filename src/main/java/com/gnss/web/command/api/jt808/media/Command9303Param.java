package com.gnss.web.command.api.jt808.media;

import com.gnss.core.annotations.DownCommand;
import com.gnss.core.proto.TerminalProto;
import com.gnss.core.service.IDownCommandMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCountUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>Description: JT808 0x9303指令参数</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2018-9-14
 */
@ApiModel("0x9303云台调整光圈控制")
@Data
@DownCommand(messageId = 0x9303, respMessageId = 0x0001, desc = "云台调整光圈控制")
public class Command9303Param implements IDownCommandMessage {

    @ApiModelProperty(value = "逻辑通道号", required = true, position = 1)
    @Range(min = 1, max = 127, message = "逻辑通道号范围1-127")
    private int channelId;

    @ApiModelProperty(value = "光圈调整方式(0:调大,1:调小)", required = true, position = 2)
    @Range(min = 0, max = 1, message = "光圈调整方式为0或1")
    private int mode;

    @Override
    public byte[] buildMessageBody(TerminalProto terminalInfo) throws Exception {
        ByteBuf msgBody = Unpooled.buffer(2);
        msgBody.writeByte(channelId)
                .writeByte(mode);
        byte[] msgBodyArr = msgBody.array();
        ReferenceCountUtil.release(msgBody);
        return msgBodyArr;
    }

    @Override
    public String toString() {
        Map<String, Object> msgBodyItems = new LinkedHashMap<>();
        msgBodyItems.put("逻辑通道号", channelId);
        msgBodyItems.put("光圈调整方式", mode == 0 ? "0:调大" : "1:调小");
        return msgBodyItems.toString();
    }
}