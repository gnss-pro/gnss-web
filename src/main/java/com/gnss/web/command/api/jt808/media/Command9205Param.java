package com.gnss.web.command.api.jt808.media;

import com.gnss.core.annotations.DownCommand;
import com.gnss.core.constants.jt1078.AvItemTypeEnum;
import com.gnss.core.constants.jt1078.MemoryTypeEnum;
import com.gnss.core.constants.jt1078.StreamTypeEnum;
import com.gnss.core.proto.TerminalProto;
import com.gnss.core.service.IDownCommandMessage;
import com.gnss.core.utils.NumberUtil;
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
import java.util.List;
import java.util.Map;

/**
 * <p>Description: JT808 0x9205指令参数</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2018-9-14
 */
@ApiModel("0x9205查询资源列表")
@Data
@DownCommand(messageId = 0x9205, respMessageId = 0x1205, desc = "查询资源列表")
public class Command9205Param implements IDownCommandMessage {

    @ApiModelProperty(value = "逻辑通道号,0表示所有通道", required = true, position = 1)
    @Range(min = 0, max = 127, message = "逻辑通道号范围0-127,0表示所有通道")
    private int channelId;

    @ApiModelProperty(value = "开始时间", position = 2)
    private Long startTime;

    @ApiModelProperty(value = "结束时间", position = 3)
    private Long endTime;

    @ApiModelProperty(value = "报警标志", position = 4)
    private List<Integer> alarmBits;

    @ApiModelProperty(value = "音视频资源类型(0:音视频,1:音频,2:视频,3:视频或音视频)", required = true, position = 5)
    @NotNull(message = "音视频资源类型不能为空")
    private AvItemTypeEnum avItemType = AvItemTypeEnum.AV;

    @ApiModelProperty(value = "码流类型(0:所有码流,1:主码流,2:子码流)", required = true, position = 6)
    @NotNull(message = "码流类型不能为空")
    private StreamTypeEnum streamType = StreamTypeEnum.ALL;

    @ApiModelProperty(value = "存储器类型(0:所有存储器,1:主存储器,2:灾备存储器)", required = true, position = 7)
    @NotNull(message = "存储器类型不能为空")
    private MemoryTypeEnum memoryType = MemoryTypeEnum.ALL;

    @ApiModelProperty(value = "报警标志", hidden = true)
    private long alarmFlag;

    @Override
    public byte[] buildMessageBody(TerminalProto terminalInfo) throws Exception {
        //开始时间
        byte[] startTimeArr = startTime == null ? new byte[6] : TimeUtil.timestampToBcdTime(startTime);
        //结束时间
        byte[] endTimeArr = endTime == null ? new byte[6] : TimeUtil.timestampToBcdTime(endTime);
        //报警标志
        alarmFlag = NumberUtil.bitsToLong(alarmBits, 64);

        ByteBuf msgBody = Unpooled.buffer(24);
        msgBody.writeByte(channelId)
                .writeBytes(startTimeArr)
                .writeBytes(endTimeArr)
                .writeLong(alarmFlag)
                .writeByte(avItemType.getValue())
                .writeByte(streamType.getValue())
                .writeByte(memoryType.getValue());
        byte[] msgBodyArr = msgBody.array();
        ReferenceCountUtil.release(msgBody);
        return msgBodyArr;
    }

    @Override
    public String toString() {
        Map<String, Object> msgBodyItems = new LinkedHashMap<>();
        msgBodyItems.put("逻辑通道号", channelId);
        msgBodyItems.put("开始时间", startTime == null ? "0" : TimeUtil.formatTime(startTime));
        msgBodyItems.put("结束时间", endTime == null ? "0" : TimeUtil.formatTime(endTime));
        msgBodyItems.put("报警标志", String.format("0x%08x", alarmFlag));
        msgBodyItems.put("音视频资源类型", String.format("%d:%s", avItemType.getValue(), avItemType.getDesc()));
        msgBodyItems.put("码流类型", String.format("%d:%s", streamType.getValue(), streamType.getDesc()));
        msgBodyItems.put("存储器类型", String.format("%d:%s", memoryType.getValue(), memoryType.getDesc()));
        return msgBodyItems.toString();
    }
}