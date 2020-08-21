package com.gnss.web.command.api.jt808.media;

import com.gnss.core.annotations.DownCommand;
import com.gnss.core.constants.jt1078.PlaybackControlTypeEnum;
import com.gnss.core.constants.jt1078.PlaybackFastTimeEnum;
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
 * <p>Description: JT808 0x9202指令参数</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2018-9-14
 */
@ApiModel("0x9202平台下发远程录像回放控制")
@Data
@DownCommand(messageId = 0x9202, respMessageId = 0x0001, desc = "平台下发远程录像回放控制")
public class Command9202Param implements IDownCommandMessage {

    @ApiModelProperty(value = "逻辑通道号", required = true, position = 1)
    @Range(min = 1, max = 127, message = "逻辑通道号范围1-127")
    private int channelId;

    @ApiModelProperty(value = "回放控制(0:开始回放,1:暂停回放,2:结束回放,3:快进回放,4:关键帧快退回放,5:拖动回放,6:关键帧播放)", required = true, position = 2)
    @NotNull(message = "回放控制不能为空")
    private PlaybackControlTypeEnum ctrlType;

    @ApiModelProperty(value = "快进或快退倍数(0:无效,1:1倍,2:2倍,3:4倍,4:8倍,5:16倍)", required = true, position = 3)
    @NotNull(message = "快进或快退倍数不能为空")
    private PlaybackFastTimeEnum fastTime;

    @ApiModelProperty(value = "拖动回放位置", position = 4)
    private Long dateTime;

    @Override
    public byte[] buildMessageBody(TerminalProto terminalInfo) throws Exception {
        byte[] dateTimeArr = dateTime == null ? new byte[6] : TimeUtil.timestampToBcdTime(dateTime);
        ByteBuf msgBody = Unpooled.buffer(9);
        msgBody.writeByte(channelId)
                .writeByte(ctrlType.getValue())
                .writeByte(fastTime.getValue())
                .writeBytes(dateTimeArr);
        byte[] msgBodyArr = msgBody.array();
        ReferenceCountUtil.release(msgBody);
        return msgBodyArr;
    }

    @Override
    public String toString() {
        Map<String, Object> msgBodyItems = new LinkedHashMap<>();
        msgBodyItems.put("逻辑通道号", channelId);
        msgBodyItems.put("回放控制", String.format("%d:%s", ctrlType.getValue(), ctrlType.getDesc()));
        msgBodyItems.put("快进或快退倍数", String.format("%d:%s", fastTime.getValue(), fastTime.getDesc()));
        msgBodyItems.put("拖动回放位置", dateTime == null ? "0" : TimeUtil.formatTime(dateTime));
        return msgBodyItems.toString();
    }
}