package com.gnss.web.log.domain;

import com.gnss.core.constants.ProtocolEnum;
import com.gnss.core.constants.VehiclePlateColorEnum;
import com.gnss.core.constants.jt808.Jt808LinkTypeEnum;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * <p>Description: JT809日志</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2019/10/14
 */
@Entity
@Table(name = "jt808_log", indexes = {
        @Index(name = "jt808_log_idx_time", columnList = "time"),
        @Index(name = "jt808_log_idx_created_date", columnList = "createdDate"),
        @Index(name = "jt808_log_idx_last_modified_date", columnList = "lastModifiedDate"),
})
@EntityListeners(AuditingEntityListener.class)
@Data
public class Jt808Log {

    @Id
    @GeneratedValue(generator = "commonId")
    @GenericGenerator(name = "commonId", strategy = "com.gnss.web.utils.CommonIdGenerator")
    private Long id;

    /**
     * 创建时间
     */
    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdDate;

    /**
     * 最后修改时间
     */
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime lastModifiedDate;

    /**
     * 时间
     */
    @Column(nullable = false)
    private Long time;

    /**
     * 连接类型
     */
    @Enumerated
    private Jt808LinkTypeEnum linkType;

    /**
     * 协议类型
     */
    @Enumerated
    @Column(nullable = false)
    private ProtocolEnum protocolType;

    /**
     * 16进制消息ID
     */
    private String msgIdHex;

    /**
     * 消息ID描述
     */
    private String msgIdDesc;

    /**
     * 终端手机号
     */
    @Column(nullable = false)
    private String phoneNumber;

    /**
     * 车牌号
     */
    private String vehicleNum;

    /**
     * 车牌颜色
     */
    @Enumerated
    private VehiclePlateColorEnum vehiclePlateColor;

    /**
     * 消息流水号
     */
    private Integer msgFlowId;

    /**
     * 加密方式，0：不加密，1：RSA加密
     */
    private Integer encryptType;

    /**
     * 消息体描述
     */
    @Column(columnDefinition = "mediumtext")
    private String msgBodyDesc;

    /**
     * 错误消息
     */
    @Column(columnDefinition = "mediumtext")
    private String errorMsg;
}