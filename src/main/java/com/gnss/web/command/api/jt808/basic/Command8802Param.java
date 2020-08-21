package com.gnss.web.command.api.jt808.basic;

import com.gnss.core.annotations.DownCommand;
import com.gnss.core.constants.jt808.Jt808MediaEventEnum;
import com.gnss.core.constants.jt808.Jt808MediaTypeEnum;
import com.gnss.core.proto.TerminalProto;
import com.gnss.core.service.IDownCommandMessage;
import com.gnss.core.utils.TimeUtil;
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
 * <p>Description: JT808 0x8802指令参数</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2019-9-14
 */
@ApiModel("0x8802存储多媒体数据检索")
@Data
@DownCommand(messageId = 0x8802, respMessageId = 0x0802, desc = "存储多媒体数据检索")
public class Command8802Param implements IDownCommandMessage {

    @ApiModelProperty(value = "多媒体类型", required = true, position = 1)
    @NotNull(message = "多媒体类型不能为空")
    private Jt808MediaTypeEnum mediaType;

    @ApiModelProperty(value = "通道ID,默认0表示所有通道", position = 2)
    @Range(min = 0, max = 127, message = "通道ID范围0-127")
    private int channelId;

    @ApiModelProperty(value = "事件项编码", required = true, position = 3)
    @NotNull(message = "事件项编码不能为空")
    private Jt808MediaEventEnum eventItemCode;

    @ApiModelProperty(value = "开始时间", position = 4)
    private Long startTime;

    @ApiModelProperty(value = "结束时间", position = 5)
    private Long endTime;

    @Override
    public byte[] buildMessageBody(TerminalProto terminalInfo) throws Exception {
        byte[] startTimeArr = startTime == null ? new byte[6] : TimeUtil.timestampToBcdTime(startTime);
        byte[] endTimeArr = endTime == null ? new byte[6] : TimeUtil.timestampToBcdTime(endTime);

        ByteBuf msgBody = Unpooled.buffer(15);
        msgBody.writeByte(mediaType.getValue())
                .writeByte(channelId)
                .writeByte(eventItemCode.getValue())
                .writeBytes(startTimeArr)
                .writeBytes(endTimeArr);
        byte[] msgBodyArr = msgBody.array();
        ReferenceCountUtil.release(msgBody);
        return msgBodyArr;
    }

    @Override
    public String toString() {
        Map<String, Object> msgBodyItems = new LinkedHashMap<>();
        msgBodyItems.put("多媒体类型", String.format("%s:%s", mediaType.getValue(), mediaType.getDesc()));
        msgBodyItems.put("通道ID", channelId);
        msgBodyItems.put("事件项编码", String.format("%s:%s", eventItemCode.getValue(), eventItemCode.getDesc()));
        msgBodyItems.put("开始时间", startTime == null ? "0" : TimeUtil.formatTime(startTime));
        msgBodyItems.put("结束时间", endTime == null ? "0" : TimeUtil.formatTime(endTime));
        return msgBodyItems.toString();
    }
}