package com.gnss.web.command.api.jt808.basic;

import com.gnss.core.annotations.DownCommand;
import com.gnss.core.exception.ApplicationException;
import com.gnss.core.proto.TerminalProto;
import com.gnss.core.service.IDownCommandMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCountUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>Description: JT808 0x8202指令参数</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2018-12-14
 */
@ApiModel("0x8202临时位置跟踪控制")
@Getter
@Setter
@DownCommand(messageId = 0x8202, respMessageId = 0x0001, desc = "临时位置跟踪控制")
public class Command8202Param implements IDownCommandMessage {

    @ApiModelProperty(value = "时间间隔", position = 1)
    private int interval;

    @ApiModelProperty(value = "位置跟踪有效期", position = 2)
    private Integer duration;

    @Override
    public byte[] buildMessageBody(TerminalProto terminalInfo) throws Exception {
        byte[] msgBodyArr = null;
        if (interval == 0) {
            msgBodyArr = new byte[2];
        } else {
            if (duration == null) {
                throw new ApplicationException("位置跟踪有效期不能为空");
            }
            ByteBuf msgBody = Unpooled.buffer(6);
            msgBody.writeShort(interval).writeInt(duration);
            msgBodyArr = msgBody.array();
            ReferenceCountUtil.release(msgBody);
        }
        return msgBodyArr;
    }

    @Override
    public String toString() {
        Map<String, Object> msgBodyItems = new LinkedHashMap<>();
        msgBodyItems.put("时间间隔", interval);
        msgBodyItems.put("位置跟踪有效期", duration);
        return msgBodyItems.toString();
    }
}