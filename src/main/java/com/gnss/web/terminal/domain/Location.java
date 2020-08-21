package com.gnss.web.terminal.domain;

import com.gnss.web.common.domain.BaseLocationEntity;
import com.gnss.web.info.domain.Terminal;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * <p>Description: 位置实体</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2019/4/1
 */
@Entity
@Table(name = "terminal_location", indexes = {
        @Index(name = "location_idx_created_date", columnList = "createdDate"),
        @Index(name = "location_idx_last_modified_date", columnList = "lastModifiedDate")})
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@ToString(exclude = {"terminal"})
public class Location extends BaseLocationEntity {

    @Id
    @GeneratedValue(generator = "commonId")
    @GenericGenerator(name = "commonId", strategy = "com.gnss.web.utils.CommonIdGenerator")
    private Long id;

    /**
     * 关联终端
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "terminal_id", nullable = false)
    private Terminal terminal;
}
