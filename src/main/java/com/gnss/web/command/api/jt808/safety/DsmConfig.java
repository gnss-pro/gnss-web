package com.gnss.web.command.api.jt808.safety;

import com.gnss.core.constants.jt808.ParamSettingEnum;
import com.gnss.core.proto.TerminalProto;
import com.gnss.core.utils.NumberUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCountUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import org.springframework.util.CollectionUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Description: 驾驶员状态监测系统DSM参数</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2019/7/3
 */
@ApiModel("驾驶员状态监测系统DSM参数")
@Getter
@Setter
public class DsmConfig {

    @ApiModelProperty(value = "报警判断速度阈值(单位km/h),默认30", position = 1)
    @Range(min = 0, max = 60, message = "报警判断速度阈值范围为0-60")
    private int p1 = 30;

    @ApiModelProperty(value = "报警提示音量,默认6", position = 2)
    @Range(min = 0, max = 8, message = "报警提示音量范围为0-8")
    private int p2 = 6;

    @ApiModelProperty(value = "主动拍照策略,默认0", position = 3)
    @Range(min = 0, max = 3, message = "主动拍照策略范围为0-3")
    private int p3;

    @ApiModelProperty(value = "主动定时拍照时间间隔(单位秒),默认3600", position = 4)
    @Range(min = 60, max = 60000, message = "主动定时拍照时间间隔范围为60-60000")
    private int p4 = 3600;

    @ApiModelProperty(value = "主动定距拍照距离间隔(单位米),默认200", position = 5)
    @Range(min = 0, max = 60000, message = "主动定距拍照距离间隔范围为0-60000")
    private int p5 = 200;

    @ApiModelProperty(value = "单次主动拍照张数,默认3", position = 6)
    @Range(min = 1, max = 10, message = "单次主动拍照张数范围为1-10")
    private int p6 = 3;

    @ApiModelProperty(value = "单次主动拍照时间间隔,默认2", position = 7)
    @Range(min = 1, max = 5, message = "单次主动拍照时间间隔范围为1-5")
    private int p7 = 2;

    @ApiModelProperty(value = "拍照分辨率,默认1", position = 8)
    @Range(min = 1, max = 6, message = "拍照分辨率范围为1-6")
    private int p8 = 1;

    @ApiModelProperty(value = "视频录制分辨率,默认1", position = 9)
    @Range(min = 1, max = 7, message = "视频录制分辨率范围为1-7")
    private int p9 = 1;

    @ApiModelProperty(value = "报警使能位,默认0x000001FF", position = 10)
    private List<Integer> p10Bits;

    @ApiModelProperty(value = "报警使能", hidden = true)
    private long p10 = 0x000001FF;

    @ApiModelProperty(value = "事件使能位,默认0x00000003", position = 11)
    private List<Integer> p11Bits;

    @ApiModelProperty(value = "事件使能", hidden = true)
    private long p11 = 0x00000003;

    @ApiModelProperty(value = "吸烟报警判断时间间隔(单位秒),默认180", position = 12)
    @Range(min = 0, max = 3600, message = "吸烟报警判断时间间隔范围为0-3600")
    private int p12 = 180;

    @ApiModelProperty(value = "接打电话报警判断时间间隔(单位秒),默认120", position = 13)
    @Range(min = 0, max = 3600, message = "接打电话报警判断时间间隔范围为0-3600")
    private int p13 = 120;

    @ApiModelProperty(value = "疲劳驾驶报警分级速度阈值(单位km/h),默认50", position = 14)
    @Range(min = 0, max = 220, message = "疲劳驾驶报警分级速度阈值范围为0-220")
    private int p14 = 50;

    @ApiModelProperty(value = "疲劳驾驶报警前后视频录制时间,默认5", position = 15)
    @Range(min = 0, max = 60, message = "疲劳驾驶报警前后视频录制时间范围为0-60")
    private int p15 = 5;

    @ApiModelProperty(value = "疲劳驾驶报警拍照张数,默认3", position = 16)
    @Range(min = 0, max = 10, message = "疲劳驾驶报警拍照张数范围为0-10")
    private int p16 = 3;

    @ApiModelProperty(value = "疲劳驾驶报警拍照间隔时间(单位100ms),默认2", position = 17)
    @Range(min = 30, max = 120, message = "疲劳驾驶报警拍照间隔时间范围为30-120")
    private int p17 = 60;

