package com.gnss.web.info.api;

import com.gnss.core.constants.VehiclePlateColorEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>Description: 终端创建信息</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2019/10/14
 */
@ApiModel("终端创建信息")
@Data
public class TerminalCreateDTO {

    @ApiModelProperty(value = "终端手机号", required = true, position = 1)
    @NotBlank(message = "终端手机号不能为空")
    private String phoneNum;

    @ApiModelProperty(value = "终端号码", required = true, position = 2)
    @NotBlank(message = "终端号码不能为空")
    private String terminalNum;

    @ApiModelProperty(value = "车牌号码", required = true, position = 3)
    @NotBlank(message = "车牌号码不能为空")
    private String vehicleNum;

    @ApiModelProperty(value = "车牌颜色", required = true, position = 4)
    @NotNull(message = "车牌颜色不能为空")
    private VehiclePlateColorEnum vehiclePlateColor;
}