package com.gnss.web.log.api;

import com.gnss.core.constants.ProtocolEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>Description: JT808日志详情信息</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2019/10/14
 */
@ApiModel("JT808日志详情信息")
@Data
public class Jt808LogDetailDTO {

    @ApiModelProperty(value = "日志ID", position = 1)
    private String id;

    @ApiModelProperty(value = "时间", position = 2)
    private String time;

    @ApiModelProperty(value = "协议类型", position = 3)
    private ProtocolEnum protocolType;

    @ApiModelProperty(value = "16进制消息ID", position = 4)
    private String msgIdHex;

    @ApiModelProperty(value = "消息ID描述", position = 5)
    private String msgIdDesc;

    @ApiModelProperty(value = "终端手机号", position = 6)
    private String phoneNumber;

    @ApiModelProperty(value = "车牌号", position = 7)
    private String vehicleNum;

    @ApiModelProperty(value = "车牌颜色", position = 8)
    private String vehiclePlateColor;

    @ApiModelProperty(value = "消息流水号", position = 9)
    private Integer msgFlowId;

    @ApiModelProperty(value = "加密方式(0：不加密，1：RSA加密)", position = 10)
    private Integer encryptType;

    @ApiModelProperty(value = "消息体描述", position = 11)
    private String msgBodyDesc;

    @ApiModelProperty(value = "链路类型", position = 12)
    private String linkType;

    @ApiModelProperty(value = "错误消息", position = 13)
    private String errorMsg;
}