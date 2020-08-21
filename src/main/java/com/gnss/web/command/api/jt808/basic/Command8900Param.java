package com.gnss.web.command.api.jt808.basic;

import com.gnss.core.annotations.DownCommand;
import com.gnss.core.constants.CommonConstant;
import com.gnss.core.constants.TransparentTypeEnum;
import com.gnss.core.proto.TerminalProto;
import com.gnss.core.service.IDownCommandMessage;
import com.gnss.core.utils.NumberUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCountUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>Description: JT808 0x8900指令参数</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2018-9-14
 */
@ApiModel("0x8900数据下行透传")
@Data
@DownCommand(messageId = 0x8900, respMessageId = 0x0001, desc = "数据下行透传")
public class Command8900Param implements IDownCommandMessage {

    @ApiModelProperty(value = "发送类型(0:文本,1:十六进制字符串)", required = true, position = 1)
    @Range(min = 0, max = 1, message = "发送类型为0或1")
    private int sendType;

    @ApiModelProperty(value = "透传消息类型", required = true, position = 2)
    @NotNull(message = "透传消息类型不能为空")
    private TransparentTypeEnum msgType;

    @ApiModelProperty(value = "透传消息内容", position = 3)
    private String content;

    @ApiModelProperty(value = "透传消息内容", hidden = true)
    private byte[] contentBytes;

    @Override
    public byte[] buildMessageBody(TerminalProto terminalInfo) throws Exception {
        ByteBuf msgBody = null;
        byte[] msgBodyArr = null;
        if (content == null) {
            msgBodyArr = new byte[]{(byte) msgType.getValue()};
        } else {
            try {
                if (sendType == 0) {
                    contentBytes = content.getBytes(CommonConstant.DEFAULT_CHARSET_NAME);
                } else {
                    content = content.replaceAll(" ", "");
                    contentBytes = new byte[content.length() / 2];
                    for (int i = 0; i < contentBytes.length; i++) {
                        contentBytes[i] = (byte) Integer.parseInt(content.substring(i * 2, i * 2 + 2), 16);
                    }
                }
                msgBody = Unpooled.buffer(1 + contentBytes.length);
                msgBody.writeByte(msgType.getValue())
                        .writeBytes(contentBytes);
                msgBodyArr = msgBody.array();
            } catch (Exception e) {
                throw e;
            } finally {
                if (msgBody != null) {
                    ReferenceCountUtil.release(msgBody);
                }
            }
        }
        return msgBodyArr;
    }

    @Override
    public String toString() {
        Map<String, Object> msgBodyItems = new LinkedHashMap<>();
        msgBodyItems.put("透传消息类型", String.format("%s:%s", NumberUtil.formatShortNum(msgType.getValue()), msgType.getDesc()));
        msgBodyItems.put("透传消息内容", content);
        return msgBodyItems.toString();
    }
}