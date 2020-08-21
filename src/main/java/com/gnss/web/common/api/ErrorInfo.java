package com.gnss.web.common.api;

import lombok.Data;

import java.util.Date;

/**
 * <p>Description: 错误信息</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2019-9-14
 */
@Data
public class ErrorInfo {
    public static final Integer OK = 200;
    public static final Integer ERROR = 500;
    public static final Integer NO_PERMISSION = 10001;
    public static final Integer NO_SESSION = 10002;
    public static final Integer NOT_FOUND = 404;

    private Integer code = OK;

    private String msg;

    private Object data;
    private String path;
    private Date timestamp;

    public ErrorInfo() {

    }

    public ErrorInfo(Object data) {
        this.data = data;
    }

    public ErrorInfo(Integer code, String msg, String path) {
        this.code = code;
        this.msg = msg;
        this.path = path;
        this.timestamp = new Date();
    }

    public ErrorInfo(Integer code, String msg) {
        this(code, msg, null);
    }

}
