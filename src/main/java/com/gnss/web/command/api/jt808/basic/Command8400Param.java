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
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>Description: JT808 0x8400指令参数</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2018-9-14
 */
@ApiModel("0x8400电话回拨参数")
@Getter
@Setter
@DownCommand(messageId = 0x8400, respMessageId = 0x0001, desc = "电话回拨")
public class Command8400Param implements IDownCommandMessage {

    @ApiModelProperty(value = "标志(0:普通通话,1:监听),默认1", position = 1)
    @Range(min = 0, max = 1, message = "标志为0或1")
    private int flag = 1;

    @ApiModelProperty(value = "电话号码", required = true, position = 2)
    @NotBlank(message = "电话号码不能为空")
    private String phoneNum;

    @Override
    public byte[] buildMessageBody(TerminalProto terminalInfo) throws Exception {
        byte[] phoneNumArr = phoneNum.getBytes(CommonConstant.DEFAULT_CHARSET_NAME);
        ByteBuf msgBody = Unpooled.buffer(phoneNumArr.length + 1);
        msgBody.writeByte(flag)
                .writeBytes(phoneNumArr);
        byte[] msgBodyArr = msgBody.array();
        ReferenceCountUtil.release(msgBody);
        return msgBodyArr;
    }

    @Override
    public String toString() {
        Map<String, Object> msgBodyItems = new LinkedHashMap<>();
        msgBodyItems.put("标志", flag == 0 ? "0:普通通话" : flag + ":监听");
        msgBodyItems.put("电话号码", phoneNum);
        return msgBodyItems.toString();
    }
}