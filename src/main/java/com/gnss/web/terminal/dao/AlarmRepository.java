package com.gnss.web.terminal.dao;

import com.gnss.core.constants.AlarmTypeEnum;
import com.gnss.web.common.dao.BaseRepository;
import com.gnss.web.info.domain.Terminal;
import com.gnss.web.terminal.domain.Alarm;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AlarmRepository extends BaseRepository<Alarm> {

    /**
     * 根据报警列表和报警类型查询未结束的报警
     *
     * @param terminal
     * @param alarmTypeList
     * @return
     */
    List<Alarm> findByTerminalAndEndTimeIsNullAndAlarmTypeIn(Terminal terminal, List<AlarmTypeEnum> alarmTypeList);

    /**
     * 根据ID列表批量删除
     *
     * @param idList
     * @return
     */
    @Modifying
    @Query(value = "DELETE FROM terminal_alarm WHERE id IN ?1", nativeQuery = true)
    int deleteByIdIn(List<Long> idList);
}