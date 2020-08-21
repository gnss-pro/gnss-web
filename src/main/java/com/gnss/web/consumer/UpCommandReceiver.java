package com.gnss.web.consumer;

import com.github.benmanes.caffeine.cache.Cache;
import com.gnss.core.constants.CommandRequestTypeEnum;
import com.gnss.core.proto.CommandProto;
import com.gnss.web.constants.RabbitConstants;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * <p>Description: 上行指令订阅</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2019-1-18
 */
@Component
@Slf4j
@RabbitListener(queues = RabbitConstants.UP_COMMAND_QUEUE)
public class UpCommandReceiver {

    @Autowired
    private Cache<String, CompletableFuture<CommandProto>> downCommandCache;

    @RabbitHandler
    public void handleUpCommand(CommandProto upCommand, Channel channel, Message message) throws Exception {
        log.info("收到上行指令：{}", upCommand);
        try {
            //异步发送的响应结果
            if (upCommand.getRequestType() == CommandRequestTypeEnum.ASYNC) {
                return;
            }

            //同步发送的响应结果
            String operationId = upCommand.getOperationId();
            Optional.ofNullable(downCommandCache.getIfPresent(operationId))
                    .ifPresent(future -> {
                        //将响应结果返回给调用方
                        future.complete(upCommand);
                        //移除缓存记录
                        downCommandCache.invalidate(operationId);
                    });
        } catch (Exception e) {
            log.error("处理上行指令异常{}", upCommand, e);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
    }
}