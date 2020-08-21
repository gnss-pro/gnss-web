package com.gnss.web.command.service;

import com.alibaba.fastjson.JSON;
import com.github.benmanes.caffeine.cache.Cache;
import com.gnss.core.constants.CommandRequestTypeEnum;
import com.gnss.core.constants.CommandSendResultEnum;
import com.gnss.core.constants.TerminalStatusEnum;
import com.gnss.core.constants.jt808.Jt808ReplyResultEnum;
import com.gnss.core.exception.ApplicationException;
import com.gnss.core.model.DownCommandInfo;
import com.gnss.core.model.jt808.CommonReplyParam;
import com.gnss.core.proto.CommandProto;
import com.gnss.core.proto.TerminalProto;
import com.gnss.core.proto.TerminalStatusProto;
import com.gnss.core.service.IDownCommandMessage;
import com.gnss.mqutil.event.DownCommandRegister;
import com.gnss.mqutil.producer.RabbitMessageSender;
import com.gnss.web.command.api.CommandRequestDTO;
import com.gnss.web.command.api.CommandResultDTO;
import com.gnss.web.terminal.service.TerminalStatusService;
import com.gnss.web.utils.CommonIdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * <p>Description: 指令操作服务</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2019-1-18
 */
@Service
@Slf4j
public class CommandOperationService {

    private static final String JT808_COMMAND_0001 = "0001";

    @Value("${spring.application.name}")
    private String appName;

    @Autowired
    private TerminalStatusService terminalStatusService;

    @Autowired
    private RabbitMessageSender messageSender;

    @Autowired
    private DownCommandRegister downCommandRegister;

    @Autowired
    private Cache<String, CompletableFuture<CommandProto>> downCommandCache;

    /**
     * 发送带泛型的指令
     *
     * @param commandRequestDTO
     * @param <T>
     * @return
     */
    public <T extends IDownCommandMessage> CommandResultDTO sendCommandEntity(CommandRequestDTO<T> commandRequestDTO) {
        String paramsDesc = JSON.toJSONString(commandRequestDTO.getParamsEntity());
        Class paramsEntityClass = commandRequestDTO.getParamsEntityClass();
        //查询指令有无注册
        DownCommandInfo downCommandInfo = downCommandRegister.getDownCommandInfo(paramsEntityClass);
        if (downCommandInfo == null) {
            log.error("指令发送失败,不支持的指令,终端ID:{},参数实体:{},指令参数:{}", commandRequestDTO.getTerminalId(), paramsEntityClass.getName(), paramsDesc);
            CommandResultDTO result = buildCommandResponse(null, downCommandInfo, CommandSendResultEnum.NOT_SUPPORT);
            result.setTerminalId(commandRequestDTO.getTerminalId().toString());
            return result;
        }
        //查询终端是否在线
        Long terminalId = commandRequestDTO.getTerminalId();
        TerminalStatusProto terminalStatus = terminalStatusService.getLastStatus(terminalId);
        if (terminalStatus == null) {
            CommandResultDTO result = buildCommandResponse(null, downCommandInfo, CommandSendResultEnum.TERMINAL_OFFLINE);
            result.setTerminalId(commandRequestDTO.getTerminalId().toString());
            return result;
        } else if (terminalStatus.getTerminalStatus() == TerminalStatusEnum.OFFLINE) {
            log.error("指令发送失败,终端不在线,终端ID:{},指令ID:{},指令参数:{}", terminalId, downCommandInfo.getDownCommandId(), paramsDesc);
            return buildCommandResponse(terminalStatus.getTerminalInfo(), downCommandInfo, CommandSendResultEnum.TERMINAL_OFFLINE);
        }
        //发送指令
        return sendCommand(terminalStatus, commandRequestDTO, downCommandInfo, paramsDesc);
    }

