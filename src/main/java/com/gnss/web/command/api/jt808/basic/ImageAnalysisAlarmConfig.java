package com.gnss.web.command.api.jt808.basic;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>Description: 图像分析报警设置</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2020/6/4
 */
@ApiModel("图像分析报警设置")
@Getter
@Setter
public class ImageAnalysisAlarmConfig {

    @ApiModelProperty(value = "车辆核载人数(范围1-127)", position = 1)
    @Range(min = 1, max = 127, message = "车辆核载人数范围为1-127")
    private int seats;

    @ApiModelProperty(value = "疲劳程度阈值(范围1-127)", position = 2)
    @Range(min = 1, max = 127, message = "疲劳程度阈值范围为1-127")
    private int fatigueThreshold = 5;

    public byte[] buildMessageBody() throws Exception {
        return new byte[]{(byte) seats, (byte) fatigueThreshold};
    }

    @Override
    public String toString() {
        Map<String, Object> items = new LinkedHashMap<>();
        items.put("车辆核载人数", seats);
        items.put("疲劳程度阈值", fatigueThreshold);
        return items.toString();
    }
}