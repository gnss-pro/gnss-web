package com.gnss.web.command.api.jt808.basic;

import com.gnss.core.annotations.DownCommand;
import com.gnss.core.constants.CommonConstant;
import com.gnss.core.proto.TerminalProto;
import com.gnss.core.service.IDownCommandMessage;
import com.gnss.core.utils.NumberUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCountUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Description: JT808 0x8302指令参数</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2019-9-14
 */
@ApiModel("0x8302提问下发参数")
@Getter
@Setter
@DownCommand(messageId = 0x8302, respMessageId = 0x0302, desc = "提问下发")
public class Command8302Param implements IDownCommandMessage {

    @ApiModelProperty(value = "标志位", position = 1)
    private List<Integer> flags;

    @ApiModelProperty(value = "问题", required = true, position = 2)
    @NotBlank(message = "问题不能为空")
    private String questionContent;

    @ApiModelProperty(value = "候选答案列表", required = true, position = 3)
    @NotEmpty(message = "候选答案列表不能为空")
    @Valid
    private List<AnswerConfig> answerList;

    @ApiModelProperty(value = "标志", hidden = true)
    private int flag;

    @ApiModelProperty(value = "问题长度", hidden = true)
    private int questionLen;

    @Override
    public byte[] buildMessageBody(TerminalProto terminalInfo) throws Exception {
        if (flags == null) {
            flags = Collections.emptyList();
        }

        byte[] msgBodyArr = null;
        ByteBuf msgBody = Unpooled.buffer();
        try {
            flag = NumberUtil.bitsToInt(flags, 8);
            byte[] questionArr = questionContent.getBytes(CommonConstant.DEFAULT_CHARSET_NAME);
            questionLen = questionArr.length;
            msgBody.writeByte(flag)
                    .writeByte(questionLen)
                    .writeBytes(questionArr);
            //写入答案列表
            for (AnswerConfig answerConfig : answerList) {
                byte[] answerArr = answerConfig.getAnswerContent().getBytes(CommonConstant.DEFAULT_CHARSET_NAME);
                int answerLen = answerArr.length;
                answerConfig.setAnswerContentLen(answerLen);
                msgBody.writeByte(answerConfig.getAnswerId())
                        .writeShort(answerLen)
                        .writeBytes(answerArr);
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
        msgBodyItems.put("标志", NumberUtil.formatShortNum(flag));
        msgBodyItems.put("问题长度", questionLen);
        msgBodyItems.put("问题", questionContent);
        msgBodyItems.put("候选答案列表", answerList.toString());
        return msgBodyItems.toString();
    }
}