    /**
     * 发送指令
     *
     * @param terminalStatus
     * @param commandRequestDTO
     * @param downCommandInfo
     * @param paramsDesc
     * @return
     */
    private CommandResultDTO sendCommand(TerminalStatusProto terminalStatus, CommandRequestDTO commandRequestDTO, DownCommandInfo downCommandInfo, String paramsDesc) {
        TerminalProto terminalInfo = terminalStatus.getTerminalInfo();
        String downCommandId = downCommandInfo.getDownCommandId();
        //构造指令发送结果
        CommandResultDTO resultDTO = buildCommandResponse(terminalInfo, downCommandInfo, CommandSendResultEnum.FAILED);
        try {
            //获取透传的消息体,发送指令
            byte[] msgBody = commandRequestDTO.getParamsEntity().buildMessageBody(terminalInfo);
            //构造MQ传输指令
            paramsDesc = commandRequestDTO.getParamsEntity().toString();
            CommandProto commandProto = buildCommandProto(terminalStatus, commandRequestDTO, downCommandInfo, paramsDesc, msgBody);
            messageSender.sendJsonDownCommand(commandProto);

            //同步方式需要等待指令应答结果,同步方式直接返回成功给客户端
            if (commandRequestDTO.getRequestType() == CommandRequestTypeEnum.SYNC) {
                log.info("同步指令发送成功到MQ,终端ID:{},指令ID:{},指令参数:{}", terminalInfo.getTerminalStrId(), downCommandId, paramsDesc);
                waitForResult(resultDTO, commandProto);
            } else {
                log.info("异步指令发送成功到MQ,终端ID:{},指令ID:{},指令参数:{}", terminalInfo.getTerminalStrId(), downCommandId, paramsDesc);
                resultDTO.setSendResult(CommandSendResultEnum.SUCCESS);
                resultDTO.setResultDesc(resultDTO.getSendResult().getDesc());
            }
        } catch (ApplicationException e) {
            log.error("指令发送失败,终端ID:{},指令ID:{},指令参数:{}", terminalInfo.getTerminalStrId(), downCommandId, paramsDesc, e);
            resultDTO.setSendResult(CommandSendResultEnum.INTERNAL_SERVER_ERROR);
            resultDTO.setResultDesc(e.getMessage());
        } catch (Exception e) {
            log.error("指令发送失败,终端ID:{},指令ID:{},指令参数:{}", terminalInfo.getTerminalStrId(), downCommandId, paramsDesc, e);
            resultDTO.setSendResult(CommandSendResultEnum.INTERNAL_SERVER_ERROR);
            resultDTO.setResultDesc(resultDTO.getSendResult().getDesc());
        }
        return resultDTO;
    }

    /**
     * 同步方式需要等待终端应答
     *
     * @param commandProto
     * @return
     */
    private CommandResultDTO waitForResult(CommandResultDTO resultDTO, CommandProto commandProto) {
        //注册指令监听事件
        CompletableFuture<CommandProto> future = new CompletableFuture<>();
        String terminalId = resultDTO.getTerminalId();
        String terminalNum = resultDTO.getTerminalNum();
        String downCommandId = commandProto.getDownCommandId();
        downCommandCache.put(commandProto.getOperationId(), future);

        String paramSend = commandProto.getParamsDesc();
        try {
            //等待应答结果
            CommandProto result = future.get(commandProto.getTimeout(), TimeUnit.MILLISECONDS);
            resultDTO.setResponseParams(result.getResponseParams());
            //处理JT808通用应答
            if (Objects.equals(JT808_COMMAND_0001, commandProto.getRespCommandId())) {
                handleJt808CommonReply(resultDTO, result.getResponseParams());
            } else {
                resultDTO.setSendResult(result.getSendResult());
                resultDTO.setResultDesc(result.getSendResult().getDesc());
            }
            resultDTO.setSendResult(result.getSendResult());
            log.info("收到指令应答,终端ID:{},终端号:{},指令ID:{},指令参数:{},应答结果:{}", terminalId, terminalNum, downCommandId, paramSend, result);
        } catch (TimeoutException e) {
            resultDTO.setSendResult(CommandSendResultEnum.TIMEOUT);
            resultDTO.setResultDesc(resultDTO.getSendResult().getDesc());
            log.error("等待指令应答超时,终端ID:{},终端号:{},指令ID:{},指令参数:{},等待时间:{}", terminalId, terminalNum, downCommandId, paramSend, commandProto.getTimeout());
        } catch (Exception e) {
            resultDTO.setSendResult(CommandSendResultEnum.INTERNAL_SERVER_ERROR);
            resultDTO.setResultDesc(resultDTO.getSendResult().getDesc());
            log.error("等待指令应答异常,终端ID:{},终端号:{},指令类型:{},指令参数:{}", terminalId, terminalNum, downCommandId, paramSend, e);
        }
        return resultDTO;
    }

