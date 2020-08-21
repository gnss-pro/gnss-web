package com.gnss.web.command.api.jt808.basic;

import com.gnss.core.constants.CommonConstant;
import com.gnss.core.constants.ProtocolEnum;
import com.gnss.core.exception.ApplicationException;
import com.gnss.core.proto.TerminalProto;
import com.gnss.core.utils.NumberUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCountUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Description: 圆形区域项</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2020/4/26
 */
@ApiModel("圆形区域项")
@Getter
@Setter
public class CircleRegion {

    @ApiModelProperty(value = "区域ID")
    private Integer regionId;

    @ApiModelProperty(value = "区域属性位", position = 1)
    private List<Integer> regionPropBits;

    @ApiModelProperty(value = "区域属性", hidden = true)
    private int regionProp;

    @ApiModelProperty(value = "中心点纬度", required = true, position = 2)
    @NotNull(message = "中心点纬度不能为空")
    private Double centerLat;

    @ApiModelProperty(value = "中心点经度", required = true, position = 3)
    @NotNull(message = "中心点经度不能为空")
    private Double centerLng;

    @ApiModelProperty(value = "半径", required = true, position = 4)
    @NotNull(message = "中心点经度不能为空")
    private Integer radius;

    @ApiModelProperty(value = "起始时间(yy,MM,dd,HH,mm,ss)", position = 5)
    private List<String> startTimeItems;

    @ApiModelProperty(value = "结束时间(yy,MM,dd,HH,mm,ss)", position = 6)
    private List<String> endTimeItems;

    @ApiModelProperty(value = "最高速度(km/h)", position = 7)
    private Integer maxSpeed;

    @ApiModelProperty(value = "超速持续时间(秒)", position = 8)
    private Integer overspeedDuration;

    @ApiModelProperty(value = "夜间最高速度(km/h),JT808-2019新增", position = 10)
    private Integer maxSpeedNight;

    @ApiModelProperty(value = "名称长度", hidden = true)
    private int regionNameLen;

    @ApiModelProperty(value = "区域名称,JT808-2019新增", position = 11)
    private String regionName;

    @ApiModelProperty(value = "终端信息", hidden = true)
    private TerminalProto terminalInfo;

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
        //南纬
        if (centerLat < 0) {
            regionPropBits.add(6);
        }
        //西经
        if (centerLng < 0) {
            regionPropBits.add(7);
        }
        //区域属性位列表转换为整型
        regionProp = NumberUtil.bitsToInt(regionPropBits, 16);
        byte[] regionNameBytes = regionName.getBytes(CommonConstant.DEFAULT_CHARSET_NAME);
        regionNameLen = regionNameBytes.length;
        byte[] msgBodyArr = null;
        ByteBuf msgBody = Unpooled.buffer();
        try {
            msgBody.writeInt(regionId)
                    .writeShort(regionProp)
                    .writeInt((int) (Math.abs(centerLat) * 1000000))
                    .writeInt((int) (Math.abs(centerLng) * 1000000))
                    .writeInt(radius);
            //启用起始时间和结束时间规则
            if (regionPropBits.contains(0)) {
                startTimeItems.forEach(item -> msgBody.writeByte(Byte.parseByte(item, 16)));
                endTimeItems.forEach(item -> msgBody.writeByte(Byte.parseByte(item, 16)));
            }
            //启用最高速度、超速持续时间和夜间最高速度规则
            if (regionPropBits.contains(1)) {
                msgBody.writeShort(maxSpeed);
                msgBody.writeByte(overspeedDuration);
                if (terminalInfo.getProtocolType() == ProtocolEnum.JT808_2019) {
                    msgBody.writeShort(maxSpeedNight);
                }
            }
            //JT808-2019增加区域名称
            if (terminalInfo.getProtocolType() == ProtocolEnum.JT808_2019) {
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
        msgBodyItems.put("中心点纬度", centerLat);
        msgBodyItems.put("中心点经度", centerLng);
        msgBodyItems.put("半径", radius);
        if (regionPropBits.contains(0)) {
            msgBodyItems.put("起始时间", startTimeItems);
            msgBodyItems.put("结束时间", endTimeItems);
        }
        if (regionPropBits.contains(1)) {
            msgBodyItems.put("最高速度", maxSpeed);
            msgBodyItems.put("超速持续时间", overspeedDuration);
        }
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