package com.gnss.web.terminal.dao;

import com.gnss.web.common.dao.BaseRepository;
import com.gnss.web.terminal.domain.ActiveSafetyAlarm;
import com.gnss.web.terminal.domain.AttachmentInfo;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * <p>Description: 主动安全附件信息DAO</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2019/10/14
 */
public interface AttachmentRepository extends BaseRepository<AttachmentInfo> {

    /**
     * 根据报警查询关联的附件列表
     *
     * @param activeSafetyAlarm
     * @return
     */
    List<AttachmentInfo> findByActiveSafetyAlarmOrderByIdAsc(ActiveSafetyAlarm activeSafetyAlarm);

    /**
     * 根据主动安全报警ID查询附件路径
     *
     * @param safetyAlarmIdList
     * @return
     */
    @Query(value = "SELECT file_path FROM terminal_attachment WHERE safety_alarm_id IN ?1", nativeQuery = true)
    List<String> findFilePathByAlarmIdIn(List<Long> safetyAlarmIdList);

    /**
     * 根据主动安全报警ID批量删除附件
     *
     * @param safetyAlarmIdList
     * @return
     */
    @Modifying
    @Query(value = "DELETE FROM terminal_attachment WHERE safety_alarm_id IN ?1", nativeQuery = true)
    int deleteByAlarmIdIn(List<Long> safetyAlarmIdList);
}