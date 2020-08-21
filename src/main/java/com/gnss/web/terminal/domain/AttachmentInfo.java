package com.gnss.web.terminal.domain;

import com.gnss.web.common.domain.BaseEntity;
import com.gnss.web.info.domain.Terminal;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * <p>Description: 主动安全报警附件信息</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2018-12-26
 */
@Entity
@Table(name = "terminal_attachment", indexes = {
        @Index(name = "attachment_idx_created_date", columnList = "createdDate"),
        @Index(name = "attachment_idx_last_modified_date", columnList = "lastModifiedDate")})
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@ToString(exclude = {"terminal"})
public class AttachmentInfo extends BaseEntity {

    /**
     * 关联终端
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "terminal_id", columnDefinition = "bigint COMMENT '终端ID'")
    private Terminal terminal;

    /**
     * 文件名称
     */
    @Column(nullable = false, columnDefinition = "varchar(255) COMMENT '文件名称'")
    private String fileName;

    /**
     * 文件大小
     */
    @Column(nullable = false, columnDefinition = "int COMMENT '文件大小'")
    private Integer fileSize;

    /**
     * 文件类型
     */
    @Column(columnDefinition = "int COMMENT '文件类型:0图片,1音频,2视频,3文本,4其他'")
    private Integer fileType;

    /**
     * 文件路径
     */
    @Column(nullable = false, columnDefinition = "varchar(500) COMMENT '文件路径'")
    private String filePath;

    /**
     * base64加密后的文件路径
     */
    @Column(name = "base64_file_path", nullable = false, columnDefinition = "varchar(500) COMMENT 'base64加密后的文件路径'")
    private String base64FilePath;

    /**
     * 关联主动安全报警
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "safety_alarm_id", nullable = false, columnDefinition = "bigint COMMENT '主动安全报警ID'")
    private ActiveSafetyAlarm activeSafetyAlarm;
}