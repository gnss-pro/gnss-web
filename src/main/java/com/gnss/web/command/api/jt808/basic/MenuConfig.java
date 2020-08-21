package com.gnss.web.command.api.jt808.basic;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>Description: 信息点播信息项</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2020/6/4
 */
@ApiModel("信息点播信息项")
@Getter
@Setter
public class MenuConfig {

    @ApiModelProperty(value = "信息类型(范围1-127)", required = true, position = 1)
    @Range(min = 1, max = 127, message = "信息类型范围为1-127")
    private int infoType;

    @ApiModelProperty(value = "信息名称", required = true, position = 2)
    @NotBlank(message = "信息名称不能为空")
    private String infoContent;

    @ApiModelProperty(value = "信息名称长度", hidden = true)
    private int infoContentLen;

    @Override
    public String toString() {
        Map<String, Object> items = new LinkedHashMap<>();
        items.put("信息类型", infoType);
        items.put("信息名称长度", infoContentLen);
        items.put("信息名称", infoContent);
        return items.toString();
    }
}