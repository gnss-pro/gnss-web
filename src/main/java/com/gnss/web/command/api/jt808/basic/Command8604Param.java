package com.gnss.web.command.api.jt808.basic;

import com.gnss.core.annotations.DownCommand;
import com.gnss.core.constants.CommonConstant;
import com.gnss.core.constants.ProtocolEnum;
import com.gnss.core.exception.ApplicationException;
import com.gnss.core.proto.TerminalProto;
import com.gnss.core.service.IDownCommandMessage;
import com.gnss.core.utils.NumberUtil;
import com.gnss.web.utils.CommonIdGenerator;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCountUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Description: JT808 0x8604指令参数</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2020/4/26
 */
@ApiModel("0x8604设置多边形区域")
@Getter
@Setter
@DownCommand(messageId = 0x8604, respMessageId = 0x0001, desc = "设置多边形区域")
public class Command8604Param implements IDownCommandMessage {

    @ApiModelProperty(value = "区域ID")
    private Integer regionId;

    @ApiModelProperty(value = "区域属性位", position = 1)
    private List<Integer> regionPropBits;

    @ApiModelProperty(value = "区域属性", hidden = true)
    private int regionProp;

    @ApiModelProperty(value = "起始时间(yy,MM,dd,HH,mm,ss)", position = 2)
    private List<String> startTimeItems;

    @ApiModelProperty(value = "结束时间(yy,MM,dd,HH,mm,ss)", position = 3)
    private List<String> endTimeItems;

    @ApiModelProperty(value = "最高速度(km/h)", position = 4)
    private Integer maxSpeed;

    @ApiModelProperty(value = "超速持续时间(秒)", position = 5)
    private Integer overspeedDuration;

    @ApiModelProperty(value = "区域总顶点数", hidden = true)
    private int pointCount;

    @ApiModelProperty(value = "顶点项", required = true, position = 6)
    @NotEmpty(message = "顶点项不能为空")
    @Valid
    private List<PolygonPoint> points;

    @ApiModelProperty(value = "夜间最高速度(km/h),JT808-2019新增", position = 7)
    private Integer maxSpeedNight;

    @ApiModelProperty(value = "名称长度", hidden = true)
    private int regionNameLen;

    @ApiModelProperty(value = "区域名称,JT808-2019新增", position = 8)
    private String regionName;

    @ApiModelProperty(value = "终端信息", hidden = true)
    private TerminalProto terminalInfo;

    @Override
    public byte[] buildMessageBody(TerminalProto terminalInfo) throws Exception {
        //校验参数
        if (regionPropBits == null) {
            regionPropBits = new ArrayList<>();
        }
        if (regionPropBits.contains(0)) {
            if (startTimeItems == null || startTimeItems.size() != 6) {
                throw new ApplicationException("起始时间为6个字节");
            }
            if (endTimeItems == null || endTimeItems.size() != 6) {
                throw new ApplicationException("结束时间为6个字节");
            }
        }
        if (regionPropBits.contains(1)) {
            if (maxSpeed == null) {
                throw new ApplicationException("最高速度不能为空");
            }
            if (overspeedDuration == null) {
                throw new ApplicationException("超速持续时间不能为空");
            }
            if (terminalInfo.getProtocolType() == ProtocolEnum.JT808_2019 && maxSpeedNight == null) {
                throw new ApplicationException("夜间最高速度不能为空");
            }
        }

        this.terminalInfo = terminalInfo;
        //设置区域ID
        if (regionId == null) {
            regionId = CommonIdGenerator.generateInt();
        }
        //第一个顶点项
        PolygonPoint firstPoint = points.get(0);
        //南纬
        if (firstPoint.getLat() < 0) {
            regionPropBits.add(6);
        }
        //西经
        if (firstPoint.getLng() < 0) {
            regionPropBits.add(7);
        }
        //区域属性位列表转换为整型
        regionProp = NumberUtil.bitsToInt(regionPropBits, 16);
        byte[] msgBodyArr = null;
        ByteBuf msgBody = Unpooled.buffer();
        try {
            msgBody.writeInt(regionId)
                    .writeShort(regionProp);
            //启用起始时间和结束时间规则
            if (regionPropBits.contains(0)) {
                startTimeItems.forEach(item -> msgBody.writeByte(Byte.parseByte(item, 16)));
                endTimeItems.forEach(item -> msgBody.writeByte(Byte.parseByte(item, 16)));
            }
            //启用最高速度、超速持续时间和夜间最高速度规则
            if (regionPropBits.contains(1)) {
                msgBody.writeShort(maxSpeed);
                msgBody.writeByte(overspeedDuration);
            }

            //写入顶点项
            pointCount = points.size();
            msgBody.writeShort(pointCount);
            points.forEach(polygonPoint -> {
                msgBody.writeInt((int) (Math.abs(polygonPoint.getLat()) * 1000000))
                        .writeInt((int) (Math.abs(polygonPoint.getLng()) * 1000000));
            });

            //JT808-2019增加夜间最高速度和区域名称
            if (terminalInfo.getProtocolType() == ProtocolEnum.JT808_2019) {
                if (regionPropBits.contains(1)) {
                    msgBody.writeShort(maxSpeedNight);
                }
                byte[] regionNameBytes = regionName.getBytes(CommonConstant.DEFAULT_CHARSET_NAME);
                regionNameLen = regionNameBytes.length;
                msgBody.writeShort(regionNameLen);
                msgBody.writeBytes(regionNameBytes);
            }

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

    @Override
    public String toString() {
        Map<String, Object> msgBodyItems = new LinkedHashMap<>();
        msgBodyItems.put("区域ID", regionId);
        msgBodyItems.put("区域属性", NumberUtil.formatIntNum(regionProp));
        if (regionPropBits.contains(0)) {
            msgBodyItems.put("起始时间", startTimeItems);
            msgBodyItems.put("结束时间", endTimeItems);
        }
        if (regionPropBits.contains(1)) {
            msgBodyItems.put("最高速度", maxSpeed);
            msgBodyItems.put("超速持续时间", overspeedDuration);
        }
        msgBodyItems.put("区域总顶点数", pointCount);
        msgBodyItems.put("顶点项", points.toString());
        if (terminalInfo.getProtocolType() == ProtocolEnum.JT808_2019) {
            if (regionPropBits.contains(1)) {
                msgBodyItems.put("夜间最高速度", maxSpeedNight);
            }
            msgBodyItems.put("名称长度", regionNameLen);
            msgBodyItems.put("区域名称", regionName);
        }
        return msgBodyItems.toString();
    }
}