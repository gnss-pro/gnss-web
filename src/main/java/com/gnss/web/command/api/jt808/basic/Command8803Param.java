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
 * <p>Description: JT808 0x8803指令参数</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2019-9-14
 */
@ApiModel("0x8803存储多媒体数据上传命令")
@Data
@DownCommand(messageId = 0x8803, respMessageId = 0x0001, desc = "存储多媒体数据上传命令")
public class Command8803Param implements IDownCommandMessage {

    @ApiModelProperty(value = "多媒体类型", required = true, position = 1)
    @NotNull(message = "多媒体类型不能为空")
    private Jt808MediaTypeEnum mediaType;

    @ApiModelProperty(value = "通道ID,默认0表示所有通道", position = 2)
    @Range(min = 0, max = 127, message = "通道ID范围0-127")
    private int channelId;

    @ApiModelProperty(value = "事件项编码", required = true, position = 3)
    @NotNull(message = "事件项编码不能为空")
    private Jt808MediaEventEnum eventItemCode;

    @ApiModelProperty(value = "开始时间", required = true, position = 4)
    @NotNull(message = "开始时间不能为空")
    private Long startTime;

    @ApiModelProperty(value = "结束时间", required = true, position = 5)
    @NotNull(message = "结束时间不能为空")
    private Long endTime;

    @ApiModelProperty(value = "删除标志(0:保留;1:删除),默认0", position = 6)
    @Range(min = 0, max = 1, message = "删除标志为0或1")
    private int deleteFlag;

    @Override
    public byte[] buildMessageBody(TerminalProto terminalInfo) throws Exception {
        //开始时间
        byte[] startTimeArr = TimeUtil.timestampToBcdTime(startTime);
        //结束时间
        byte[] endTimeArr = TimeUtil.timestampToBcdTime(endTime);
        ByteBuf msgBody = Unpooled.buffer(16);
        msgBody.writeByte(mediaType.getValue())
                .writeByte(channelId)
                .writeByte(eventItemCode.getValue())
                .writeBytes(startTimeArr)
                .writeBytes(endTimeArr)
                .writeByte(deleteFlag);
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
        msgBodyItems.put("开始时间", TimeUtil.formatTime(startTime));
        msgBodyItems.put("结束时间", TimeUtil.formatTime(endTime));
        msgBodyItems.put("删除标志", deleteFlag == 0 ? "0:保留" : "1:删除");
        return msgBodyItems.toString();
    }
}