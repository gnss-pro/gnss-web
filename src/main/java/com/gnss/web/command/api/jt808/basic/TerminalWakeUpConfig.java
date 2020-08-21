package com.gnss.web.command.api.jt808.basic;

import com.gnss.core.utils.NumberUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCountUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Description: 终端休眠唤醒模式设置</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2020/6/4
 */
@ApiModel("终端休眠唤醒模式设置")
@Getter
@Setter
public class TerminalWakeUpConfig {

    @ApiModelProperty(value = "休眠唤醒模式", position = 1)
    private List<Integer> wakeUpModeBits;

    @ApiModelProperty(value = "唤醒条件类型", position = 2)
    private List<Integer> conditionTypeBits;

    @ApiModelProperty(value = "定时唤醒日设置", position = 3)
    private List<Integer> wakeUpDateBits;

    @ApiModelProperty(value = "定时唤醒启用标志", position = 4)
    private List<Integer> timePeriodBits;

    @ApiModelProperty(value = "定时唤醒日设置", position = 5)
    @NotNull(message = "定时唤醒日设置不能为空")
    @Size(min = 16, max = 16, message = "定时唤醒日设置长度为16")
    private List<String> wakeUpTimeList;

    @ApiModelProperty(value = "休眠唤醒模式", hidden = true)
    private int wakeUpMode;

    @ApiModelProperty(value = "唤醒条件类型", hidden = true)
    private int conditionType;

    @ApiModelProperty(value = "定时唤醒日设置", hidden = true)
    private int wakeUpDate;

    @ApiModelProperty(value = "定时唤醒启用标志", hidden = true)
    private int timePeriod;

    public byte[] buildMessageBody() throws Exception {
        if (wakeUpModeBits == null) {
            wakeUpModeBits = new ArrayList<>();
        }
        //勾选了唤醒条件,将休眠唤醒模式的bit0设置为1
        if (!CollectionUtils.isEmpty(conditionTypeBits)) {
            wakeUpModeBits.add(0);
            conditionType = NumberUtil.bitsToInt(conditionTypeBits, 8);
        }
        //定时唤醒日设置
        if (!CollectionUtils.isEmpty(wakeUpDateBits)) {
            wakeUpDate = NumberUtil.bitsToInt(wakeUpDateBits, 8);
        }
        //定时唤醒启用标志
        if (!CollectionUtils.isEmpty(timePeriodBits)) {
            timePeriod = NumberUtil.bitsToInt(timePeriodBits, 8);
        }
        //设置了定时唤醒日或者日定时唤醒参数,将休眠唤醒模式的bit1设置为1
        if (wakeUpDate != 0 || timePeriod != 0) {
            wakeUpModeBits.add(1);
        }
        //休眠唤醒模式
        wakeUpMode = NumberUtil.bitsToInt(wakeUpModeBits, 8);

        ByteBuf msgBody = Unpooled.buffer(20);
        msgBody.writeByte(wakeUpMode)
                .writeByte(conditionType)
                .writeByte(wakeUpDate)
                .writeByte(timePeriod);
        //时间采用BCD码表示
        wakeUpTimeList.forEach(timeItem -> msgBody.writeByte(Byte.parseByte(timeItem, 16)));
        byte[] msgBodyArr = msgBody.array();
        ReferenceCountUtil.release(msgBody);
        return msgBodyArr;
    }

    @Override
    public String toString() {
        Map<String, Object> items = new LinkedHashMap<>();
        items.put("休眠唤醒模式", String.format("0x%02X", wakeUpMode));
        items.put("唤醒条件类型", String.format("0x%02X", conditionType));
        items.put("定时唤醒日设置", String.format("0x%02X", wakeUpDate));
        items.put("定时唤醒启用标志", String.format("0x%02X", timePeriod));
        for (int i = 0; i < wakeUpTimeList.size() / 2; i++) {
            String timeStr = wakeUpTimeList.get(i * 2) + ":" + wakeUpTimeList.get(i * 2 + 1);
            items.put("时间段" + (i + 1) + "唤醒时间", timeStr);
        }
        return items.toString();
    }
}