package com.gnss.web.terminal.service;

import com.gnss.core.constants.CommandRequestTypeEnum;
import com.gnss.core.constants.RpcEnum;
import com.gnss.core.proto.AlarmFlagProto;
import com.gnss.core.proto.TerminalProto;
import com.gnss.mqutil.producer.RabbitMessageSender;
import com.gnss.web.command.api.CommandRequestDTO;
import com.gnss.web.command.api.CommandResultDTO;
import com.gnss.web.command.api.jt808.safety.Command9208Param;
import com.gnss.web.command.service.CommandOperationService;
import com.gnss.web.common.service.BaseService;
import com.gnss.web.terminal.dao.ActiveSafetyAlarmRepository;
import com.gnss.web.terminal.domain.ActiveSafetyAlarm;
import com.gnss.web.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>Description: 主动安全报警服务</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2019-04-04
 */
@Service
@Slf4j
public class ActiveSafetyAlarmService extends BaseService<ActiveSafetyAlarm> {

    @Autowired
    private ActiveSafetyAlarmRepository activeSafetyAlarmRepository;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private RabbitMessageSender messageSender;

    @Autowired
    private CommandOperationService commandOperationService;

    /**
     * 更新主动安全报警表已完成附件上传数量
     *
     * @param safetyAlarmId
     */
    public void updateCompletedCount(Long safetyAlarmId) {
        activeSafetyAlarmRepository.updateCompletedCount(safetyAlarmId);
    }

    /**
     * 发送指令通知终端上传附件
     *
     * @param terminalInfo
     * @param activeSafetyAlarm
     * @param alarmFlag
     */
    public void notifyUploadAttachment(TerminalProto terminalInfo, ActiveSafetyAlarm activeSafetyAlarm, AlarmFlagProto alarmFlag) {
        Command9208Param command9208Param = new Command9208Param();
        command9208Param.setAlarmFlagArr(alarmFlag.getSourceData());
        command9208Param.setAlarmNum(activeSafetyAlarm.getId().toString());
        CommandRequestDTO<Command9208Param> commandRequestDTO = new CommandRequestDTO<>();
        commandRequestDTO.setTerminalId(terminalInfo.getTerminalId());
        commandRequestDTO.setRequestType(CommandRequestTypeEnum.ASYNC);
        commandRequestDTO.setParamsEntity(command9208Param);
        CommandResultDTO resultDTO = commandOperationService.sendCommandEntity(commandRequestDTO);
        log.info("发送报警附件上传指令(0x9208),终端信息:{},报警编号:{},发送结果:{}", terminalInfo, activeSafetyAlarm.getId(), resultDTO);
    }

    /**
     * 批量删除主动安全报警
     *
     * @param idList
     */
    public int batchDelete(List<Long> idList) throws Exception {
        List<String> filePathList = attachmentService.findFilePathByAlarmIdIn(idList);
        if (filePathList.isEmpty()) {
            int alarmCnt = activeSafetyAlarmRepository.deleteByIdIn(idList);
            log.info("批量删除主动安全报警,ID列表:{},报警删除数量:{}", idList, alarmCnt);
            return alarmCnt;
        }
        //删除关联的附件记录
        int attachmentCnt = attachmentService.deleteByAlarmIdIn(idList);
        int alarmCnt = activeSafetyAlarmRepository.deleteByIdIn(idList);
        //删除文件服务器的文件
        Boolean deleteFileResult = FileUtil.sendDeleteFileCommand(messageSender, RpcEnum.DELETE_ATTACHMENT, filePathList);
        log.info("批量删除主动安全报警,ID列表:{},报警删除数量:{},附件删除数量:{},文件删除结果:{}", idList, alarmCnt, attachmentCnt, deleteFileResult);
        return alarmCnt;
    }
}