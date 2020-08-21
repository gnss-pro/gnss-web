package com.gnss.web.consumer;

import com.gnss.core.proto.Jt808LogProto;
import com.gnss.web.constants.RabbitConstants;
import com.gnss.web.log.service.Jt808LogService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>Description: JT808日志订阅</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2019-1-18
 */
@Component
@Slf4j
@RabbitListener(queues = RabbitConstants.JT808_LOG_QUEUE)
public class Jt808LogReceiver {

    @Autowired
    private Jt808LogService jt808LogService;

    @RabbitHandler
    public void handleJt808Log(Jt808LogProto jt808LogProto, Channel channel, Message message) throws Exception {
        try {
            jt808LogService.save(jt808LogProto);
        } catch (Exception e) {
            log.error("保存JT808日志异常:{}", jt808LogProto, e);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
    }
}