package com.gnss.web.configuration;

import com.gnss.mqutil.configuration.BaseConfiguration;
import com.gnss.mqutil.constants.MqConstant;
import com.gnss.web.constants.RabbitConstants;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>Description: 中间件配置</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2019-1-18
 */
@Configuration
public class MiddlewareConfiguration extends BaseConfiguration {

    @Value("${spring.application.name}")
    private String appName;

    /**
     * 实时状态队列
     *
     * @return
     */
    @Bean
    public Queue terminalStatusQueue() {
        return new Queue(RabbitConstants.TERMINAL_STATUS_QUEUE);
    }

    /**
     * 多媒体文件队列
     *
     * @return
     */
    @Bean
    public Queue mediaFileQueue() {
        return new Queue(RabbitConstants.MEDIA_FILE_QUEUE);
    }

    /**
     * 主动安全报警附件队列
     *
     * @return
     */
    @Bean
    public Queue attachmentQueue() {
        return new Queue(RabbitConstants.ATTACHMENT_QUEUE);
    }

    /**
     * 上行指令队列
     *
     * @return
     */
    @Bean
    public Queue upCommandQueue() {
        return new Queue(RabbitConstants.UP_COMMAND_QUEUE);
    }

    /**
     * 上传数据队列
     *
     * @return
     */
    @Bean
    public Queue webUploadDataQueue() {
        return new Queue(RabbitConstants.UPLOAD_DATA_QUEUE);
    }

    /**
     * JT808日志队列
     *
     * @return
     */
    @Bean
    public Queue webJt808LogQueue() {
        return new Queue(RabbitConstants.JT808_LOG_QUEUE);
    }

    /**
     * JT809日志队列
     *
     * @return
     */
    @Bean
    public Queue webJt809LogQueue() {
        return new Queue(RabbitConstants.JT809_LOG_QUEUE);
    }

    /**
     * RPC队列
     *
     * @return
     */
    @Bean
    public Queue rpcQueue() {
        return new Queue(MqConstant.WEB_RPC_ROUTING_KEY, false, false, false);
    }

    @Bean
    Binding bindingTerminalStatusExchange(Queue terminalStatusQueue, TopicExchange terminalStatusExchange) {
        return BindingBuilder.bind(terminalStatusQueue).to(terminalStatusExchange).with(MqConstant.TERMINAL_STATUS_ROUTING_KEY);
    }

    @Bean
    Binding bindingMediaFileExchange(Queue mediaFileQueue, TopicExchange mediaFileExchange) {
        return BindingBuilder.bind(mediaFileQueue).to(mediaFileExchange).with(MqConstant.MEDIA_FILE_ROUTING_KEY);
    }

    @Bean
    Binding bindingAttachmentExchange(Queue attachmentQueue, TopicExchange attachmentExchange) {
        return BindingBuilder.bind(attachmentQueue).to(attachmentExchange).with(MqConstant.ATTACHMENT_ROUTING_KEY);
    }

    @Bean
    Binding bindingUpCommandExchange(Queue upCommandQueue, TopicExchange upCommandExchange) {
        String upCommandRoutingKey = String.format("#.%s.up.command", appName);
        return BindingBuilder.bind(upCommandQueue).to(upCommandExchange).with(upCommandRoutingKey);
    }

    @Bean
    Binding bindingUploadDataExchange(Queue webUploadDataQueue, TopicExchange webUploadDataExchange) {
        return BindingBuilder.bind(webUploadDataQueue).to(webUploadDataExchange).with(MqConstant.WEB_UPLOAD_DATA_ROUTING_KEY);
    }

    @Bean
    Binding bindingJt808LogExchange(Queue webJt808LogQueue, TopicExchange jt808LogExchange) {
        return BindingBuilder.bind(webJt808LogQueue).to(jt808LogExchange).with(MqConstant.JT808_LOG_ROUTING_KEY);
    }

    @Bean
    Binding bindingJt809LogExchange(Queue webJt809LogQueue, TopicExchange jt809LogExchange) {
        return BindingBuilder.bind(webJt809LogQueue).to(jt809LogExchange).with(MqConstant.JT809_LOG_ROUTING_KEY);
    }

    @Bean
    Binding bindingRpcExchange(Queue rpcQueue, TopicExchange rpcExchange) {
        return BindingBuilder.bind(rpcQueue).to(rpcExchange).with(MqConstant.WEB_RPC_ROUTING_KEY);
    }

}