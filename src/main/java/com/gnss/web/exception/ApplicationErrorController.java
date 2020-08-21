package com.gnss.web.exception;

import com.gnss.web.common.api.ErrorInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p>Description: 异常处理接口</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2019-1-18
 */
@Controller
@RequestMapping("/error")
@EnableConfigurationProperties({ServerProperties.class})
public class ApplicationErrorController extends AbstractErrorController {

    private ErrorAttributes errorAttributes;

    @Autowired
    private ServerProperties serverProperties;

    @Autowired
    public ApplicationErrorController(ErrorAttributes errorAttributes) {
        super(errorAttributes);
        this.errorAttributes = errorAttributes;
    }

    @Override
    public String getErrorPath() {
        return this.serverProperties.getError().getPath();
    }

    @RequestMapping(produces = "text/html")
    public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> body = getErrorAttributes(request, isIncludeStackTrace(request));
        MappingJackson2JsonView view = new MappingJackson2JsonView();
        view.setAttributesMap(body);
        ModelAndView mav = new ModelAndView();
        mav.setView(view);
        return mav;
    }

    @RequestMapping
    @ResponseBody
    public ResponseEntity<ErrorInfo> error(HttpServletRequest request) {
        ErrorInfo errorInfo = getErrorInfo(request);
        return new ResponseEntity<>(errorInfo, HttpStatus.OK);
    }

    private ErrorInfo getErrorInfo(HttpServletRequest request) {
        Map<String, Object> body = getErrorAttributes(request, isIncludeStackTrace(request));
        String path = (String) body.get("path");
        String message = (String) body.get("message");
        Integer status = (Integer) body.get("status");
        ErrorInfo errorInfo = new ErrorInfo(status, message, path);
        return errorInfo;
    }

    private boolean isIncludeStackTrace(HttpServletRequest request) {
        ErrorProperties.IncludeStacktrace include = this.serverProperties.getError().getIncludeStacktrace();
        if (include == ErrorProperties.IncludeStacktrace.ALWAYS) {
            return true;
        }
        if (include == ErrorProperties.IncludeStacktrace.ON_TRACE_PARAM) {
            return getTraceParameter(request);
        }
        return false;
    }

}