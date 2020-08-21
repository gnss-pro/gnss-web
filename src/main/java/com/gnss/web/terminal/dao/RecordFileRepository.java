package com.gnss.web.terminal.dao;

import com.gnss.web.common.dao.BaseRepository;
import com.gnss.web.terminal.domain.RecordFile;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecordFileRepository extends BaseRepository<RecordFile> {

    /**
     * 更新消息流水号
     *
     * @param id
     * @param msgFlowId
     */
    @Modifying
    @Query(value = "UPDATE terminal_record_file SET msg_flow_id = ?2  WHERE id = ?1", nativeQuery = true)
    void updateMessageFlowId(Long id, int msgFlowId);

    /**
     * 更新文件状态
     *
     * @param id
     * @param fileStatus
     */
    @Modifying
    @Query(value = "UPDATE terminal_record_file SET file_status = ?2  WHERE id = ?1", nativeQuery = true)
    void updateFileStatus(Long id, int fileStatus);

    /**
     * 批量删除录像文件
     *
     * @param idList
     * @return
     */
    @Modifying
    @Query(value = "DELETE FROM terminal_record_file WHERE id IN ?1", nativeQuery = true)
    int deleteByIdIn(List<Long> idList);

    /**
     * 根据ID列表查询录像文件
     *
     * @param idList
     * @return
     */
    List<RecordFile> findByIdIn(List<Long> idList);
}