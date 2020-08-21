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
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>Description: JT808 0x8304指令参数</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2019-9-14
 */
@ApiModel("0x8304信息服务")
@Getter
@Setter
@DownCommand(messageId = 0x8304, respMessageId = 0x0001, desc = "信息服务")
public class Command8304Param implements IDownCommandMessage {

    @ApiModelProperty(value = "信息类型(范围0-127)", required = true, position = 1)
    @NotNull(message = "信息类型不能为空")
    @Range(min = 0, max = 127, message = "信息类型范围为0-127")
    private Integer infoType;

    @ApiModelProperty(value = "信息内容", required = true, position = 2)
    @NotBlank(message = "信息内容不能为空")
    private String infoContent;

    @ApiModelProperty(value = "信息长度", hidden = true)
    private int infoContentLen;

    @Override
    public byte[] buildMessageBody(TerminalProto terminalInfo) throws Exception {
        byte[] infoContentArr = infoContent.getBytes(CommonConstant.DEFAULT_CHARSET_NAME);
        infoContentLen = infoContentArr.length;
        ByteBuf msgBody = Unpooled.buffer(3 + infoContentLen);
        msgBody.writeByte(infoType)
                .writeShort(infoContentLen)
                .writeBytes(infoContentArr);
        byte[] msgBodyArr = msgBody.array();
        ReferenceCountUtil.release(msgBody);
        return msgBodyArr;
    }

    @Override
    public String toString() {
        Map<String, Object> msgBodyItems = new LinkedHashMap<>();
        msgBodyItems.put("信息类型", infoType);
        msgBodyItems.put("信息长度", infoContentLen);
        msgBodyItems.put("信息内容", infoContent);
        return msgBodyItems.toString();
    }
}