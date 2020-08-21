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
 * <p>Description: 高级驾驶辅助系统ADAS参数</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2019/7/3
 */
@ApiModel("高级驾驶辅助系统ADAS参数")
@Getter
@Setter
public class AdasConfig {

    @ApiModelProperty(value = "报警判断速度阈值(单位km/h),默认30", position = 1)
    @Range(min = 0, max = 60, message = "报警判断速度阈值范围为0-60")
    private int p1 = 30;

    @ApiModelProperty(value = "报警提示音量,默认6", position = 2)
    @Range(min = 0, max = 8, message = "报警提示音量范围为0-8")
    private int p2 = 6;

    @ApiModelProperty(value = "主动拍照策略,默认0", position = 3)
    @Range(min = 0, max = 3, message = "主动拍照策略范围为0-3")
    private int p3;

    @ApiModelProperty(value = "主动定时拍照时间间隔(单位秒),默认60", position = 4)
    @Range(min = 0, max = 3600, message = "主动定时拍照时间间隔范围为0-3600")
    private int p4 = 60;

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

    @ApiModelProperty(value = "报警使能位,默认0x00010FFF", position = 10)
    private List<Integer> p10Bits;

    @ApiModelProperty(value = "报警使能", hidden = true)
    private long p10 = 0x00010FFF;

    @ApiModelProperty(value = "事件使能位,默认0x00000003", position = 11)
    private List<Integer> p11Bits;

    @ApiModelProperty(value = "事件使能", hidden = true)
    private long p11 = 0x00000003;

    @ApiModelProperty(value = "障碍物报警距离阈值(单位100ms),默认30", position = 12)
    @Range(min = 10, max = 50, message = "障碍物报警距离阈值范围为10-50")
    private int p12 = 30;

    @ApiModelProperty(value = "障碍物报警分级速度阈值(单位km/h),默认50", position = 13)
    @Range(min = 0, max = 220, message = "障碍物报警分级速度阈值范围为0-220")
    private int p13 = 50;

    @ApiModelProperty(value = "障碍物报警前后视频录制时间(单位100ms),默认5", position = 14)
    @Range(min = 0, max = 60, message = "障碍物报警前后视频录制时间范围为0-60")
    private int p14 = 5;

    @ApiModelProperty(value = "障碍物报警拍照张数,默认3", position = 15)
    @Range(min = 0, max = 10, message = "障碍物报警拍照张数范围为0-10")
    private int p15 = 3;

    @ApiModelProperty(value = "障碍物报警拍照间隔,默认2", position = 16)
    @Range(min = 1, max = 10, message = "障碍物报警拍照间隔范围为1-10")
    private int p16 = 2;

    @ApiModelProperty(value = "频繁变道报警判断时间段,默认60", position = 17)
    @Range(min = 30, max = 120, message = "频繁变道报警判断时间段范围为30-120")
    private int p17 = 60;

    @ApiModelProperty(value = "频繁变道报警判断次数,默认5", position = 18)
    @Range(min = 3, max = 10, message = "频繁变道报警判断次数范围为3-10")
    private int p18 = 5;

    @ApiModelProperty(value = "频繁变道报警分级速度阈值,默认50", position = 19)
    @Range(min = 0, max = 220, message = "频繁变道报警分级速度阈值范围为0-220")
    private int p19 = 50;

    @ApiModelProperty(value = "频繁变道报警前后视频录制时间,默认5", position = 20)
    @Range(min = 0, max = 60, message = "频繁变道报警前后视频录制时间范围为0-60")
    private int p20 = 5;

    @ApiModelProperty(value = "频繁变道报警拍照张数,默认3", position = 21)
    @Range(min = 0, max = 10, message = "频繁变道报警拍照张数范围为0-10")
    private int p21 = 3;

    @ApiModelProperty(value = "频繁变道报警拍照间隔(单位100ms),默认2", position = 22)
    @Range(min = 1, max = 10, message = "频繁变道报警拍照间隔范围为1-10")
    private int p22 = 2;

    @ApiModelProperty(value = "车道偏离报警分级速度阈值(单位km/h),默认50", position = 23)
    @Range(min = 0, max = 220, message = "车道偏离报警分级速度阈值范围为0-220")
    private int p23 = 50;

    @ApiModelProperty(value = "车道偏离报警前后视频录制时间(单位秒),默认5", position = 24)
    @Range(min = 0, max = 60, message = "车道偏离报警前后视频录制时间范围为0-60")
    private int p24 = 5;

    @ApiModelProperty(value = "车道偏离报警拍照张数,默认3", position = 25)
    @Range(min = 0, max = 10, message = "车道偏离报警拍照张数范围为0-10")
    private int p25 = 3;

    @ApiModelProperty(value = "车道偏离报警拍照间隔(单位100ms),默认2", position = 26)
    @Range(min = 1, max = 10, message = "车道偏离报警拍照张数范围为1-10")
    private int p26 = 2;

    @ApiModelProperty(value = "前向碰撞报警时间阈值(单位100ms),默认27", position = 27)
    @Range(min = 10, max = 50, message = "车道偏离报警分级速度阈值范围为10-50")
    private int p27 = 27;

    @ApiModelProperty(value = "前向碰撞报警分级速度阈值(单位km/h),默认50", position = 28)
    @Range(min = 0, max = 220, message = "前向碰撞报警分级速度阈值范围为0-220")
    private int p28 = 50;

    @ApiModelProperty(value = "前向碰撞报警前后视频录制时间(单位秒),默认5", position = 29)
    @Range(min = 0, max = 60, message = "前向碰撞报警前后视频录制时间范围为0-60")
    private int p29 = 5;

