package com.gnss.web.utils;

import com.alibaba.fastjson.JSON;
import com.gnss.core.constants.MessageSendResultEnum;
import com.gnss.core.constants.RpcEnum;
import com.gnss.core.exception.ApplicationException;
import com.gnss.core.proto.RpcProto;
import com.gnss.mqutil.producer.RabbitMessageSender;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * <p>Description: 文件工具</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2019/8/14
 */
@Slf4j
public class FileUtil {

    private FileUtil() {
    }

    /**
     * 发送指令到文件服务器删除文件
     *
     * @param messageSender
     * @param rpcType
     * @param filePathList
     * @return
     * @throws Exception
     */
    public static Boolean sendDeleteFileCommand(RabbitMessageSender messageSender, RpcEnum rpcType, List<String> filePathList) throws Exception {
        RpcProto rpcRequest = new RpcProto(rpcType, filePathList);
        RpcProto rpcResponse = messageSender.sendFileServerRpc(rpcRequest);
        if (rpcResponse == null) {
            throw new ApplicationException("删除文件服务器文件失败");
        }
        if (rpcResponse.getRpcResult() != MessageSendResultEnum.SUCCESS) {
            log.error("删除文件服务器文件失败,结果:{},原因:{}", rpcResponse.getRpcResult(), rpcResponse.getErrorMsg());
            throw new ApplicationException("删除文件服务器文件失败");
        }
        Boolean result = JSON.parseObject(rpcResponse.getContent(), Boolean.class);
        return result;
    }
}