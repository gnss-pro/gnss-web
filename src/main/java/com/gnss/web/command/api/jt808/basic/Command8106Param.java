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
 * <p>Description: JT808 0x8106指令参数</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2019-12-14
 */
@ApiModel("0x8106查询指定终端参数")
@Getter
@Setter
@DownCommand(messageId = 0x8106, respMessageId = 0x0104, desc = "查询指定终端参数")
public class Command8106Param implements IDownCommandMessage {

    @ApiModelProperty(value = "参数ID列表", required = true, position = 1)
    @NotEmpty(message = "参数ID列表不能为空")
    private LinkedHashSet<Integer> paramIdList;

    @ApiModelProperty(value = "参数总数", hidden = true)
    private int paramIdCount;

    @Override
    public byte[] buildMessageBody(TerminalProto terminalInfo) throws Exception {
        paramIdCount = paramIdList.size();
        ByteBuf msgBody = Unpooled.buffer(4 * paramIdCount + 1);
        //设置参数总数
        msgBody.writeByte(paramIdCount);
        for (Integer paramId : paramIdList) {
            msgBody.writeInt(paramId);
        }
        byte[] msgBodyArr = msgBody.array();
        ReferenceCountUtil.release(msgBody);
        return msgBodyArr;
    }

    @Override
    public String toString() {
        Map<String, Object> msgBodyItems = new LinkedHashMap<>();
        msgBodyItems.put("参数总数", paramIdCount);
        msgBodyItems.put("参数ID列表", paramIdList);
        return msgBodyItems.toString();
    }
}
