package com.gnss.web.command.api.jt808.media;

import com.gnss.core.annotations.DownCommand;
import com.gnss.core.constants.jt1078.DownloadControlTypeEnum;
import com.gnss.core.proto.TerminalProto;
import com.gnss.core.service.IDownCommandMessage;
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
 * <p>Description: JT808 0x9207指令参数</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2018-9-14
 */
@ApiModel("0x9207文件上传控制")
@Data
@DownCommand(messageId = 0x9207, respMessageId = 0x0001, desc = "文件上传控制")
public class Command9207Param implements IDownCommandMessage {

    @ApiModelProperty(value = "应答流水号", required = true, position = 1)
    @Range(min = 1, max = Short.MAX_VALUE, message = "应答流水号范围1-32767")
    private int msgSeq;

    @ApiModelProperty(value = "上传控制(0:暂停,1:继续,2:取消)", required = true, position = 2)
    @NotNull(message = "上传控制不能为空")
    private DownloadControlTypeEnum ctrlType;

    @Override
    public byte[] buildMessageBody(TerminalProto terminalInfo) throws Exception {
        ByteBuf msgBody = Unpooled.buffer(3);
        msgBody.writeShort(msgSeq)
                .writeByte(ctrlType.getValue());
        byte[] msgBodyArr = msgBody.array();
        ReferenceCountUtil.release(msgBody);
        return msgBodyArr;
    }

    @Override
    public String toString() {
        Map<String, Object> msgBodyItems = new LinkedHashMap<>();
        msgBodyItems.put("应答流水号", msgSeq);
        msgBodyItems.put("上传控制", String.format("%d:%s", ctrlType.getValue(), ctrlType.getDesc()));
        return msgBodyItems.toString();
    }
}