package com.gnss.web.command.api.jt808.basic;

import com.gnss.core.annotations.DownCommand;
import com.gnss.core.constants.CommonConstant;
import com.gnss.core.constants.jt808.ParamSettingEnum;
import com.gnss.core.proto.TerminalProto;
import com.gnss.core.service.IDownCommandMessage;
import com.gnss.core.utils.NumberUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCountUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>Description: JT808 0x8103指令参数</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2018-9-14
 */
@ApiModel("0x8103设置终端参数")
@Data
@Slf4j
@DownCommand(messageId = 0x8103, respMessageId = 0x0001, desc = "设置终端参数")
public class Command8103Param implements IDownCommandMessage {

    @ApiModelProperty(value = "参数项", position = 1)
    private Map<String, Object> items;

    @ApiModelProperty(value = "参数项0x0032违规行驶时段范围", position = 2)
    @Size(min = 4, max = 4, message = "违规行驶时段范围为4字节")
    private List<Integer> param0032;

    @ApiModelProperty(value = "报警开关(0x0050报警屏蔽字,0x0051报警发送文本SMS开关,0x0052报警拍摄开关,0053报警拍摄存储标志,0054关键标志)", position = 3)
    private Map<String, List<Integer>> alarmSelector;

    @ApiModelProperty(value = "定时拍照控制参数", position = 4)
    @Valid
    private PhotoControlParam param0064;

    @ApiModelProperty(value = "定距拍照控制参数", position = 5)
    @Valid
    private PhotoControlParam param0065;

    @ApiModelProperty(value = "参数项0x0090GNSS定位模式", position = 6)
    private List<Integer> param0090;

    @ApiModelProperty(value = "CAN参数设置", position = 7)
    @Valid
    private CanConfig param0110;

    @ApiModelProperty(value = "音视频参数设置", position = 8)
    @Valid
    private AvConfig param0075;

    @ApiModelProperty(value = "音视频通道设置", position = 9)
    @Valid
    private List<ChannelConfig> param0076;

    @ApiModelProperty(value = "单独音视频参数设置", position = 10)
    @Valid
    private List<AvConfig> param0077;

    @ApiModelProperty(value = "特殊报警录像参数设置", position = 11)
    @Valid
    private SpecialAlarmRecordConfig param0079;

    @ApiModelProperty(value = "视频报警屏蔽设置", position = 12)
    private List<Integer> param007A;

    @ApiModelProperty(value = "图像分析报警设置", position = 13)
    @Valid
    private ImageAnalysisAlarmConfig param007B;

    @ApiModelProperty(value = "终端休眠唤醒模式设置", position = 14)
    @Valid
    private TerminalWakeUpConfig param007C;

