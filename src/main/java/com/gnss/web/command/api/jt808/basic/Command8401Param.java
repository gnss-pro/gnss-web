package com.gnss.web.command.api.jt808.basic;

import com.gnss.core.annotations.DownCommand;
import com.gnss.core.constants.CommonConstant;
import com.gnss.core.exception.ApplicationException;
import com.gnss.core.proto.TerminalProto;
import com.gnss.core.service.IDownCommandMessage;
import com.gnss.web.common.constant.PhoneBookConfigTypeEnum;
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
 * <p>Description: JT808 0x8401指令参数</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2019-9-14
 */
@ApiModel("0x8401设置电话本")
@Getter
@Setter
@DownCommand(messageId = 0x8401, respMessageId = 0x0001, desc = "设置电话本")
public class Command8401Param implements IDownCommandMessage {

    @ApiModelProperty(value = "设置类型", required = true, position = 1)
    @NotNull(message = "设置类型不能为空")
    private PhoneBookConfigTypeEnum configType;

    @ApiModelProperty(value = "联系人列表", position = 2)
    @Valid
    private List<PhoneBookConfig> itemList;

    @ApiModelProperty(value = "联系人总数", hidden = true)
    private int itemCount;

    @Override
    public byte[] buildMessageBody(TerminalProto terminalInfo) throws Exception {
        //删除终端所有联系人,不带后续字节
        if (configType == PhoneBookConfigTypeEnum.TYPE_0) {
            return new byte[]{(byte) configType.getValue()};
        }

        //其他设置类型的联系人列表不能为空
        if (CollectionUtils.isEmpty(itemList)) {
            throw new ApplicationException("联系人列表不能为空");
        }

        byte[] msgBodyArr = null;
        itemCount = itemList.size();
        ByteBuf msgBody = Unpooled.buffer();
        msgBody.writeByte(configType.getValue())
                .writeByte(itemCount);
        try {
            for (PhoneBookConfig phoneBookConfig : itemList) {
                //电话号码
                String phoneNum = phoneBookConfig.getPhoneNum();
                byte[] phoneNumArr = phoneNum.getBytes(CommonConstant.DEFAULT_CHARSET_NAME);
                //联系人
                String contact = phoneBookConfig.getContact();
                byte[] contactArr = contact.getBytes(CommonConstant.DEFAULT_CHARSET_NAME);
                phoneBookConfig.setPhoneNumLen(phoneNumArr.length);
                phoneBookConfig.setContactLen(contactArr.length);
                msgBody.writeByte(phoneBookConfig.getFlag())
                        .writeByte(phoneNumArr.length)
                        .writeBytes(phoneNumArr)
                        .writeByte(contactArr.length)
                        .writeBytes(contactArr);
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
        msgBodyItems.put("联系人总数", itemCount);
        msgBodyItems.put("联系人列表", CollectionUtils.isEmpty(itemList) ? "" : itemList.toString());
        return msgBodyItems.toString();
    }
}