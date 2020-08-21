package com.gnss.web.command.api.jt808.media;

import com.gnss.core.annotations.DownCommand;
import com.gnss.core.proto.TerminalProto;
import com.gnss.core.service.IDownCommandMessage;
import com.gnss.web.common.constant.PtzDirectionEnum;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCountUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>Description: JT808 0x9301指令参数</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2018-9-14
 */
@ApiModel("0x9301云台旋转")
@Data
@DownCommand(messageId = 0x9301, respMessageId = 0x0001, desc = "云台旋转")
public class Command9301Param implements IDownCommandMessage {

    @ApiModelProperty(value = "逻辑通道号", required = true, position = 1)
    @Range(min = 1, max = 127, message = "逻辑通道号范围1-127")
    private int channelId;

    @ApiModelProperty(value = "方向(0:停止,1:上,2:下,3:左,4:右)", required = true, position = 2)
    @NotNull(message = "方向不能为空")
    private PtzDirectionEnum direction;

    @ApiModelProperty(value = "速度0-255", required = true, position = 3)
    @Range(min = 0, max = 255, message = "速度范围0-255")
    private int speed;

    @Override
    public byte[] buildMessageBody(TerminalProto terminalInfo) throws Exception {
        ByteBuf msgBody = Unpooled.buffer(3);
        msgBody.writeByte(channelId)
                .writeByte(direction.getValue())
                .writeByte(speed);
        byte[] msgBodyArr = msgBody.array();
        ReferenceCountUtil.release(msgBody);
        return msgBodyArr;
    }

    @Override
    public String toString() {
        Map<String, Object> msgBodyItems = new LinkedHashMap<>();
        msgBodyItems.put("逻辑通道号", channelId);
        msgBodyItems.put("方向", String.format("%d:%s", direction.getValue(), direction.getDesc()));
        msgBodyItems.put("速度", speed);
        return msgBodyItems.toString();
    }
}