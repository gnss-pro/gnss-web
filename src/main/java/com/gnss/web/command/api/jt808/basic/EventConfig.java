package com.gnss.web.command.api.jt808.basic;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>Description: 事件设置事件项</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2020/6/4
 */
@ApiModel("事件设置事件项")
@Getter
@Setter
public class EventConfig {

    @ApiModelProperty(value = "事件ID(范围1-127)", required = true, position = 1)
    @Range(min = 1, max = 127, message = "事件ID范围为1-127")
    private int eventId;

    @ApiModelProperty(value = "事件内容", position = 2)
    private String eventContent;

    @ApiModelProperty(value = "事件内容长度", hidden = true)
    private int eventContentLen;

    @Override
    public String toString() {
        Map<String, Object> items = new LinkedHashMap<>();
        items.put("事件ID", eventId);
        items.put("事件内容长度", eventContentLen);
        items.put("事件内容", eventContent);
        return items.toString();
    }
}