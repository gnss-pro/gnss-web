package com.gnss.web.terminal.dao;

import com.gnss.web.common.dao.BaseRepository;
import com.gnss.web.terminal.domain.ActiveSafetyAlarm;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ActiveSafetyAlarmRepository extends BaseRepository<ActiveSafetyAlarm> {

    /**
     * 更新主动安全报警表已完成附件上传数量
     *
     * @param safetyAlarmId
     */
    @Modifying
    @Query(value = "UPDATE terminal_safety_alarm SET completed_count = completed_count + 1  WHERE id = ?1", nativeQuery = true)
    void updateCompletedCount(Long safetyAlarmId);

    /**
     * 批量删除主动安全报警
     *
     * @param idList
     * @return
     */
    @Modifying
    @Query(value = "DELETE FROM terminal_safety_alarm WHERE id IN ?1", nativeQuery = true)
    int deleteByIdIn(List<Long> idList);
}