package com.gnss.web.terminal.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>Description: 录像文件详情</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2019/6/8
 */
@ApiModel("录像文件详情")
@Data
public class RecordFileDTO {

    @ApiModelProperty(value = "录像文件ID")
    private String recordFileId;

    @ApiModelProperty(value = "车牌号", position = 1)
    private String vehicleNum;

    @ApiModelProperty(value = "车牌颜色", position = 2)
    private String plateColor;

    @ApiModelProperty(value = "终端手机号", position = 3)
    private String simNum;

    @ApiModelProperty(value = "存储位置", position = 4)
    private String storageType;

    @ApiModelProperty(value = "音视频资源类型", position = 5)
    private String avItemType;

    @ApiModelProperty(value = "音视频资源类型描述", position = 6)
    private String avItemTypeDesc;

    @ApiModelProperty(value = "通道ID", position = 7)
    private int channelId;

    @ApiModelProperty(value = "开始时间", position = 8)
    private String startTime;

    @ApiModelProperty(value = "结束时间", position = 9)
    private String endTime;

    @ApiModelProperty(value = "文件状态(0:正在上传,1:已完成,2:失败)", position = 10)
    private int fileStatus;

    @ApiModelProperty(value = "消息流水号", position = 11)
    private Integer msgFlowId;

    @ApiModelProperty(value = "文件大小", position = 12)
    private Long fileSize;

    @ApiModelProperty(value = "文件名", position = 13)
    private String fileName;

    @ApiModelProperty(value = "base64加密后的文件路径", position = 14)
    private String base64FilePath;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间", position = 15)
    private LocalDateTime createdDate;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "最后更新时间", position = 16)
    private LocalDateTime lastModifiedDate;
}