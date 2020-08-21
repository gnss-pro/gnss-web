package com.gnss.web.command.api.jt808.media;

import com.gnss.core.annotations.DownCommand;
import com.gnss.core.constants.jt1078.AvItemTypeEnum;
import com.gnss.core.constants.jt1078.RealAvControlTypeEnum;
import com.gnss.core.proto.TerminalProto;
import com.gnss.core.service.IDownCommandMessage;
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
 * <p>Description: JT808 0x9102指令参数</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2018-9-14
 */
@ApiModel("0x9102音视频实时传输控制")
@Data
@DownCommand(messageId = 0x9102, respMessageId = 0x0001, desc = "音视频实时传输控制")
public class Command9102Param implements IDownCommandMessage {

    @ApiModelProperty(value = "逻辑通道号", required = true, position = 1)
    @Range(min = 1, max = 127, message = "逻辑通道号范围1-127")
    private int channelId;

    @ApiModelProperty(value = "控制指令(0:关闭音视频传输,1:切换码流,2:暂停该通道所有流发送,3:恢复暂停前流的发送,4:关闭双向对讲)", required = true, position = 2)
    @NotNull(message = "控制指令不能为空")
    private RealAvControlTypeEnum ctrlCmd;

    @ApiModelProperty(value = "关闭音视频类型(0:关闭该通道有关的音视频数据,1:只关闭该通道有关的音频保留视频,2:只关闭该通道有关的视频保留音频)", required = true, position = 3)
    @NotNull(message = "关闭音视频类型不能为空")
    private AvItemTypeEnum avItemType;

    @ApiModelProperty(value = "切换码流类型(0:主码流,1:子码流)", required = true, position = 4)
    @Range(min = 0, max = 1, message = "切换码流类型为0或1")
    private int streamType;

    @Override
    public byte[] buildMessageBody(TerminalProto terminalInfo) throws Exception {
        ByteBuf msgBody = Unpooled.buffer(4);
        msgBody.writeByte(channelId)
                .writeByte(ctrlCmd.getValue())
                .writeByte(avItemType.getValue())
                .writeByte(streamType);
        byte[] msgBodyArr = msgBody.array();
        ReferenceCountUtil.release(msgBody);
        return msgBodyArr;
    }

    @Override
    public String toString() {
        Map<String, Object> msgBodyItems = new LinkedHashMap<>();
        msgBodyItems.put("逻辑通道号", channelId);
        msgBodyItems.put("控制指令", String.format("%d:%s", ctrlCmd.getValue(), ctrlCmd.getDesc()));
        msgBodyItems.put("关闭音视频类型", String.format("%d:%s", avItemType.getValue(), avItemType.getCloseStreamDesc()));
        msgBodyItems.put("切换码流类型", streamType == 0 ? "0:主码流" : "1:子码流");
        return msgBodyItems.toString();
    }
}