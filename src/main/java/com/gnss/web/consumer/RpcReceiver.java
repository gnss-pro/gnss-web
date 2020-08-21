package com.gnss.web.consumer;

import com.gnss.core.constants.RpcEnum;
import com.gnss.core.proto.RpcProto;
import com.gnss.mqutil.constants.MqConstant;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * <p>Description: RabbitMQ RPC调用</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2019-1-18
 */
@Component
@Slf4j
@RabbitListener(queues = MqConstant.WEB_RPC_ROUTING_KEY)
public class RpcReceiver {

    @RabbitHandler
    public RpcProto handldRpc(RpcProto rpcRequest, Channel channel, Message message) throws Exception {
        RpcEnum rpcType = rpcRequest.getRpcType();
        String responseContent = null;
        try {
            log.info("收到RPC请求,请求类型:{},结果:{}", rpcType, responseContent);
        } catch (Exception e) {
            log.error("RPC请求异常:{}", rpcRequest, e);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            return null;
        }
        return rpcRequest;
    }
}