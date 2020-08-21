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
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>Description: JT808 0x8A00指令参数</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2018-9-14
 */
@ApiModel("0x8A00平台RSA公钥")
@Data
@DownCommand(messageId = 0x8A00, respMessageId = 0x0A00, desc = "平台RSA公钥")
public class Command8A00Param implements IDownCommandMessage {

    @ApiModelProperty(value = "平台RSA公钥{e,n}的e", required = true, position = 1)
    private int keyE;

    @ApiModelProperty(value = "平台RSA公钥{e,n}的n,16进制字符串", required = true, position = 2)
    @NotBlank(message = "平台RSA公钥{e,n}的n不能为空")
    private String keyN;

    @ApiModelProperty(value = "平台RSA公钥{e,n}的n", hidden = true)
    private byte[] keyNBytes;

    @Override
    public byte[] buildMessageBody(TerminalProto terminalInfo) throws Exception {
        keyN = keyN.replaceAll(" ", "");
        int len = keyN.length() / 2;
        if (len > 128) {
            throw new ApplicationException("平台RSA公钥{e,n}的n不能超过128字节");
        }
        keyNBytes = new byte[128];
        for (int i = 0; i < len; i++) {
            keyNBytes[i] = (byte) Integer.parseInt(keyN.substring(i * 2, i * 2 + 2), 16);
        }

        ByteBuf msgBody = Unpooled.buffer(132);
        msgBody.writeInt(keyE)
                .writeBytes(keyNBytes);
        byte[] msgBodyArr = msgBody.array();
        ReferenceCountUtil.release(msgBody);
        return msgBodyArr;
    }

    @Override
    public String toString() {
        Map<String, Object> msgBodyItems = new LinkedHashMap<>();
        msgBodyItems.put("平台RSA公钥{e,n}的e", keyE);
        msgBodyItems.put("平台RSA公钥{e,n}的n", keyN);
        return msgBodyItems.toString();
    }
}