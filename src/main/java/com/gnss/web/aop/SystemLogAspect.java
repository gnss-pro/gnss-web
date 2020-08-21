package com.gnss.web.aop;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * <p>Description: 系统异常日志AOP</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2019-06-23
 */
@Aspect
@Slf4j
@Component
public class SystemLogAspect {

    @Pointcut("execution(public * com.gnss.web.*.*.controller..*.*(..))")
    public void webLog() {
    }

    @Around("webLog()")
    public Object interceptor(ProceedingJoinPoint pjp) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String uri = request.getRequestURI();
        String httpMethod = request.getMethod();
        String ip = request.getRemoteAddr();
        String classMethod = pjp.getSignature().getDeclaringTypeName() + "." + pjp.getSignature().getName();
        String argsJsonStr = JSON.toJSONString(Arrays.toString(pjp.getArgs()));

        log.debug("请求URI: {}", uri);
        log.debug("请求HTTP_METHOD: {}", httpMethod);
        log.debug("请求IP: {}", ip);
        log.debug("请求CLASS_METHOD: {}", classMethod);
        log.debug("请求ARGS: {}", argsJsonStr);

        log.debug("请求开始,{}", classMethod);
        Object result;
        long beginTime = System.currentTimeMillis();
        try {
            result = pjp.proceed();
        } catch (Throwable e) {
            log.error("执行方法{}发生异常: ", classMethod, e);
            throw e;
        }

        long costMs = System.currentTimeMillis() - beginTime;
        log.debug("请求结束,{},耗时:{}ms", classMethod, costMs);
        return result;
    }
}