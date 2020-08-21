package com.gnss.web.terminal.dao;

import com.gnss.web.common.dao.BaseRepository;
import com.gnss.web.terminal.domain.MediaFile;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MediaFileRepository extends BaseRepository<MediaFile> {

    /**
     * 根据ID列表批量删除
     *
     * @param idList
     * @return
     */
    @Modifying
    @Query(value = "DELETE FROM terminal_media_file WHERE id IN ?1", nativeQuery = true)
    int deleteByIdIn(List<Long> idList);
}