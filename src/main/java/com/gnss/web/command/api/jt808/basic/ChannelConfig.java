package com.gnss.web.command.api.jt808.basic;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>Description: 音视频通道设置</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2020/6/4
 */
@ApiModel("音视频通道设置")
@Getter
@Setter
public class ChannelConfig {

    @ApiModelProperty(value = "物理通道号", position = 1)
    @Range(min = 1, max = 127, message = "物理通道号范围为1-127")
    private int physicalChannel;

    @ApiModelProperty(value = "逻辑通道号", position = 2)
    @Range(min = 1, max = 127, message = "逻辑通道号范围为1-127")
    private int logicalChannel;

    @ApiModelProperty(value = "通道类型", position = 3)
    @Range(min = 0, max = 2, message = "通道类型范围为0-2")
    private int channelType;

    @ApiModelProperty(value = "是否连接云台(0:未连接,1:连接)", position = 4)
    @Range(min = 0, max = 1, message = "是否连接云台为0或1")
    private int connectPtz;

    @Override
    public String toString() {
        Map<String, Object> items = new LinkedHashMap<>();
        items.put("物理通道号", physicalChannel);
        items.put("逻辑通道号", logicalChannel);
        items.put("通道类型", channelType);
        items.put("是否连接云台", connectPtz);
        return items.toString();
    }
}