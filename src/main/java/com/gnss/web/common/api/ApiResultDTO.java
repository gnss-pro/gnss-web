package com.gnss.web.common.api;

import com.gnss.web.common.constant.ResultCodeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>Description: 统一响应结果</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2019-1-18
 */
@ApiModel("响应结果")
@Data
public class ApiResultDTO<T> implements Serializable {

    private static final ApiResultDTO<?> EMPTY = new ApiResultDTO<>();

    @ApiModelProperty(value = "响应代码", position = 1)
    private int code;

    @ApiModelProperty(value = "响应消息", position = 2)
    private String msg;

    @ApiModelProperty(value = "数据", position = 3)
    private T data;

    public ApiResultDTO() {
        super();
    }

    public ApiResultDTO(T data) {
        super();
        this.data = data;
    }

    public ApiResultDTO(Throwable e) {
        super();
        this.msg = e.getMessage();
        this.code = ResultCodeEnum.FAIL.getCode();
    }

    public ApiResultDTO(ResultCodeEnum resultCodeEnum, String msg) {
        super();
        this.code = resultCodeEnum.getCode();
        this.msg = msg;
    }

    public static ApiResultDTO success() {
        ApiResultDTO apiResultDTO = new ApiResultDTO();
        apiResultDTO.setCode(ResultCodeEnum.SUCCESS.getCode());
        return apiResultDTO;
    }

    public static ApiResultDTO success(Object data) {
        ApiResultDTO apiResultDTO = new ApiResultDTO();
        apiResultDTO.setCode(ResultCodeEnum.SUCCESS.getCode());
        apiResultDTO.setData(data);
        return apiResultDTO;
    }

    public static ApiResultDTO failure(Object data) {
        return failure(data, ResultCodeEnum.FAIL.getDesc());
    }

    public static ApiResultDTO failure(Object data, String errorMsg) {
        ApiResultDTO apiResultDTO = new ApiResultDTO();
        apiResultDTO.setCode(ResultCodeEnum.FAIL.getCode());
        apiResultDTO.setData(data);
        apiResultDTO.setMsg(errorMsg);
        return apiResultDTO;
    }

    public static ApiResultDTO exception(String errorMsg) {
        ApiResultDTO apiResultDTO = new ApiResultDTO();
        apiResultDTO.setCode(ResultCodeEnum.EXCEPTION.getCode());
        apiResultDTO.setMsg(errorMsg);
        return apiResultDTO;
    }

    @SuppressWarnings("unchecked")
    public static final <T> ApiResultDTO<T> empty() {
        return (ApiResultDTO<T>) EMPTY;
    }

}