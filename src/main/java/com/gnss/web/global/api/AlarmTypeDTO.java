package com.gnss.web.global.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>Description: 报警类型描述</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2019/7/1
 */
@ApiModel("报警类型描述")
@Data
public class AlarmTypeDTO {

    @ApiModelProperty(value = "报警编码", position = 1)
    private String code;

    @ApiModelProperty(value = "报警描述", position = 2)
    private String desc;
}