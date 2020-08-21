package com.gnss.web.consumer;

import com.gnss.core.proto.AttachmentInfoProto;
import com.gnss.web.constants.RabbitConstants;
import com.gnss.web.terminal.service.AttachmentService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>Description: 主动安全附件订阅</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2019-1-18
 */
@Component
@Slf4j
@RabbitListener(queues = RabbitConstants.ATTACHMENT_QUEUE)
public class AttachmentReceiver {

    @Autowired
    private AttachmentService attachmentService;

    @RabbitHandler
    public void handleAttachment(AttachmentInfoProto attachmentInfoProto, Channel channel, Message message) throws Exception {
        try {
            attachmentService.save(attachmentInfoProto);
        } catch (Exception e) {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            log.error("保存主动安全报警附件异常:{}", attachmentInfoProto, e);
        }
    }
}