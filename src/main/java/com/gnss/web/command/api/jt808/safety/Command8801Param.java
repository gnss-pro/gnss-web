package com.gnss.web.command.api.jt808.safety;

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

/**
 * <p>Description: JT808 0x8801指令参数</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2018-9-14
 */
@ApiModel("0x8801外设立即拍照指令")
@Data
@DownCommand(messageId = 0x8801, respMessageId = 0x0805, desc = "外设立即拍照指令")
public class Command8801Param implements IDownCommandMessage {

    @ApiModelProperty(value = "通道ID(0x00~0x25:主机使用摄像头通道进行拍照,0x64:控制ADAS拍照,0x65:控制DSM拍照)", required = true, position = 1)
    @Range(min = 1, max = 127, message = "通道ID范围1-127")
    private int channelId;

    @ApiModelProperty(value = "拍摄命令(0:停止拍摄,0xFFFF:录像,其他为拍照张数)", position = 2)
    @Range(min = 0, max = 0xFFFF, message = "拍摄命令范围0-0xFFFF")
    private int photoCommand = 1;

    @ApiModelProperty(value = "拍照间隔/录像时间(0为按最小间隔拍照或一直录像)", position = 3)
    @Range(min = 0, max = 0xFFFF, message = "拍照间隔/录像时间范围0-0xFFFF")
    private int timeInterval;

    @ApiModelProperty(value = "保存标志(0:实时上传,1:保存)", position = 4)
    @Range(min = 0, max = 1, message = "保存标志必须为0或1")
    private int saveFlag;

    @ApiModelProperty(value = "分辨率[1:320*240,2:640*480,3:800*600,4:1024*768,5:176*144(Qcif),6:352*288(Cif),7:704*288(HALF D1),8:704*576(D1)]", position = 5)
    @Range(min = 1, max = 8, message = "分辨率范围1-8")
    private int resolution = 2;

    @ApiModelProperty(value = "图像/视频质量(1-10,1质量损失最小,10压缩比最大)", position = 6)
    @Range(min = 1, max = 10, message = "图像/视频质量范围1-10")
    private int quality = 1;

    @ApiModelProperty(value = "亮度0-255", position = 7)
    @Range(min = 0, max = 255, message = "亮度范围0-255")
    private int brightness = 100;

    @ApiModelProperty(value = "对比度0-127", position = 8)
    @Range(min = 0, max = 127, message = "对比度范围0-127")
    private int contrast = 100;

    @ApiModelProperty(value = "饱和度0-127", position = 9)
    @Range(min = 0, max = 127, message = "饱和度范围0-127")
    private int saturation = 100;

    @ApiModelProperty(value = "色度0-255", position = 10)
    @Range(min = 0, max = 255, message = "色度范围0-255")
    private int chroma = 100;

    @Override
    public byte[] buildMessageBody(TerminalProto terminalInfo) throws Exception {
        ByteBuf msgBody = Unpooled.buffer(12);
        msgBody.writeByte(channelId)
                .writeShort(photoCommand)
                .writeShort(timeInterval)
                .writeByte(saveFlag)
                .writeByte(resolution)
                .writeByte(quality)
                .writeByte(brightness)
                .writeByte(contrast)
                .writeByte(saturation)
                .writeByte(chroma);
        byte[] msgBodyArr = msgBody.array();
        ReferenceCountUtil.release(msgBody);
        return msgBodyArr;
    }
}
