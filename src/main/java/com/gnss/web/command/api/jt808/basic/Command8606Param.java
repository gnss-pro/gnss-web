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
import org.apache.commons.lang3.StringUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Description: JT808 0x8606指令参数</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2020/4/26
 */
@ApiModel("0x8606设置路线")
@Getter
@Setter
@DownCommand(messageId = 0x8606, respMessageId = 0x0001, desc = "设置路线")
public class Command8606Param implements IDownCommandMessage {

    @ApiModelProperty(value = "路线ID")
    private Integer driveLineId;

    @ApiModelProperty(value = "路线属性位", position = 1)
    private List<Integer> linePropBits;

    @ApiModelProperty(value = "路线属性", hidden = true)
    private int lineProp;

    @ApiModelProperty(value = "起始时间(yy,MM,dd,HH,mm,ss)", position = 2)
    private List<String> startTimeItems;

    @ApiModelProperty(value = "结束时间(yy,MM,dd,HH,mm,ss)", position = 3)
    private List<String> endTimeItems;

    @ApiModelProperty(value = "路线总拐点数", hidden = true)
    private int pointCount;

    @ApiModelProperty(value = "拐点项", required = true, position = 4)
    @NotEmpty(message = "拐点项不能为空")
    @Valid
    private List<DriveLinePoint> points;

    @ApiModelProperty(value = "名称长度", hidden = true)
    private int driveLineNameLen;

    @ApiModelProperty(value = "路线名称", position = 5)
    private String driveLineName;

    @ApiModelProperty(value = "终端信息", hidden = true)
    private TerminalProto terminalInfo;

    @Override
    public byte[] buildMessageBody(TerminalProto terminalInfo) throws Exception {
        if (linePropBits == null) {
            linePropBits = Collections.emptyList();
        }
        //校验参数
        if (linePropBits.contains(0)) {
            if (startTimeItems == null || startTimeItems.size() != 6) {
                throw new ApplicationException("起始时间为6个字节");
            }
            if (endTimeItems == null || endTimeItems.size() != 6) {
                throw new ApplicationException("结束时间为6个字节");
            }
        }
        if (terminalInfo.getProtocolType() == ProtocolEnum.JT808_2019 && StringUtils.isBlank(driveLineName)) {
            throw new ApplicationException("路线名称不能为空");
        }

        this.terminalInfo = terminalInfo;
        //设置路线ID
        if (driveLineId == null) {
            driveLineId = CommonIdGenerator.generateInt();
        }
        //路线属性位列表转换为整型
        lineProp = NumberUtil.bitsToInt(linePropBits, 16);

        ByteBuf msgBody = Unpooled.buffer();
        byte[] msgBodyArr;
        try {
            msgBody.writeInt(driveLineId)
                    .writeShort(lineProp);
            if (linePropBits.contains(0)) {
                startTimeItems.forEach(item -> msgBody.writeByte(Byte.parseByte(item, 16)));
                endTimeItems.forEach(item -> msgBody.writeByte(Byte.parseByte(item, 16)));
            }
            pointCount = points.size();
            msgBody.writeShort(pointCount);
            for (int i = 0; i < pointCount; i++) {
                DriveLinePoint driveLinePoint = points.get(i);
                //生成拐点ID
                int orderId = i + 1;
                if (driveLinePoint.getPointId() == null) {
                    driveLinePoint.setPointId(orderId);
                }
                //生成路段ID
                if (driveLinePoint.getRoadSectionId() == null) {
                    driveLinePoint.setRoadSectionId(orderId);
                }
                byte[] pointData = driveLinePoint.buildMessageBody(terminalInfo);
                msgBody.writeBytes(pointData);
            }
            //JT808-2019增加路线名称
            if (terminalInfo.getProtocolType() == ProtocolEnum.JT808_2019) {
                byte[] nameArr = driveLineName.getBytes(CommonConstant.DEFAULT_CHARSET_NAME);
                msgBody.writeShort(nameArr.length)
                        .writeBytes(nameArr);
            }
            msgBodyArr = new byte[msgBody.readableBytes()];
            msgBody.readBytes(msgBodyArr);
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
        msgBodyItems.put("路线ID", driveLineId);
        msgBodyItems.put("路线属性", NumberUtil.formatIntNum(lineProp));
        if (linePropBits.contains(0)) {
            msgBodyItems.put("起始时间", startTimeItems);
            msgBodyItems.put("结束时间", endTimeItems);
        }
        msgBodyItems.put("路线总拐点数", pointCount);
        msgBodyItems.put("拐点项", points.toString());
        if (terminalInfo.getProtocolType() == ProtocolEnum.JT808_2019) {
            msgBodyItems.put("名称长度", driveLineNameLen);
            msgBodyItems.put("路线名称", driveLineName);
        }
        return msgBodyItems.toString();
    }
}