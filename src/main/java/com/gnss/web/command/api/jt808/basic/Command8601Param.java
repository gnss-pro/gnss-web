package com.gnss.web.command.api.jt808.basic;

import com.gnss.core.annotations.DownCommand;
import com.gnss.core.proto.TerminalProto;
import com.gnss.core.service.IDownCommandMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCountUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

/**
 * <p>Description: JT808 0x8601指令参数</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2019-12-14
 */
@ApiModel("0x8601删除圆形区域")
@Getter
@Setter
@DownCommand(messageId = 0x8601, respMessageId = 0x0001, desc = "删除圆形区域")
public class Command8601Param implements IDownCommandMessage {

    @ApiModelProperty(value = "区域ID列表", required = true, position = 1)
    @NotEmpty(message = "区域ID列表不能为空")
    private LinkedHashSet<Integer> regionIdList;

    @ApiModelProperty(value = "区域数", hidden = true)
    private int regionCount;

    @Override
    public byte[] buildMessageBody(TerminalProto terminalInfo) throws Exception {
        regionCount = regionIdList.size();
        ByteBuf msgBody = Unpooled.buffer(4 * regionCount + 1);
        msgBody.writeByte(regionCount);
        regionIdList.forEach(msgBody::writeInt);
        byte[] msgBodyArr = msgBody.array();
        ReferenceCountUtil.release(msgBody);
        return msgBodyArr;
    }

    @Override
    public String toString() {
        Map<String, Object> msgBodyItems = new LinkedHashMap<>();
        msgBodyItems.put("区域数", regionCount);
        msgBodyItems.put("区域ID列表", regionIdList);
        return msgBodyItems.toString();
    }
}