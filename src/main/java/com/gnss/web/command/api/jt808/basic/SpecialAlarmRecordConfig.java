package com.gnss.web.command.api.jt808.basic;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCountUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>Description: 特殊报警录像参数设置</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2020/6/4
 */
@ApiModel("特殊报警录像参数设置")
@Getter
@Setter
public class SpecialAlarmRecordConfig {

    @ApiModelProperty(value = "存储阈值(范围1-99,默认20)", position = 1)
    @Range(min = 1, max = 99, message = "存储阈值范围为1-99")
    private int storageThreshold = 20;

    @ApiModelProperty(value = "持续时间,默认5", position = 2)
    @Range(min = 1, max = 127, message = "持续时间范围为1-127")
    private int duration = 5;

    @ApiModelProperty(value = "起始时间,默认1", position = 3)
    @Range(min = 1, max = 127, message = "起始时间范围为1-127")
    private int startTime = 1;

    public byte[] buildMessageBody() throws Exception {
        ByteBuf msgBody = Unpooled.buffer(3);
        msgBody.writeByte(storageThreshold)
                .writeByte(duration)
                .writeByte(startTime);
        byte[] msgBodyArr = msgBody.array();
        ReferenceCountUtil.release(msgBody);
        return msgBodyArr;
    }

    @Override
    public String toString() {
        Map<String, Object> items = new LinkedHashMap<>();
        items.put("存储阈值", storageThreshold);
        items.put("持续时间", duration);
        items.put("起始时间", startTime);
        return items.toString();
    }
}