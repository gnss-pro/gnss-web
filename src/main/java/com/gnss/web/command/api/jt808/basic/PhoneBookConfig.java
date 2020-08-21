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
 * <p>Description: 电话本联系人项</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2019/6/4
 */
@ApiModel("电话本联系人项")
@Getter
@Setter
public class PhoneBookConfig {

    @ApiModelProperty(value = "标志(1:呼入,2:呼出,3:呼入/呼出)", position = 1)
    @Range(min = 1, max = 3, message = "标志范围为1-3")
    private int flag;

    @ApiModelProperty(value = "电话号码", required = true, position = 2)
    @NotBlank(message = "电话号码不能为空")
    private String phoneNum;

    @ApiModelProperty(value = "联系人", position = 2)
    @NotBlank(message = "联系人不能为空")
    private String contact;

    @ApiModelProperty(value = "电话号码长度", hidden = true)
    private int phoneNumLen;

    @ApiModelProperty(value = "联系人长度", hidden = true)
    private int contactLen;

    @Override
    public String toString() {
        Map<String, Object> items = new LinkedHashMap<>();
        items.put("标志", flag);
        items.put("电话号码长度", phoneNumLen);
        items.put("电话号码", phoneNum);
        items.put("联系人长度", contactLen);
        items.put("联系人", contactLen);
        return items.toString();
    }
}