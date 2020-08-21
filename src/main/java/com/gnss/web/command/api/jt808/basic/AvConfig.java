package com.gnss.web.command.api.jt808.basic;

import com.gnss.core.utils.NumberUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCountUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Description: 音视频参数设置</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2020/6/4
 */
@ApiModel("音视频参数设置")
@Getter
@Setter
public class AvConfig {

    @ApiModelProperty(value = "通道号")
    @Range(min = 1, max = 127, message = "通道号范围为1-127")
    private int channelId = 1;

    @ApiModelProperty(value = "实时流编码模式", position = 1)
    @NotNull(message = "实时流编码模式不能为空")
    private Integer rtCodecMode;

    @ApiModelProperty(value = "实时流分辨率", position = 2)
    @NotNull(message = "实时流分辨率不能为空")
    private Integer rtResolution;

    @ApiModelProperty(value = "实时流关键帧间隔", position = 3)
    @NotNull(message = "实时流关键帧间隔不能为空")
    private Integer rtKeyFrameInterval;

    @ApiModelProperty(value = "实时流目标帧率", position = 4)
    @NotNull(message = "实时流目标帧率不能为空")
    private Integer rtFrameRate;

    @ApiModelProperty(value = "实时流目标码率", position = 5)
    @NotNull(message = "实时流目标码率不能为空")
    private Integer rtBitRate;

    @ApiModelProperty(value = "存储流编码格式", position = 6)
    @NotNull(message = "存储流编码格式不能为空")
    private Integer storageCodecMode;

    @ApiModelProperty(value = "存储流分辨率", position = 7)
    @NotNull(message = "存储流分辨率不能为空")
    private Integer storageResolution;

    @ApiModelProperty(value = "存储流关键帧间隔", position = 8)
    @NotNull(message = "存储流关键帧间隔不能为空")
    private Integer storageKeyFrameInterval;

    @ApiModelProperty(value = "存储流目标帧率", position = 9)
    @NotNull(message = "存储流目标帧率不能为空")
    private Integer storageFrameRate;

    @ApiModelProperty(value = "存储流目标码率", position = 10)
    @NotNull(message = "存储流目标码率不能为空")
    private Integer storageBitRate;

    @ApiModelProperty(value = "OSD字幕叠加设置", position = 11)
    private List<Integer> osdBits;

    @ApiModelProperty(value = "是否启用音频输出(0:不启用,1:启用)", position = 12)
    @Range(min = 0, max = 1, message = "是否启用音频输出为0或1")
    private int enableAudio;

    @ApiModelProperty(value = "OSD字幕叠加设置转换后的值", hidden = true)
    private int osd;

    @ApiModelProperty(value = "是否单独设置音视频通道", hidden = true)
    private boolean singleSetting;

    public byte[] buildMessageBody(boolean singleSetting) throws Exception {
        this.singleSetting = singleSetting;
        if (osdBits == null) {
            osdBits = Collections.EMPTY_LIST;
        }
        //OSD每个bit值转换成整型
        osd = NumberUtil.bitsToInt(osdBits, 16);

        ByteBuf msgBody = Unpooled.buffer(21);
        //单独设置通道需要写入通道号
        if (singleSetting) {
            msgBody.writeByte(channelId);
        }
        msgBody.writeByte(rtCodecMode)
                .writeByte(rtResolution)
                .writeShort(rtKeyFrameInterval)
                .writeByte(rtFrameRate)
                .writeInt(rtBitRate)
                .writeByte(storageCodecMode)
                .writeByte(storageResolution)
                .writeShort(storageKeyFrameInterval)
                .writeByte(storageFrameRate)
                .writeInt(storageBitRate)
                .writeShort(osd);
        if (!singleSetting) {
            msgBody.writeByte(enableAudio);
        }
        byte[] msgBodyArr = msgBody.array();
        ReferenceCountUtil.release(msgBody);
        return msgBodyArr;
    }

    @Override
    public String toString() {
        Map<String, Object> items = new LinkedHashMap<>();
        if (singleSetting) {
            items.put("通道号", channelId);
        }
        items.put("实时流编码模式", rtCodecMode);
        items.put("实时流分辨率", rtResolution);
        items.put("实时流关键帧间隔", rtKeyFrameInterval);
        items.put("实时流目标帧率", rtFrameRate);
        items.put("实时流目标码率", rtBitRate);
        items.put("存储流编码格式", storageCodecMode);
        items.put("存储流分辨率", storageResolution);
        items.put("存储流关键帧间隔", storageKeyFrameInterval);
        items.put("存储流目标帧率", storageFrameRate);
        items.put("存储流目标码率", storageBitRate);
        items.put("OSD字幕叠加设置", NumberUtil.formatIntNum(osd));
        if (!singleSetting) {
            items.put("是否启用音频输出", enableAudio == 0 ? "0:不启用" : "1:启用");
        }
        return items.toString();
    }
}