    /**
     * 构建指令发送结果
     *
     * @param terminalInfo
     * @param downCommandInfo
     * @param sendResult
     * @return
     */
    private CommandResultDTO buildCommandResponse(TerminalProto terminalInfo, DownCommandInfo downCommandInfo, CommandSendResultEnum sendResult) {
        CommandResultDTO result = new CommandResultDTO();
        if (terminalInfo != null) {
            result.setTerminalId(terminalInfo.getTerminalStrId());
            result.setTerminalNum(terminalInfo.getTerminalNum());
            result.setSimCode(terminalInfo.getTerminalSimCode());
            result.setVehicleNum(terminalInfo.getVehicleNum());
        }
        if (downCommandInfo != null) {
            result.setDownCommandId(downCommandInfo.getDownCommandId());
            result.setDownCommandDesc(downCommandInfo.getDesc());
            result.setRespCommandId(downCommandInfo.getRespCommandId());
        }
        result.setSendResult(sendResult);
        if (sendResult != null) {
            result.setResultDesc(sendResult.getDesc());
        }
        return result;
    }

    /**
     * 构造MQ传输指令
     * @param terminalStatus
     * @param commandRequestDTO
     * @param downCommandInfo
     * @param paramsDesc
     * @param msgBody
     * @return
     */
    public CommandProto buildCommandProto(TerminalStatusProto terminalStatus, CommandRequestDTO commandRequestDTO, DownCommandInfo downCommandInfo, String paramsDesc, byte[] msgBody) {
        CommandProto commandProto = new CommandProto();
        commandProto.setOperationId(String.valueOf(CommonIdGenerator.generate()));
        commandProto.setTerminalInfo(terminalStatus.getTerminalInfo());
        commandProto.setRequestType(commandRequestDTO.getRequestType());
        commandProto.setDownCommandId(downCommandInfo.getDownCommandId());
        commandProto.setDownCommandDesc(downCommandInfo.getDesc());
        commandProto.setParamsDesc(paramsDesc);
        commandProto.setRespCommandId(downCommandInfo.getRespCommandId());
        commandProto.setFromNode(appName);
        commandProto.setToNode(terminalStatus.getNodeName());
        commandProto.setStartTime(System.currentTimeMillis());
        commandProto.setTimeout(commandRequestDTO.getResponseTimeout());
        commandProto.setWholePacket(false);
        commandProto.setMessageBody(msgBody);
        return commandProto;
    }

    /**
     * 处理JT808通用应答
     *
     * @param resultDTO
     * @param responseJson
     */
    private void handleJt808CommonReply(CommandResultDTO resultDTO, String responseJson) {
        //转换成JT808的通用应答0x0001
        CommonReplyParam commonReplyParam = JSON.parseObject(responseJson, CommonReplyParam.class);
        Jt808ReplyResultEnum jt808ReplyResultEnum = Jt808ReplyResultEnum.fromValue(commonReplyParam.getResult());
        //JT808通用应答结果(0:成功/确认;1:失败;2:消息有误;3:不支持)
        if (jt808ReplyResultEnum == Jt808ReplyResultEnum.SUCCESS) {
            resultDTO.setSendResult(CommandSendResultEnum.SUCCESS);
        } else {
            resultDTO.setSendResult(CommandSendResultEnum.FAILED);
        }
        resultDTO.setResultDesc(jt808ReplyResultEnum.getDesc());
    }
}