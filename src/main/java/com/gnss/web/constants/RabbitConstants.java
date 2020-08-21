package com.gnss.web.constants;

/**
 * <p>Description: 消息队列常量</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2019-1-18
 */
public class RabbitConstants {

    private RabbitConstants() {
    }

    /**
     * RabbitMQ终端状态队列
     */
    public static final String TERMINAL_STATUS_QUEUE = "web-terminal-status";

    /**
     * RabbitMQ多媒体队列
     */
    public static final String MEDIA_FILE_QUEUE = "web-media-file";

    /**
     * RabbitMQ附件队列
     */
    public static final String ATTACHMENT_QUEUE = "web-attachment";

    /**
     * RabbitMQ指令应答队列
     */
    public static final String UP_COMMAND_QUEUE = "web-up-command";

    /**
     * RabbitMQ终端上传数据队列
     */
    public static final String UPLOAD_DATA_QUEUE = "web-upload-data";

    /**
     * RabbitMQ JT808日志队列
     */
    public static final String JT808_LOG_QUEUE = "web-jt808-log";

    /**
     * RabbitMQ JT809日志队列
     */
    public static final String JT809_LOG_QUEUE = "web-jt809-log";
}