package com.gnss.web.command.api.jt808.basic;

import com.gnss.core.annotations.DownCommand;
import com.gnss.core.proto.TerminalProto;
import com.gnss.core.service.IDownCommandMessage;
import com.gnss.web.common.constant.AudioSamplingRateEnum;
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
 * <p>Description: JT808 0x8804指令参数</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2019-9-14
 */
@ApiModel("0x8804录音开始命令")
@Data
@DownCommand(messageId = 0x8804, respMessageId = 0x0001, desc = "录音开始命令")
public class Command8804Param implements IDownCommandMessage {

    @ApiModelProperty(value = "录音命令(0:停止录音,1:开始录音),默认1", position = 1)
    @Range(min = 0, max = 1, message = "录音命令为0和1")
    private int commandType = 1;

    @ApiModelProperty(value = "录音时间(秒),0表示一直录音", position = 2)
    private int duration;

    @ApiModelProperty(value = "保存标志(0:实时上传,1:保存),默认0", position = 3)
    @Range(min = 0, max = 1, message = "保存标志为0或1")
    private int saveFlag;

    @ApiModelProperty(value = "音频采样率(0:8K,1:11K,2:23K,3:32K,其他保留),默认0", required = true, position = 4)
    @NotNull(message = "音频采样率不能为空")
    private AudioSamplingRateEnum audioRate = AudioSamplingRateEnum.AUDIO_8K;

    @Override
    public byte[] buildMessageBody(TerminalProto terminalInfo) throws Exception {
        ByteBuf msgBody = Unpooled.buffer(5);
        msgBody.writeByte(commandType)
                .writeShort(duration)
                .writeByte(saveFlag)
                .writeByte(audioRate.getValue());
        byte[] msgBodyArr = msgBody.array();
        ReferenceCountUtil.release(msgBody);
        return msgBodyArr;
    }

    @Override
    public String toString() {
        Map<String, Object> msgBodyItems = new LinkedHashMap<>();
        msgBodyItems.put("录音命令", commandType == 0 ? "0:停止录音" : "1:开始录音");
        msgBodyItems.put("录音时间", duration);
        msgBodyItems.put("保存标志", saveFlag == 0 ? "0:实时上传" : "1:保存");
        msgBodyItems.put("音频采样率", String.format("%d:%s", audioRate.getValue(), audioRate.getDesc()));
        return msgBodyItems.toString();
    }
}