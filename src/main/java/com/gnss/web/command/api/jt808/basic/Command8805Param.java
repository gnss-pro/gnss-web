package com.gnss.web.command.api.jt808.basic;

import com.gnss.core.annotations.DownCommand;
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
 * <p>Description: JT808 0x8805指令参数</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2019-9-14
 */
@ApiModel("0x8805单条存储多媒体数据检索上传命令")
@Data
@DownCommand(messageId = 0x8805, respMessageId = 0x0001, desc = "单条存储多媒体数据检索上传命令")
public class Command8805Param implements IDownCommandMessage {

    @ApiModelProperty(value = "多媒体ID", required = true, position = 1)
    @NotNull(message = "多媒体ID不能为空")
    private Long mediaId;

    @ApiModelProperty(value = "删除标志(0:保留;1:删除),默认0", position = 2)
    @Range(min = 0, max = 1, message = "删除标志为0或1")
    private int deleteFlag;

    @Override
    public byte[] buildMessageBody(TerminalProto terminalInfo) throws Exception {
        ByteBuf msgBody = Unpooled.buffer(5);
        msgBody.writeInt(mediaId.intValue())
                .writeByte(deleteFlag);
        byte[] msgBodyArr = msgBody.array();
        ReferenceCountUtil.release(msgBody);
        return msgBodyArr;
    }

    @Override
    public String toString() {
        Map<String, Object> msgBodyItems = new LinkedHashMap<>();
        msgBodyItems.put("多媒体ID", mediaId);
        msgBodyItems.put("删除标志", deleteFlag == 0 ? "0:保留" : "1:删除");
        return msgBodyItems.toString();
    }
}