package com.gnss.web.command.api.jt808.basic;

import com.gnss.core.proto.TerminalProto;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCountUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

/**
 * <p>Description: CAN参数设置</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2020/6/19
 */
@ApiModel("CAN参数设置")
@Getter
@Setter
public class CanConfig {

    @ApiModelProperty(value = "CAN总线ID", position = 1)
    @NotNull(message = "CAN总线ID不能为空")
    private Integer busId;

    @ApiModelProperty(value = "CAN通道号", position = 2)
    @Range(min = 0, max = 1, message = "CAN通道号必须为0或1")
    private int channel;

    @ApiModelProperty(value = "采集时间间隔(单位:ms)", position = 3)
    private int collectInterval;

    @ApiModelProperty(value = "帧类型", position = 4)
    @Range(min = 0, max = 1, message = "帧类型必须为0或1")
    private int frameType;

    @ApiModelProperty(value = "数据采集方式", position = 5)
    @Range(min = 0, max = 1, message = "数据采集方式必须为0或1")
    private int collectMethod;

    public byte[] buildMessageBody(TerminalProto terminalInfo) throws Exception {
        int prop = channel << 31 | frameType << 30 | collectMethod << 29 | busId;
        ByteBuf msgBody = Unpooled.buffer(8);
        msgBody.writeInt(collectInterval)
                .writeInt(prop);
        byte[] msgBodyArr = msgBody.array();
        ReferenceCountUtil.release(msgBody);
        return msgBodyArr;
    }
}
