package com.gnss.web.common.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Pendy
 * @date 2017/6/8
 * @desc 基础实体类, 包含ID和审计日志
 **/
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@ToString(of = {"id"})
@EqualsAndHashCode(of = {"id"})
public abstract class BaseEntity implements Serializable {

    @Id
    @GeneratedValue(generator = "commonId")
    @GenericGenerator(name = "commonId", strategy = "com.gnss.web.utils.CommonIdGenerator")
    private Long id;

    /**
     * 创建时间
     */
    @CreatedDate
    @Column(nullable = false, columnDefinition = "datetime COMMENT '创建时间'")
    private LocalDateTime createdDate;

    /**
     * 修改时间
     */
    @LastModifiedDate
    @Column(nullable = false, columnDefinition = "datetime COMMENT '最后修改时间'")
    private LocalDateTime lastModifiedDate;
}