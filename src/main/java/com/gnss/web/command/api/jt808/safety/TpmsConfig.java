package com.gnss.web.command.api.jt808.safety;

import com.gnss.core.constants.jt808.ParamSettingEnum;
import com.gnss.core.proto.TerminalProto;
import com.gnss.core.utils.CommonUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCountUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Size;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>Description: 胎压监测系统TPMS参数</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2019/7/3
 */
@ApiModel("胎压监测系统TPMS参数")
@Getter
@Setter
public class TpmsConfig {

    @ApiModelProperty(value = "轮胎规格型号,默认900R20", position = 1)
    @Size(max = 12, message = "轮胎规格型号长度最大12个字符")
    private String p1 = "900R20";

    @ApiModelProperty(value = "胎压单位,默认3", position = 2)
    @Range(min = 0, max = 3, message = "胎压单位范围为0-60")
    private int p2 = 3;

    @ApiModelProperty(value = "正常胎压值,默认140", position = 3)
    private int p3 = 140;

    @ApiModelProperty(value = "胎压不平衡门限(单位%),默认20", position = 4)
    @Range(min = 0, max = 100, message = "胎压不平衡门限范围为0-100")
    private int p4 = 20;

    @ApiModelProperty(value = "慢漏气门限(单位%),默认5", position = 5)
    @Range(min = 0, max = 100, message = "慢漏气门限范围为0-100")
    private int p5 = 5;

    @ApiModelProperty(value = "低压阈值,默认110", position = 6)
    private int p6 = 110;

    @ApiModelProperty(value = "高压阈值,默认189", position = 7)
    private int p7 = 189;

    @ApiModelProperty(value = "高温阈值,默认80", position = 8)
    private int p8 = 80;

    @ApiModelProperty(value = "电压阈值(单位%),默认10", position = 9)
    @Range(min = 0, max = 100, message = "电压阈值范围为0-100")
    private int p9 = 10;

    @ApiModelProperty(value = "定时上报时间间隔,默认60", position = 10)
    @Range(min = 0, max = 3600, message = "定时上报时间间隔范围为0-3600")
    private int p10 = 60;

    public byte[] buildMessageBody(TerminalProto terminalInfo) throws Exception {
        byte[] msgBodyArr = null;
        int paramLen = 36;
        ByteBuf msgBody = Unpooled.buffer(42);
        //参数总数、参数ID、参数长度
        msgBody.writeByte(1)
                .writeInt((int) ParamSettingEnum.PARAM_F366.getValue())
                .writeByte(paramLen);
        CommonUtil.writeGbkString(msgBody, p1, 12);
        msgBody.writeShort(p2)
                .writeShort(p3)
                .writeShort(p4)
                .writeShort(p5)
                .writeShort(p6)
                .writeShort(p7)
                .writeShort(p8)
                .writeShort(p9)
                .writeShort(p10)
                .writeBytes(new byte[6]);
        msgBodyArr = new byte[msgBody.readableBytes()];
        msgBody.readBytes(msgBodyArr);
        ReferenceCountUtil.release(msgBody);
        return msgBodyArr;
    }

    @Override
    public String toString() {
        Map<String, Object> items = new LinkedHashMap<>();
        items.put("轮胎规格型号", p1);
        items.put("胎压单位", p2);
        items.put("正常胎压值", p3);
        items.put("胎压不平衡门限", p4);
        items.put("慢漏气门限", p5);
        items.put("低压阈值", p6);
        items.put("高压阈值", p7);
        items.put("高温阈值", p8);
        items.put("电压阈值", p9);
        items.put("定时上报时间间隔", p10);
        return items.toString();
    }
}