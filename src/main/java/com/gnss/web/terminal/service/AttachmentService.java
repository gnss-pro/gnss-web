package com.gnss.web.terminal.service;

import com.gnss.core.proto.AttachmentInfoProto;
import com.gnss.web.common.service.BaseService;
import com.gnss.web.info.domain.Terminal;
import com.gnss.web.terminal.dao.AttachmentRepository;
import com.gnss.web.terminal.domain.ActiveSafetyAlarm;
import com.gnss.web.terminal.domain.AttachmentInfo;
import com.gnss.web.terminal.mapper.AttachmentMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.List;

/**
 * <p>Description: 主动安全报警附件服务</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2018-12-28
 */
@Service
@Slf4j
public class AttachmentService extends BaseService<AttachmentInfo> {

    @Autowired
    private ActiveSafetyAlarmService activeSafetyAlarmService;

    @Autowired
    private AttachmentRepository attachmentRepository;

    /**
     * 保存附件信息
     *
     * @param attachmentInfoProto
     * @return
     * @throws Exception
     */
    public AttachmentInfo save(AttachmentInfoProto attachmentInfoProto) throws Exception {
        Terminal terminal = new Terminal();
        terminal.setId(attachmentInfoProto.getTerminalInfo().getTerminalId());
        Long activeSafetyAlarmId = Long.valueOf(attachmentInfoProto.getSafetyAlarmNum());
        ActiveSafetyAlarm activeSafetyAlarm = new ActiveSafetyAlarm();
        activeSafetyAlarm.setId(activeSafetyAlarmId);
        AttachmentInfo attachmentInfo = AttachmentMapper.fromAttachmentProto(attachmentInfoProto);
        attachmentInfo.setTerminal(terminal);
        attachmentInfo.setActiveSafetyAlarm(activeSafetyAlarm);
        //文件路径采用base64加密
        String base64FilePath = Base64.getEncoder().encodeToString(attachmentInfoProto.getFilePath().getBytes("utf-8"));
        attachmentInfo.setBase64FilePath(base64FilePath);
        attachmentInfo = super.save(attachmentInfo);
        //更新主动安全报警表的已完成上传附件数量
        activeSafetyAlarmService.updateCompletedCount(activeSafetyAlarmId);
        return attachmentInfo;
    }

    /**
     * 根据主动安全报警ID查询关联附件
     *
     * @param alarmId
     * @return
     */
    @Transactional(readOnly = true)
    public List<AttachmentInfo> findByAlarmId(Long alarmId) {
        return attachmentRepository.findByActiveSafetyAlarmOrderByIdAsc(new ActiveSafetyAlarm(alarmId));
    }

    /**
     * 根据主动安全报警ID查询附件路径
     *
     * @param safetyAlarmIdList
     * @return
     */
    @Transactional(readOnly = true)
    public List<String> findFilePathByAlarmIdIn(List<Long> safetyAlarmIdList) {
        return attachmentRepository.findFilePathByAlarmIdIn(safetyAlarmIdList);
    }

    /**
     * 根据主动安全报警ID批量删除附件
     *
     * @param safetyAlarmIdList
     */
    public int deleteByAlarmIdIn(List<Long> safetyAlarmIdList) {
        return attachmentRepository.deleteByAlarmIdIn(safetyAlarmIdList);
    }
}