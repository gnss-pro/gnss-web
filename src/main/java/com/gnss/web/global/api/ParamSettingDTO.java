package com.gnss.web.global.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>Description: 终端参数设置参数项信息</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2020/6/24
 */
@ApiModel("终端参数设置参数项信息")
@Data
public class ParamSettingDTO {

    @ApiModelProperty(value = "参数ID", position = 1)
    private long paramId;

    @ApiModelProperty(value = "参数描述", position = 2)
    private String paramDesc;
}
