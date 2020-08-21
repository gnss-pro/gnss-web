package com.gnss.web.command.api.jt808.safety;

import com.gnss.core.constants.jt808.ParamSettingEnum;
import com.gnss.core.proto.TerminalProto;
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
 * <p>Description: 盲区监测系统BSD参数</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2020/7/3
 */
@ApiModel("盲区监测系统BSD参数")
@Getter
@Setter
public class BsdConfig {

    @ApiModelProperty(value = "后方接近报警时间阈值,默认1", position = 1)
    @Range(min = 1, max = 10, message = "后方接近报警时间阈值范围为1-10")
    private int p1 = 1;

    @ApiModelProperty(value = "侧后方接近报警时间阈值,默认1", position = 2)
    @Range(min = 1, max = 10, message = "侧后方接近报警时间阈值范围为1-10")
    private int p2 = 1;

    public byte[] buildMessageBody(TerminalProto terminalInfo) throws Exception {
        byte[] msgBodyArr = null;
        int paramLen = 2;
        ByteBuf msgBody = Unpooled.buffer(8);
        //参数总数、参数ID、参数长度
        msgBody.writeByte(1)
                .writeInt((int) ParamSettingEnum.PARAM_F367.getValue())
                .writeByte(paramLen);
        msgBody.writeByte(p1)
                .writeByte(p2);
        msgBodyArr = new byte[msgBody.readableBytes()];
        msgBody.readBytes(msgBodyArr);
        ReferenceCountUtil.release(msgBody);
        return msgBodyArr;
    }

    @Override
    public String toString() {
        Map<String, Object> items = new LinkedHashMap<>();
        items.put("后方接近报警时间阈值", p1);
        items.put("侧后方接近报警时间阈值", p2);
        return items.toString();
    }
}