package com.gnss.web.command.api.jt808.basic;

import com.gnss.core.annotations.DownCommand;
import com.gnss.core.proto.TerminalProto;
import com.gnss.core.service.IDownCommandMessage;
import com.gnss.web.common.constant.RegionConfigTypeEnum;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCountUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Description: JT808 0x8602指令参数</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2020/4/26
 */
@ApiModel("0x8602设置矩形区域")
@Getter
@Setter
@DownCommand(messageId = 0x8602, respMessageId = 0x0001, desc = "设置矩形区域")
public class Command8602Param implements IDownCommandMessage {

    @ApiModelProperty(value = "设置属性", required = true, position = 1)
    @NotNull(message = "设置属性不能为空")
    private RegionConfigTypeEnum configType;

    @ApiModelProperty(value = "区域项列表", required = true, position = 2)
    @NotEmpty(message = "区域项不能为空")
    @Valid
    private List<RectangleRegion> itemList;

    @ApiModelProperty(value = "区域总数", hidden = true)
    private int itemCount;

    @Override
    public byte[] buildMessageBody(TerminalProto terminalInfo) throws Exception {
        itemCount = itemList.size();
        ByteBuf msgBody = Unpooled.buffer();
        msgBody.writeByte(configType.getValue())
                .writeByte(itemCount);
        byte[] msgBodyArr;
        try {
            //写入区域项
            for (int i = 0; i < itemCount; i++) {
                RectangleRegion region = itemList.get(i);
                //生成区域ID
                if (region.getRegionId() == null) {
                    int regionId = i + 1;
                    region.setRegionId(regionId);
                }
                byte[] regionData = region.buildMessageBody(terminalInfo);
                msgBody.writeBytes(regionData);
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
        msgBodyItems.put("设置属性", String.format("%d:%s", configType.getValue(), configType.getDesc()));
        msgBodyItems.put("区域总数", itemCount);
        msgBodyItems.put("区域项列表", itemList.toString());
        return msgBodyItems.toString();
    }
}