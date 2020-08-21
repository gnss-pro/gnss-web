package com.gnss.web.terminal.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>Description: 主动安全附件明细</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2019/6/8
 */
@ApiModel("主动安全附件明细")
@Data
public class AttachmentDetailDTO {

    @ApiModelProperty(value = "ID")
    private String attachmentId;

    @ApiModelProperty(value = "文件类型(0图片,1音频,2视频,3文本,4其他)", position = 2)
    private Integer fileType;

    @ApiModelProperty(value = "文件名称", position = 3)
    private String fileName;

    @ApiModelProperty(value = "文件大小", position = 4)
    private String fileSize;

    @ApiModelProperty(value = "base64加密后的文件路径", position = 5)
    private String base64FilePath;
}