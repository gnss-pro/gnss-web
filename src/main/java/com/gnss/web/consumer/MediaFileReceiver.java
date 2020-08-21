package com.gnss.web.consumer;

import com.gnss.core.proto.MediaFileProto;
import com.gnss.web.constants.RabbitConstants;
import com.gnss.web.terminal.service.MediaFileService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>Description: JT808多媒体文件(拍照等)订阅</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2019-1-18
 */
@Component
@Slf4j
@RabbitListener(queues = RabbitConstants.MEDIA_FILE_QUEUE)
public class MediaFileReceiver {

    @Autowired
    private MediaFileService mediaFileService;

    @RabbitHandler
    public void handleMediaFile(MediaFileProto mediaFileProto, Channel channel, Message message) throws Exception {
        try {
            mediaFileService.save(mediaFileProto);
        } catch (Exception e) {
            log.error("保存终端多媒体文件失败,终端号:{}", mediaFileProto.getTerminalInfo().getTerminalNum(), e);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
    }
}