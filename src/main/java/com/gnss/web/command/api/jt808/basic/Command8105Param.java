package com.gnss.web.command.api.jt808.basic;

import com.gnss.core.annotations.DownCommand;
import com.gnss.core.constants.CommonConstant;
import com.gnss.core.proto.TerminalProto;
import com.gnss.core.service.IDownCommandMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCountUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Range;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>Description: JT808 0x8105指令参数</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2018-12-14
 */
@ApiModel("0x8105终端控制")
@Getter
@Setter
@DownCommand(messageId = 0x8105, respMessageId = 0x0001, desc = "终端控制")
public class Command8105Param implements IDownCommandMessage {

    @ApiModelProperty(value = "命令字", required = true, position = 1)
    @Range(min = 1, max = 7, message = "命令字范围1-7")
    private int command;

    @ApiModelProperty(value = "命令参数", position = 2)
    private String param;

    @Override
    public byte[] buildMessageBody(TerminalProto terminalInfo) throws Exception {
        byte[] msgBodyArr = null;
        //无线升级和控制终端连接指定服务器
        if (command == 1 || command == 2) {
            if (StringUtils.isNotBlank(param)) {
                byte[] paramArr = param.getBytes(CommonConstant.DEFAULT_CHARSET_NAME);
                ByteBuf msgBody = Unpooled.buffer(paramArr.length + 1);
                msgBody.writeByte(command).writeBytes(paramArr);
                msgBodyArr = msgBody.array();
                ReferenceCountUtil.release(msgBody);
            }
        } else {
            msgBodyArr = new byte[]{(byte) command};
        }
        return msgBodyArr;
    }

    @Override
    public String toString() {
        Map<String, Object> msgBodyItems = new LinkedHashMap<>();
        msgBodyItems.put("命令字", command);
        msgBodyItems.put("命令参数", param);
        return msgBodyItems.toString();
    }
}
