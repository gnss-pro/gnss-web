package com.gnss.web.command.controller;

import com.alibaba.fastjson.JSON;
import com.gnss.core.constants.MessageSendResultEnum;
import com.gnss.core.constants.RpcEnum;
import com.gnss.core.exception.ApplicationException;
import com.gnss.core.model.config.Jt808ServerConfig;
import com.gnss.core.proto.RpcProto;
import com.gnss.core.rpc.Jt808AppCommand;
import com.gnss.mqutil.producer.RabbitMessageSender;
import com.gnss.web.common.api.ApiResultDTO;
import com.gnss.web.common.constant.ResultCodeEnum;
import com.gnss.web.global.service.CacheService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>Description: JT808网关操作接口</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author menghuan
 * @version 1.0.1
 * @date 2019/2/14
 */
@Api(tags = "JT808网关操作")
@RestController
@RequestMapping("/api/v1/commands/jt808/ctrl")
@Slf4j
public class Jt808AppController {

    @Autowired
    private RabbitMessageSender messageSender;

    @Autowired
    private CacheService cacheService;

    @ApiOperation("设置是否上传日志")
    @PostMapping("/uploadLog/{isUploadLog}")
    public ApiResultDTO<Jt808ServerConfig> uploadLogSetting(@ApiParam("是否上传日志") @PathVariable boolean isUploadLog) throws ApplicationException {
        Jt808AppCommand jt808AppCommand = new Jt808AppCommand();
        jt808AppCommand.setUploadLog(isUploadLog);
        RpcProto rpcRequest = new RpcProto(RpcEnum.APP_CTRL, jt808AppCommand);
        try {
            RpcProto rpcResponse = messageSender.sendJt808Rpc(rpcRequest);
            if (rpcResponse == null) {
                log.error("设置JT808是否上传日志失败,网关未应答,是否上传:{}", isUploadLog);
                return new ApiResultDTO<>(ResultCodeEnum.FAIL, "网关未应答");
            }
            if (rpcResponse.getRpcResult() != MessageSendResultEnum.SUCCESS) {
                log.error("设置JT808是否上传日志失败,结果:{},原因:{}", rpcResponse.getRpcResult(), rpcResponse.getErrorMsg());
                return new ApiResultDTO<>(ResultCodeEnum.FAIL, rpcResponse.getErrorMsg());
            }
            //返回更新后的服务器信息
            Jt808ServerConfig serverConfig = JSON.parseObject(rpcResponse.getContent(), Jt808ServerConfig.class);
            cacheService.updateServerInfo(serverConfig);
            log.info("设置JT808是否上传日志成功,是否上传:{}", isUploadLog);
            return new ApiResultDTO<>(serverConfig);
        } catch (Exception e) {
            log.error("设置JT808是否上传日志异常,是否上传:{}", isUploadLog, e);
            throw new ApplicationException(e.getMessage(), e);
        }
    }

}