    @ApiModelProperty(value = "接打电话报警分级速度阈值(单位km/h),默认50", position = 18)
    @Range(min = 0, max = 220, message = "接打电话报警分级速度阈值范围为0-220")
    private int p18 = 50;

    @ApiModelProperty(value = "接打电话报警前后视频录制时间,默认5", position = 19)
    @Range(min = 0, max = 60, message = "接打电话报警前后视频录制时间范围为0-60")
    private int p19 = 5;

    @ApiModelProperty(value = "接打电话报警拍驾驶员面部特征照片张数,默认3", position = 20)
    @Range(min = 0, max = 10, message = "接打电话报警拍驾驶员面部特征照片张数范围为0-10")
    private int p20 = 3;

    @ApiModelProperty(value = "接打电话报警拍驾驶员面部特征照片间隔时间(单位100ms),默认2", position = 21)
    @Range(min = 1, max = 5, message = "接打电话报警拍驾驶员面部特征照片间隔时间范围为1-5")
    private int p21 = 2;

    @ApiModelProperty(value = "抽烟报警分级车速阈值(单位km/h),默认50", position = 22)
    @Range(min = 0, max = 220, message = "抽烟报警分级车速阈值范围为0-220")
    private int p22 = 50;

    @ApiModelProperty(value = "抽烟报警前后视频录制时间(单位秒),默认5", position = 23)
    @Range(min = 0, max = 60, message = "抽烟报警前后视频录制时间范围为0-60")
    private int p23 = 5;

    @ApiModelProperty(value = "抽烟报警拍驾驶员面部特征照片张数,默认3", position = 24)
    @Range(min = 0, max = 10, message = "抽烟报警拍驾驶员面部特征照片张数范围为0-10")
    private int p24 = 3;

    @ApiModelProperty(value = "抽烟报警拍驾驶员面部特征照片间隔时间(单位100ms),默认2", position = 25)
    @Range(min = 1, max = 5, message = "抽烟报警拍驾驶员面部特征照片间隔时间范围为1-5")
    private int p25 = 2;

    @ApiModelProperty(value = "分神驾驶报警分级车速阈值(单位km/h),默认50", position = 26)
    @Range(min = 0, max = 220, message = "分神驾驶报警分级车速阈值范围为0-220")
    private int p26 = 50;

    @ApiModelProperty(value = "分神驾驶报警前后视频录制时间(单位秒),默认5", position = 27)
    @Range(min = 0, max = 60, message = "分神驾驶报警前后视频录制时间范围为0-60")
    private int p27 = 5;

    @ApiModelProperty(value = "分神驾驶报警拍照张数,默认3", position = 28)
    @Range(min = 0, max = 10, message = "分神驾驶报警拍照张数范围为0-10")
    private int p28 = 3;

    @ApiModelProperty(value = "分神驾驶报警拍照间隔时间(单位100ms),默认2", position = 29)
    @Range(min = 1, max = 5, message = "分神驾驶报警拍照间隔时间范围为0-60")
    private int p29 = 2;

    @ApiModelProperty(value = "驾驶行为异常分级速度阈值(单位km/h),默认50", position = 30)
    @Range(min = 0, max = 220, message = "驾驶行为异常分级速度阈值范围为0-220")
    private int p30 = 50;

    @ApiModelProperty(value = "驾驶行为异常视频录制时间(单位秒),默认5", position = 31)
    @Range(min = 0, max = 60, message = "驾驶行为异常视频录制时间范围为0-60")
    private int p31 = 5;

    @ApiModelProperty(value = "驾驶行为异常抓拍照片张数,默认3", position = 32)
    @Range(min = 0, max = 10, message = "驾驶行为异常抓拍照片张数范围为0-10")
    private int p32 = 3;

    @ApiModelProperty(value = "驾驶行为异常拍照间隔(单位100ms),默认2", position = 33)
    @Range(min = 1, max = 5, message = "驾驶行为异常拍照间隔范围为1-5")
    private int p33 = 2;

    @ApiModelProperty(value = "驾驶员身份识别触发,默认1", position = 34)
    @Range(min = 0, max = 4, message = "驾驶员身份识别触发范围为0-4")
    private int p34 = 1;

