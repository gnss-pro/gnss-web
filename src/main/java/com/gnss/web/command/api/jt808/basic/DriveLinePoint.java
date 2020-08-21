package com.gnss.web.command.api.jt808.basic;

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
 * <p>Description: 行驶路线拐点</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2020/4/26
 */
@ApiModel("行驶路线拐点")
@Getter
@Setter
public class DriveLinePoint {

    @ApiModelProperty(value = "拐点ID", hidden = true)
    private Integer pointId;

    @ApiModelProperty(value = "路段ID", hidden = true)
    private Integer roadSectionId;

    @ApiModelProperty(value = "拐点纬度", required = true, position = 1)
    @NotNull(message = "拐点纬度不能为空")
    private Double lat;

    @ApiModelProperty(value = "拐点经度", required = true, position = 2)
    @NotNull(message = "拐点经度不能为空")
    private Double lng;

    @ApiModelProperty(value = "路段宽度", required = true, position = 3)
    @NotNull(message = "路段宽度不能为空")
    private Integer width;

    @ApiModelProperty(value = "路段属性位", position = 4)
    private List<Integer> roadSectionPropBits;

    @ApiModelProperty(value = "路段属性", hidden = true)
    private int roadSectionProp;

    @ApiModelProperty(value = "路段行驶过长阈值(秒)", position = 5)
    private Integer driveMaxThreshold;

    @ApiModelProperty(value = "路段行驶不足阈值(秒)", position = 6)
    private Integer driveMinThreshold;

    @ApiModelProperty(value = "路段最高速度(km/h)", position = 7)
    private Integer maxSpeed;

    @ApiModelProperty(value = "路段超速持续时间(秒)", position = 8)
    private Integer overspeedDuration;

    @ApiModelProperty(value = "路段夜间最高速度(km/h),JT808-2019新增", position = 9)
    private Integer maxSpeedNight;

    @ApiModelProperty(value = "终端信息", hidden = true)
    private TerminalProto terminalInfo;

    public byte[] buildMessageBody(TerminalProto terminalInfo) throws Exception {
        //校验参数
        if (roadSectionPropBits.contains(0)) {
            if (driveMaxThreshold == null) {
                throw new ApplicationException("路段行驶过长阈值不能为空");
            }
            if (driveMinThreshold == null) {
                throw new ApplicationException("路段行驶不足阈值不能为空");
            }
        }
        if (roadSectionPropBits.contains(1)) {
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
        if (roadSectionPropBits == null) {
            roadSectionPropBits = new ArrayList<>();
        }
        //南纬
        if (lat < 0) {
            roadSectionPropBits.add(2);
        }
        //西经
        if (lng < 0) {
            roadSectionPropBits.add(3);
        }
        roadSectionProp = NumberUtil.bitsToInt(roadSectionPropBits, 8);
        ByteBuf msgBody = Unpooled.buffer();
        byte[] msgBodyArr;
        msgBody.writeInt(pointId).writeInt(pointId)
                .writeInt((int) (Math.abs(lat) * 1000000))
                .writeInt((int) (Math.abs(lng) * 1000000))
                .writeByte(width)
                .writeByte(roadSectionProp);
        if (roadSectionPropBits.contains(0)) {
            msgBody.writeShort(driveMaxThreshold);
            msgBody.writeShort(driveMinThreshold);
        }
        if (roadSectionPropBits.contains(1)) {
            msgBody.writeShort(maxSpeed);
            msgBody.writeByte(overspeedDuration);
            //JT808-2019增加夜间最高速度
            if (terminalInfo.getProtocolType() == ProtocolEnum.JT808_2019) {
                msgBody.writeShort(maxSpeedNight);
            }
        }
        msgBodyArr = new byte[msgBody.readableBytes()];
        msgBody.readBytes(msgBodyArr);
        ReferenceCountUtil.release(msgBody);
        return msgBodyArr;
    }

    @Override
    public String toString() {
        Map<String, Object> msgBodyItems = new LinkedHashMap<>();
        msgBodyItems.put("拐点ID", pointId);
        msgBodyItems.put("路段ID", roadSectionId);
        msgBodyItems.put("拐点纬度", lat);
        msgBodyItems.put("拐点经度", lng);
        msgBodyItems.put("路段宽度", width);
        msgBodyItems.put("路段属性", NumberUtil.formatIntNum(roadSectionProp));
        if (roadSectionPropBits.contains(0)) {
            msgBodyItems.put("路段行驶过长阈值", driveMaxThreshold);
            msgBodyItems.put("路段行驶不足阈值", driveMinThreshold);
        }
        if (roadSectionPropBits.contains(1)) {
            msgBodyItems.put("路段最高速度", maxSpeed);
            msgBodyItems.put("路段超速持续时间", overspeedDuration);
        }
        if (terminalInfo.getProtocolType() == ProtocolEnum.JT808_2019) {
            msgBodyItems.put("路段夜间最高速度", maxSpeedNight);
        }
        return msgBodyItems.toString();
    }
}