package com.gnss.web.command.api.jt808.media;

import com.gnss.core.constants.MediaDataTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

/**
 * <p>Description: 开始录像</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2020/3/31
 */
@ApiModel("开始录像")
@Data
public class StartRecordDTO {

    @ApiModelProperty(value = "终端ID", required = true, position = 1)
    @NotNull(message = "终端ID不能为空")
    private String terminalId;

    @ApiModelProperty(value = "逻辑通道号", required = true, position = 2)
    @Range(min = 1, max = 127, message = "逻辑通道号范围为1-127")
    private int channelId;

    @ApiModelProperty(value = "数据类型(0:音视频,1:视频,2:双向对讲,3:监听,4:中心广播,5:透传),默认:AV音视频", required = true, position = 3)
    @NotNull(message = "数据类型不能为空")
    private MediaDataTypeEnum avItemType = MediaDataTypeEnum.AV;

    @ApiModelProperty(value = "码流类型(0:主码流,1:子码流),默认1", position = 4)
    @Range(min = 0, max = 1, message = "码流类型为0或1")
    private int streamType = 1;

    @ApiModelProperty(value = "录像时长(秒)", position = 5)
    @Range(min = 1, max = Integer.MAX_VALUE, message = "录像时长范围1-2147483647秒")
    private int recordDuration;
}