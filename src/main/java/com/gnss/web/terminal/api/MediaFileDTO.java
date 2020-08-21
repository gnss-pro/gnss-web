package com.gnss.web.terminal.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>Description: 多媒体文件详情</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2020/4/20
 */
@ApiModel("多媒体文件详情")
@Data
public class MediaFileDTO {

    @ApiModelProperty(value = "多媒体文件ID", position = 1)
    private String mediaFileId;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间", position = 2)
    private LocalDateTime createdDate;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "最后更新时间", position = 3)
    private LocalDateTime lastModifiedDate;

    @ApiModelProperty(value = "终端ID", position = 4)
    private String terminalId;

    @ApiModelProperty(value = "车牌号码", position = 5)
    private String vehicleNum;

    @ApiModelProperty(value = "终端号码", position = 6)
    private String terminalNum;

    @ApiModelProperty(value = "sim卡号", position = 7)
    private String simNum;

    @ApiModelProperty(value = "车牌颜色", position = 8)
    private String plateColor;

    @ApiModelProperty(value = "多媒体ID", position = 9)
     private long mediaId;

    @ApiModelProperty(value = "通道ID", position = 10)
    private int channelId;

    @ApiModelProperty(value = "多媒体类型", position = 11)
    private String mediaType;

    @ApiModelProperty(value = "多媒体格式编码", position = 12)
    private String mediaFormatCode;

    @ApiModelProperty(value = "事件项编码", position = 13)
    private String eventItemCode;

    @ApiModelProperty(value = "文件名", position = 14)
    private String fileName;

    @ApiModelProperty(value = "文件大小", position = 15)
    private String fileSize;

    @ApiModelProperty(value = "base64加密后的文件路径", position = 16)
    private String base64FilePath;

    @ApiModelProperty(value = "位置信息", position = 17)
    private LocationDTO location;
}