    @Override
    public byte[] buildMessageBody(TerminalProto terminalInfo) throws Exception {
        if (items == null) {
            items = new LinkedHashMap<>();
        }
        //加入特殊处理的参数项
        putParamItems(terminalInfo);
        //过滤空字符串的参数项
        items = items.entrySet().stream().filter(entry -> StringUtils.isNotBlank(entry.getValue().toString()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        byte[] msgBodyArr = null;
        ByteBuf msgBody = Unpooled.buffer();
        //设置参数总数，等后续所有参数项写入后再重新设置
        msgBody.writeByte(0);
        int itemCount = 0;
        try {
            for (Map.Entry<String, Object> entry : items.entrySet()) {
                boolean isWritten = writeItem(msgBody, Integer.parseInt(entry.getKey(), 16), entry.getValue());
                if (isWritten) {
                    itemCount++;
                }
            }
            //重新设置参数总数
            msgBody.setByte(0, itemCount);
            int len = msgBody.readableBytes();
            msgBodyArr = new byte[len];
            msgBody.getBytes(msgBody.readerIndex(), msgBodyArr);
        } catch (Exception e) {
            throw e;
        } finally {
            ReferenceCountUtil.release(msgBody);
        }
        return msgBodyArr;
    }

    /**
     * 加入特殊处理的参数项
     *
     * @param terminalInfo
     * @throws Exception
     */
    private void putParamItems(TerminalProto terminalInfo) throws Exception {
        //封装0x0032违规行驶时段范围
        if (param0032 != null) {
            byte[] param0032Data = new byte[param0032.size()];
            for (int i = 0; i < param0032.size(); i++) {
                param0032Data[i] = param0032.get(i).byteValue();
            }
            items.put("0032", param0032Data);
        }

        //封装报警开关为8个字节的报警标志
        if (!CollectionUtils.isEmpty(alarmSelector)) {
            alarmSelector.forEach((paramId, alarmBits) -> {
                long alarmFlag = NumberUtil.bitsToLong(alarmBits, 32);
                items.put(paramId, alarmFlag);
            });
        }

        //封装0x0064定时拍照控制
        if (param0064 != null) {
            long param0064Data = param0064.buildMessageBody(terminalInfo);
            items.put("0064", param0064Data);
        }

        //封装0x0065定距拍照控制
        if (param0065 != null) {
            long param0065Data = param0065.buildMessageBody(terminalInfo);
            items.put("0065", param0065Data);
        }

        //封装0x0090GNSS定位模式
        if (!CollectionUtils.isEmpty(param0090)) {
            int gnssMode = NumberUtil.bitsToInt(param0090, 8);
            items.put("0090", gnssMode);
        }

        //封装0x0110 CAN总线ID单独采集设置
        if (param0110 != null) {
            byte[] param0110Data = param0110.buildMessageBody(terminalInfo);
            items.put("0110", param0110Data);
        }

        //封装JT1078参数ID:0x0075音视频参数设置
        if (param0075 != null) {
            byte[] param0075Data = param0075.buildMessageBody(false);
            items.put("0075", param0075Data);
        }

        //封装JT1078参数ID:0x0076音视频通道设置
        if (!CollectionUtils.isEmpty(param0076)) {
            byte[] param0076Data = buildParam0076Data(param0076);
            items.put("0076", param0076Data);
        }

        //封装JT1078参数ID:0x0077单独音视频通道设置
        if (!CollectionUtils.isEmpty(param0077)) {
            byte[] param0077Data = buildParam0077Data(param0077);
            if (param0077Data != null) {
                items.put("0077", param0077Data);
            }
        }

        //封装JT1078参数ID:0x0079特殊报警录像参数设置
        if (param0079 != null) {
            byte[] param0079Data = param0079.buildMessageBody();
            items.put("0079", param0079Data);
        }

        //封装JT1078参数ID:0x007A视频报警屏蔽设置
        if (!CollectionUtils.isEmpty(param007A)) {
            long alarmMask = NumberUtil.bitsToLong(param007A, 32);
            items.put("007A", alarmMask);
        }

        //封装JT1078参数ID:0x007B图像分析报警设置
        if (param007B != null) {
            byte[] param007bData = param007B.buildMessageBody();
            items.put("007B", param007bData);
        }

        //封装JT1078参数ID:0x007C终端休眠唤醒模式设置
        if (param007C != null) {
            byte[] param007cData = param007C.buildMessageBody();
            items.put("007C", param007cData);
        }
    }

    /**
     * 封装JT1078参数ID:0x0076音视频通道设置
     *
     * @param channelConfigList
     * @return
     */
    private byte[] buildParam0076Data(List<ChannelConfig> channelConfigList) {
        //音视频通道总数
        int avChannelTotal = 0;
        //音频通道总数
        int audioChannelTotal = 0;
        //视频通道总数
        int videoChannelTotal = 0;
        ByteBuf msgBody = Unpooled.buffer(3 + 4 * channelConfigList.size());
        msgBody.writeByte(avChannelTotal)
                .writeByte(audioChannelTotal)
                .writeByte(videoChannelTotal);
        for (ChannelConfig channelConfig : channelConfigList) {
            msgBody.writeByte(channelConfig.getPhysicalChannel())
                    .writeByte(channelConfig.getLogicalChannel())
                    .writeByte(channelConfig.getChannelType())
                    .writeByte(channelConfig.getConnectPtz());
            if (channelConfig.getChannelType() == 0) {
                avChannelTotal += 1;
            } else if (channelConfig.getChannelType() == 1) {
                audioChannelTotal += 1;
            } else {
                videoChannelTotal += 1;
            }
        }
        //重新设置总数
        msgBody.setByte(0, avChannelTotal);
        msgBody.setByte(1, audioChannelTotal);
        msgBody.setByte(2, videoChannelTotal);
        byte[] msgBodyArr = msgBody.array();
        ReferenceCountUtil.release(msgBody);
        return msgBodyArr;
    }

    /**
     * 封装JT1078参数ID:0x0077单独音视频通道设置
     *
     * @param avConfigList
     * @return
     */
    private byte[] buildParam0077Data(List<AvConfig> avConfigList) {
        byte[] msgBodyArr = null;
        //单独设置的通道数量
        int channelTotal = avConfigList.size();
        ByteBuf msgBody = Unpooled.buffer(1 + 21 * channelTotal);
        msgBody.writeByte(channelTotal);
        try {
            for (AvConfig configParam : avConfigList) {
                byte[] configDataArr = configParam.buildMessageBody(true);
                msgBody.writeBytes(configDataArr);
            }
            msgBodyArr = msgBody.array();
        } catch (Exception e) {
            log.error("构造(0x0077单独音视频通道设置)消息体异常,参数:{}", avConfigList, e);
        } finally {
            ReferenceCountUtil.release(msgBody);
        }
        return msgBodyArr;
    }

    /**
     * 设置参数项
     *
     * @param msgBody
     * @param paramId
     * @param paramValue
     * @return
     * @throws Exception
     */
    private boolean writeItem(ByteBuf msgBody, int paramId, Object paramValue) throws Exception {
        if (paramValue == null) {
            return false;
        }

        //根据参数ID获取对应的数据类型
        ParamSettingEnum paramSettingEnum = ParamSettingEnum.fromValue(paramId);
        if (paramSettingEnum == ParamSettingEnum.UNKNOWN) {
            log.error("未支持的终端参数,参数项ID:{},参数信息:{}", NumberUtil.formatIntNum(paramId), paramValue);
            return false;
        }
        Class<?> itemType = paramSettingEnum.getClazz();
        if (itemType == Integer.class) {
            msgBody.writeInt(paramId);
            msgBody.writeByte(4);
            msgBody.writeInt(Integer.parseInt(paramValue.toString()));
        } else if (itemType == Short.class) {
            msgBody.writeInt(paramId);
            msgBody.writeByte(2);
            msgBody.writeShort(Short.parseShort(paramValue.toString()));
        } else if (itemType == Byte.class) {
            msgBody.writeInt(paramId);
            msgBody.writeByte(1);
            msgBody.writeByte(Byte.parseByte(paramValue.toString()));
        } else if (itemType == String.class) {
            msgBody.writeInt(paramId);
            byte[] paramValueArr = paramValue.toString().getBytes(CommonConstant.DEFAULT_CHARSET_NAME);
            msgBody.writeByte(paramValueArr.length);
            msgBody.writeBytes(paramValueArr);
        } else if (itemType == byte[].class) {
            byte[] arr = (byte[]) paramValue;
            msgBody.writeInt(paramId);
            msgBody.writeByte(arr.length);
            msgBody.writeBytes(arr);
        } else {
            log.error("未支持的终端参数类型,参数项ID:{},参数信息:{}", NumberUtil.formatIntNum(paramId), paramValue);
            return false;
        }
        return true;
    }
}