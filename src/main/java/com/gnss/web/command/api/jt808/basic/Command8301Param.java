package com.gnss.web.command.api.jt808.basic;

import com.gnss.core.annotations.DownCommand;
import com.gnss.core.constants.CommonConstant;
import com.gnss.core.exception.ApplicationException;
import com.gnss.core.proto.TerminalProto;
import com.gnss.core.service.IDownCommandMessage;
import com.gnss.web.common.constant.EventConfigTypeEnum;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCountUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Description: JT808 0x8301指令参数</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2019-9-14
 */
@ApiModel("0x8301事件设置")
@Getter
@Setter
@DownCommand(messageId = 0x8301, respMessageId = 0x0001, desc = "事件设置")
public class Command8301Param implements IDownCommandMessage {

    @ApiModelProperty(value = "设置类型", required = true, position = 1)
    @NotNull(message = "设置类型不能为空")
    private EventConfigTypeEnum configType;

    @ApiModelProperty(value = "事件项列表", position = 2)
    @Valid
    private List<EventConfig> itemList;

    @ApiModelProperty(value = "设置总数", hidden = true)
    private int itemCount;

    @Override
    public byte[] buildMessageBody(TerminalProto terminalInfo) throws Exception {
        //删除终端所有事件,不带后续字节
        if (configType == EventConfigTypeEnum.TYPE_0) {
            return new byte[]{(byte) configType.getValue()};
        }

        //其他设置类型的事件项列表不能为空
        if (CollectionUtils.isEmpty(itemList)) {
            throw new ApplicationException("事件项列表不能为空");
        }

        byte[] msgBodyArr = null;
        itemCount = itemList.size();
        ByteBuf msgBody = Unpooled.buffer();
        msgBody.writeByte(configType.getValue())
                .writeByte(itemCount);
        try {
            for (EventConfig eventConfig : itemList) {
                msgBody.writeByte(eventConfig.getEventId());
                //删除指定事件,无需带事件项的事件内容
                if (configType == EventConfigTypeEnum.TYPE_4) {
                    msgBody.writeByte(0);
                } else {
                    String eventContent = eventConfig.getEventContent();
                    if (eventContent == null) {
                        throw new ApplicationException("事件内容不能为空");
                    }
                    byte[] contentArr = eventContent.getBytes(CommonConstant.DEFAULT_CHARSET_NAME);
                    eventConfig.setEventContentLen(contentArr.length);
                    msgBody.writeByte(contentArr.length)
                            .writeBytes(contentArr);
                }
            }

            int len = msgBody.readableBytes();
            msgBodyArr = new byte[len];
            msgBody.getBytes(msgBody.readerIndex(), msgBodyArr);
        } catch (Exception e) {
            throw e;
        } finally {
            ReferenceCountUtil.release(msgBody);
        }
        return msgBodyArr;
    }

    @Override
    public String toString() {
        Map<String, Object> msgBodyItems = new LinkedHashMap<>();
        msgBodyItems.put("设置类型", String.format("%d:%s", configType.getValue(), configType.getDesc()));
        msgBodyItems.put("设置总数", itemCount);
        msgBodyItems.put("事件项列表", CollectionUtils.isEmpty(itemList) ? "" : itemList.toString());
        return msgBodyItems.toString();
    }
}