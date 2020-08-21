package com.gnss.web.annotations;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Description: 分页接口注解</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2019/10/14
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ApiImplicitParams({
        @ApiImplicitParam(value = "页数(0..N)", name = "page", dataType = "int", paramType = "query"),
        @ApiImplicitParam(value = "每页大小", name = "size", dataType = "int", paramType = "query"),
        @ApiImplicitParam(value = "排序:属性名(,asc | desc)", name = "sort", allowMultiple = true, dataType = "string", paramType = "query")
})
public @interface ApiPageableParam {
}