    @ApiModelProperty(value = "前向碰撞报警拍照张数,默认3", position = 30)
    @Range(min = 0, max = 10, message = "前向碰撞报警拍照张数范围为0-10")
    private int p30 = 3;

    @ApiModelProperty(value = "前向碰撞报警拍照间隔(单位100ms),默认2", position = 31)
    @Range(min = 1, max = 10, message = "前向碰撞报警拍照间隔范围为1-10")
    private int p31 = 2;

    @ApiModelProperty(value = "行人碰撞报警时间阈值(单位100ms),默认30", position = 32)
    @Range(min = 10, max = 50, message = "行人碰撞报警时间阈值范围为10-50")
    private int p32 = 30;

    @ApiModelProperty(value = "行人碰撞报警使能速度阈值(单位km/h),默认50", position = 33)
    @Range(min = 0, max = 220, message = "行人碰撞报警使能速度阈值范围为0-220")
    private int p33 = 50;

    @ApiModelProperty(value = "行人碰撞报警前后视频录制时间(单位秒),默认5", position = 34)
    @Range(min = 0, max = 60, message = "行人碰撞报警前后视频录制时间范围为0-60")
    private int p34 = 5;

    @ApiModelProperty(value = "行人碰撞报警拍照张数,默认3", position = 35)
    @Range(min = 0, max = 10, message = "行人碰撞报警拍照张数范围为0-10")
    private int p35 = 3;

    @ApiModelProperty(value = "行人碰撞报警拍照间隔(单位100ms),默认2", position = 36)
    @Range(min = 1, max = 10, message = "行人碰撞报警拍照间隔范围为1-10")
    private int p36 = 2;

    @ApiModelProperty(value = "车距监控报警距离阈值(单位100ms),默认10", position = 37)
    @Range(min = 10, max = 50, message = "车距监控报警距离阈值范围为10-50")
    private int p37 = 10;

    @ApiModelProperty(value = "车距监控报警分级速度阈值(单位km/h),默认50", position = 38)
    @Range(min = 0, max = 220, message = "车距监控报警分级速度阈值范围为0-220")
    private int p38 = 50;

    @ApiModelProperty(value = "车距过近报警前后视频录制时间(单位秒),默认5", position = 39)
    @Range(min = 0, max = 60, message = "车距过近报警前后视频录制时间范围为0-60")
    private int p39 = 5;

    @ApiModelProperty(value = "车距过近报警拍照张数,默认3", position = 40)
    @Range(min = 0, max = 10, message = "车距过近报警拍照张数范围为0-10")
    private int p40 = 3;

    @ApiModelProperty(value = "车距过近报警拍照间隔(单位100ms),默认2", position = 41)
    @Range(min = 1, max = 10, message = "车距过近报警拍照间隔范围为1-10")
    private int p41 = 2;

    @ApiModelProperty(value = "道路标志识别拍照张数,默认3", position = 42)
    @Range(min = 0, max = 10, message = "道路标志识别拍照张数范围为0-10")
    private int p42 = 3;

    @ApiModelProperty(value = "道路标志识别拍照间隔(单位100ms),默认2", position = 43)
    @Range(min = 1, max = 10, message = "道路标志识别拍照间隔范围为1-10")
    private int p43 = 2;

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
        int paramLen = 56;
        ByteBuf msgBody = Unpooled.buffer(62);
        //参数总数、参数ID、参数长度
        msgBody.writeByte(1)
                .writeInt((int) ParamSettingEnum.PARAM_F364.getValue())
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
                .writeByte(0)
                .writeByte(p12)
                .writeByte(p13)
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
                .writeByte(p35)
                .writeByte(p36)
                .writeByte(p37)
                .writeByte(p38)
                .writeByte(p39)
                .writeByte(p40)
                .writeByte(p41)
                .writeByte(p42)
                .writeByte(p43)
                .writeBytes(new byte[4]);
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
        items.put("障碍物报警距离阈值", p12);
        items.put("障碍物报警分级速度阈值", p13);
        items.put("障碍物报警前后视频录制时间", p14);
        items.put("障碍物报警拍照张数", p15);
        items.put("障碍物报警拍照间隔", p16);
        items.put("频繁变道报警判断时间段", p17);
        items.put("频繁变道报警判断次数", p18);
        items.put("频繁变道报警分级速度阈值", p19);
        items.put("频繁变道报警前后视频录制时间", p20);
        items.put("频繁变道报警拍照张数", p21);
        items.put("频繁变道报警拍照间隔", p22);
        items.put("车道偏离报警分级速度阈值", p23);
        items.put("车道偏离报警前后视频录制时间", p24);
        items.put("车道偏离报警拍照张数", p25);
        items.put("车道偏离报警拍照间隔", p26);
        items.put("前向碰撞报警时间阈值", p27);
        items.put("前向碰撞报警分级速度阈值", p28);
        items.put("前向碰撞报警前后视频录制时间", p29);
        items.put("前向碰撞报警拍照张数", p30);
        items.put("前向碰撞报警拍照间隔", p31);
        items.put("行人碰撞报警时间阈值", p32);
        items.put("行人碰撞报警使能速度阈值", p33);
        items.put("行人碰撞报警前后视频录制时间", p34);
        items.put("行人碰撞报警拍照张数", p35);
        items.put("行人碰撞报警拍照间隔", p36);
        items.put("车距监控报警距离阈值", p37);
        items.put("车距监控报警分级速度阈值", p38);
        items.put("车距过近报警前后视频录制时间", p39);
        items.put("车距过近报警拍照张数", p40);
        items.put("车距过近报警拍照间隔", p41);
        items.put("道路标志识别拍照张数", p42);
        items.put("道路标志识别拍照间隔", p43);
        return items.toString();
    }
}