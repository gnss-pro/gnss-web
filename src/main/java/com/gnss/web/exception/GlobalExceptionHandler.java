package com.gnss.web.exception;

import com.gnss.core.exception.ApplicationException;
import com.gnss.web.common.api.ApiResultDTO;
import com.gnss.web.common.constant.ResultCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>Description: 全局异常处理</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2019-1-18
 */
@ControllerAdvice
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = BindException.class)
    public ApiResultDTO<?> handleBindException(HttpServletRequest request, BindException e) {
        printException(request, e);
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        return createResultDTO(fieldErrors);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ApiResultDTO<?> handleMethodArgumentNotValid(HttpServletRequest request, MethodArgumentNotValidException e) {
        printException(request, e);
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        return createResultDTO(fieldErrors);
    }

    @ExceptionHandler(value = ApplicationException.class)
    public ApiResultDTO<?> handleApplicationException(HttpServletRequest request, ApplicationException e) {
        printException(request, e);
        return new ApiResultDTO<>(e);
    }

    @ExceptionHandler(value = Exception.class)
    public ApiResultDTO<?> handleException(HttpServletRequest request, Exception e) {
        printException(request, e);
        return new ApiResultDTO<>(e);
    }

    /**
     * 打印异常信息
     *
     * @param request
     * @param e
     */
    private void printException(HttpServletRequest request, Exception e) {
        String uri = request.getRequestURI();
        String httpMethod = request.getMethod();
        String ip = request.getRemoteAddr();
        log.error("全局异常处理,uri:{},httpMethod:{},ip:{}", uri, httpMethod, ip, e);
    }

    /**
     * 创建统一响应结果
     *
     * @param fieldErrors
     * @return
     */
    private ApiResultDTO<?> createResultDTO(List<FieldError> fieldErrors) {
        List<String> errorMessages = fieldErrors.stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());
        String errorMessage = String.join(";", errorMessages);
        ApiResultDTO<?> apiResultDTO = new ApiResultDTO<>(ResultCodeEnum.FAIL, errorMessage);
        return apiResultDTO;
    }

}
