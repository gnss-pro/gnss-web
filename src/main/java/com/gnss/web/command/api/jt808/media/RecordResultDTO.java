package com.gnss.web.command.api.jt808.media;

import com.gnss.core.constants.MediaDataTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>Description: 录像结果</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2020/3/31
 */
@ApiModel("录像结果")
@Data
public class RecordResultDTO {

    @ApiModelProperty(value = "录像ID", position = 1)
    private String recordId;

    @ApiModelProperty(value = "终端ID", position = 2)
    private String terminalId;

    @ApiModelProperty(value = "逻辑通道号", position = 3)
    private int channelId;

    @ApiModelProperty(value = "数据类型(0:音视频,1:视频,2:双向对讲,3:监听,4:中心广播,5:透传)", position = 4)
    private MediaDataTypeEnum avItemType;

    @ApiModelProperty(value = "码流类型(0:主码流,1:子码流)", position = 5)
    private int streamType;
}
