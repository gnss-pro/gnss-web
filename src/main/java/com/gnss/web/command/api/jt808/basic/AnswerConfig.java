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
 * <p>Description: 提问候选答案</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2019/6/4
 */
@ApiModel("提问候选答案")
@Getter
@Setter
public class AnswerConfig {

    @ApiModelProperty(value = "答案ID(范围1-127)", position = 1)
    @Range(min = 1, max = 127, message = "答案ID范围为1-127")
    private int answerId;

    @ApiModelProperty(value = "答案内容", required = true, position = 2)
    @NotBlank(message = "答案内容不能为空")
    private String answerContent;

    @ApiModelProperty(value = "答案内容长度", hidden = true)
    private int answerContentLen;

    @Override
    public String toString() {
        Map<String, Object> items = new LinkedHashMap<>();
        items.put("答案ID", answerId);
        items.put("答案内容长度", answerContentLen);
        items.put("答案内容", answerContent);
        return items.toString();
    }
}