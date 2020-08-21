package com.gnss.web.terminal.domain;

import com.gnss.core.constants.jt1078.AvItemTypeEnum;
import com.gnss.web.common.domain.BaseEntity;
import com.gnss.web.constants.FileStorageEnum;
import com.gnss.web.info.domain.Terminal;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * <p>Description: JT1078录像文件</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2019/8/11
 */
@Entity
@Table(name = "terminal_record_file", indexes = {
        @Index(name = "record_file_idx_created_date", columnList = "createdDate"),
        @Index(name = "record_file_idx_last_modified_date", columnList = "lastModifiedDate")})
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@ToString(exclude = {"terminal"})
public class RecordFile extends BaseEntity {

    /**
     * 关联终端
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "terminal_id", nullable = false)
    private Terminal terminal;

    /**
     * 存储位置
     */
    @Enumerated
    @Column(nullable = false)
    private FileStorageEnum storageType;

    /**
     * 音视频资源类型
     */
    private AvItemTypeEnum avItemType;

    /**
     * 通道ID
     */
    @Column(nullable = false)
    private int channelId;

    /**
     * 开始时间
     */
    private Long startTime;

    /**
     * 结束时间
     */
    private Long endTime;

    /**
     * 文件状态(0:正在上传,1:已完成,2:失败)
     */
    @Column(nullable = false, columnDefinition = "tinyint default 0")
    private int fileStatus;

    /**
     * JT808消息流水号
     */
    private Integer msgFlowId;

    /**
     * 文件大小
     */
    private long fileSize;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件路径
     */
    @Column(length = 2000)
    private String filePath;

    /**
     * base64加密后的文件路径
     */
    @Column(name = "base64_file_path", length = 2000)
    private String base64FilePath;
}