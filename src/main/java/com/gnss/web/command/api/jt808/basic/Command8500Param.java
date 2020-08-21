package com.gnss.web.command.api.jt808.basic;

import com.gnss.core.annotations.DownCommand;
import com.gnss.core.constants.ProtocolEnum;
import com.gnss.core.proto.TerminalProto;
import com.gnss.core.service.IDownCommandMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCountUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Map;

/**
 * <p>Description: JT808 0x8500指令参数</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2019-12-14
 */
@ApiModel("0x8500车辆控制")
@Data
@DownCommand(messageId = 0x8500, respMessageId = 0x0500, desc = "车辆控制")
public class Command8500Param implements IDownCommandMessage {

    @ApiModelProperty(value = "控制类型", required = true, position = 1)
    @NotEmpty(message = "控制类型不能为空")
    private Map<String, Object> items;

    @Override
    public byte[] buildMessageBody(TerminalProto terminalInfo) throws Exception {
        ProtocolEnum protocolType = terminalInfo.getProtocolType();
        if (protocolType == ProtocolEnum.JT808_2019) {
            return buildMessageBody2019();
        }

        //构造JT808-2011和JT808-2013消息体
        return buildMessageBody();
    }

    /**
     * 构造JT808-2011和JT808-2013消息体
     *
     * @return
     * @throws Exception
     */
    private byte[] buildMessageBody() throws Exception {
        byte[] msgBodyArr = null;
        for (Map.Entry<String, Object> entry : items.entrySet()) {
            if ("0001".equals(entry.getKey())) {
                //车门控制
                msgBodyArr = new byte[]{Byte.parseByte(entry.getValue().toString())};
                break;
            } else if ("1000".equals(entry.getKey())) {
                //博实结断油电
                ByteBuf msgBody = Unpooled.buffer(2);
                msgBody.writeShort(0x1000);
                msgBodyArr = msgBody.array();
                ReferenceCountUtil.release(msgBody);
            }
        }
        return msgBodyArr;
    }

    /**
     * 构造JT808-2019消息体
     *
     * @return
     * @throws Exception
     */
    private byte[] buildMessageBody2019() throws Exception {
        byte[] msgBodyArr = null;
        ByteBuf msgBody = Unpooled.buffer();
        //设置参数总数,等后续所有控制类型写入后再重新设置
        msgBody.writeShort(1);
        int itemCount = 0;
        try {
            for (Map.Entry<String, Object> entry : items.entrySet()) {
                //车门
                if ("0001".equals(entry.getKey())) {
                    msgBody.writeShort(Integer.parseInt(entry.getKey(), 16));
                    msgBody.writeByte(Byte.parseByte(entry.getValue().toString()));
                    itemCount++;
                }
            }
            //重新设置参数总数
            msgBody.setShort(0, itemCount);
            int len = msgBody.readableBytes();
            msgBodyArr = new byte[len];
            msgBody.readBytes(msgBodyArr);
        } catch (Exception e) {
            throw e;
        } finally {
            ReferenceCountUtil.release(msgBody);
        }
        return msgBodyArr;
    }
}