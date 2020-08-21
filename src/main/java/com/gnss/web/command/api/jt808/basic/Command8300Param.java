package com.gnss.web.command.api.jt808.basic;

import com.gnss.core.annotations.DownCommand;
import com.gnss.core.constants.CommonConstant;
import com.gnss.core.constants.ProtocolEnum;
import com.gnss.core.proto.TerminalProto;
import com.gnss.core.service.IDownCommandMessage;
import com.gnss.core.utils.NumberUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCountUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Description: JT808 0x8300指令参数</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2019-9-14
 */
@ApiModel("0x8300文本信息下发参数")
@Getter
@Setter
@DownCommand(messageId = 0x8300, respMessageId = 0x0001, desc = "文本信息下发")
public class Command8300Param implements IDownCommandMessage {

    @ApiModelProperty(value = "标志位", position = 1)
    private List<Integer> flags;

    @ApiModelProperty(value = "文本类型(JT808-2019新增)", position = 2)
    private Integer textType;

    @ApiModelProperty(value = "文本信息", required = true, position = 3)
    @NotBlank(message = "文本信息不能为空")
    private String textMsg;

    @ApiModelProperty(value = "标志", hidden = true)
    private int flag;

    @Override
    public byte[] buildMessageBody(TerminalProto terminalInfo) throws Exception {
        if (flags == null) {
            flags = Collections.emptyList();
        }
        byte[] msgBodyArr = null;
        ByteBuf msgBody = null;
        try {
            flag = NumberUtil.bitsToInt(flags, 8);
            byte[] textMsgArr = textMsg.getBytes(CommonConstant.DEFAULT_CHARSET_NAME);
            int msgBodyLen = 1 + textMsgArr.length;
            if (terminalInfo.getProtocolType() == ProtocolEnum.JT808_2019) {
                msgBodyLen += 1;
                msgBody = Unpooled.buffer(msgBodyLen);
                msgBody.writeByte(flag)
                        .writeByte(textType == null ? 1 : textType)
                        .writeBytes(textMsgArr);
            } else {
                msgBody = Unpooled.buffer(msgBodyLen);
                msgBody.writeByte(flag)
                        .writeBytes(textMsgArr);
            }
            msgBodyArr = msgBody.array();
        } catch (Exception e) {
            throw e;
        } finally {
            if (msgBody != null) {
                ReferenceCountUtil.release(msgBody);
            }
        }
        return msgBodyArr;
    }

    @Override
    public String toString() {
        Map<String, Object> msgBodyItems = new LinkedHashMap<>();
        msgBodyItems.put("标志", NumberUtil.formatShortNum(flag));
        if (textType != null) {
            msgBodyItems.put("文本类型", textType);
        }
        msgBodyItems.put("文本信息", textMsg);
        return msgBodyItems.toString();
    }
}