    public byte[] buildMessageBody(TerminalProto terminalInfo) throws Exception {
        //转换报警使能位
        if (!CollectionUtils.isEmpty(p10Bits)) {
            p10 = NumberUtil.bitsToLong(p10Bits, 32);
        }
        //转换事件使能位
        if (!CollectionUtils.isEmpty(p11Bits)) {
            p11 = NumberUtil.bitsToLong(p11Bits, 32);
        }

        byte[] msgBodyArr = null;
        int paramLen = 49;
        ByteBuf msgBody = Unpooled.buffer(55);
        //参数总数、参数ID、参数长度
        msgBody.writeByte(1)
                .writeInt((int) ParamSettingEnum.PARAM_F365.getValue())
                .writeByte(paramLen);
        msgBody.writeByte(p1)
                .writeByte(p2)
                .writeByte(p3)
                .writeShort(p4)
                .writeShort(p5)
                .writeByte(p6)
                .writeByte(p7)
                .writeByte(p8)
                .writeByte(p9)
                .writeInt((int) p10)
                .writeInt((int) p11)
                .writeShort(p12)
                .writeShort(p13)
                .writeBytes(new byte[3])
                .writeByte(p14)
                .writeByte(p15)
                .writeByte(p16)
                .writeByte(p17)
                .writeByte(p18)
                .writeByte(p19)
                .writeByte(p20)
                .writeByte(p21)
                .writeByte(p22)
                .writeByte(p23)
                .writeByte(p24)
                .writeByte(p25)
                .writeByte(p26)
                .writeByte(p27)
                .writeByte(p28)
                .writeByte(p29)
                .writeByte(p30)
                .writeByte(p31)
                .writeByte(p32)
                .writeByte(p33)
                .writeByte(p34)
                .writeBytes(new byte[2]);
        msgBodyArr = new byte[msgBody.readableBytes()];
        msgBody.readBytes(msgBodyArr);
        ReferenceCountUtil.release(msgBody);
        return msgBodyArr;
    }

    @Override
    public String toString() {
        Map<String, Object> items = new LinkedHashMap<>();
        items.put("报警判断速度阈值", p1);
        items.put("报警提示音量", p2);
        items.put("主动拍照策略", p3);
        items.put("主动定时拍照时间间隔", p4);
        items.put("主动定距拍照距离间隔", p5);
        items.put("单次主动拍照张数", p6);
        items.put("单次主动拍照时间间隔", p7);
        items.put("拍照分辨率", p8);
        items.put("视频录制分辨率", p9);
        items.put("报警使能", String.format("0x%08X", p10));
        items.put("事件使能", String.format("0x%08X", p11));
        items.put("吸烟报警判断时间间隔", p12);
        items.put("接打电话报警判断时间间隔", p13);
        items.put("疲劳驾驶报警分级速度阈值", p14);
        items.put("疲劳驾驶报警前后视频录制时间", p15);
        items.put("疲劳驾驶报警拍照张数", p16);
        items.put("疲劳驾驶报警拍照间隔时间", p17);
        items.put("接打电话报警分级速度阈值", p18);
        items.put("接打电话报警前后视频录制时间", p19);
        items.put("接打电话报警拍驾驶员面部特征照片张数", p20);
        items.put("接打电话报警拍驾驶员面部特征照片间隔时间", p21);
        items.put("抽烟报警分级车速阈值", p22);
        items.put("抽烟报警前后视频录制时间", p23);
        items.put("抽烟报警拍驾驶员面部特征照片张数", p24);
        items.put("抽烟报警拍驾驶员面部特征照片间隔时间", p25);
        items.put("分神驾驶报警分级车速阈值", p26);
        items.put("分神驾驶报警前后视频录制时间", p27);
        items.put("分神驾驶报警拍照张数", p28);
        items.put("分神驾驶报警拍照间隔时间", p29);
        items.put("驾驶行为异常分级速度阈值", p30);
        items.put("驾驶行为异常视频录制时间", p31);
        items.put("驾驶行为异常抓拍照片张数", p32);
        items.put("驾驶行为异常拍照间隔", p33);
        items.put("驾驶员身份识别触发", p34);
        return items.toString();
    }
}