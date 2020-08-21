package com.gnss.web.terminal.domain;

import com.gnss.core.constants.jt808.Jt808MediaEventEnum;
import com.gnss.core.constants.jt808.Jt808MediaFormatEnum;
import com.gnss.core.constants.jt808.Jt808MediaTypeEnum;
import com.gnss.web.common.domain.BaseEntity;
import com.gnss.web.info.domain.Terminal;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * <p>Description: JT808多媒体文件</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2020/4/1
 */
@Entity
@Table(name = "terminal_media_file", indexes = {
        @Index(name = "media_file_idx_created_date", columnList = "createdDate"),
        @Index(name = "media_file_idx_last_modified_date", columnList = "lastModifiedDate")})
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@ToString(exclude = {"terminal"})
public class MediaFile extends BaseEntity {

    /**
     * 关联终端
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "terminal_id", nullable = false)
    private Terminal terminal;

    /**
     * 多媒体ID
     */
    private long mediaId;

    /**
     * 多媒体类型
     */
    @Enumerated
    private Jt808MediaTypeEnum mediaType;

    /**
     * 多媒体格式编码
     */
    @Enumerated
    private Jt808MediaFormatEnum mediaFormatCode;

    /**
     * 事件项编码
     */
    @Enumerated
    private Jt808MediaEventEnum eventItemCode;

    /**
     * 通道ID
     */
    @Column(nullable = false)
    private int channelId;

    /**
     * 文件名
     */
    @Column(nullable = false)
    private String fileName;

    /**
     * 文件大小
     */
    @Column(nullable = false, name = "file_size")
    private long fileSize;

    /**
     * 文件路径
     */
    @Column(nullable = false, length = 512)
    private String filePath;

    /**
     * base64加密后的文件路径
     */
    @Column(name = "base64_file_path", nullable = false, length = 512)
    private String base64FilePath;

    /**
     * 位置信息
     */
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;
}