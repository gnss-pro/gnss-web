package com.gnss.web.common.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>Description: 基础位置信息</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2018-2-13
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@ToString
public abstract class BaseLocationEntity implements Serializable {
    /**
     * 创建时间
     */
    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdDate;

    /**
     * 修改时间
     */
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime lastModifiedDate;

    /**
     * 是否定位：0未定位，1定位
     */
    @Column(nullable = false, columnDefinition = "tinyint default 0")
    private int gpsValid;

    /**
     * ACC状态：0关，1开
     */
    @Column(columnDefinition = "tinyint default 0")
    private Integer acc;

    /**
     * 纬度
     */
    private Double lat;

    /**
     * 经度
     */
    private Double lon;

    /**
     * 高程
     */
    private Integer altitude;

    /**
     * 速度
     */
    private Double speed;

    /**
     * 方向
     */
    private Integer direction;

    /**
     * 时间
     */
    private Long time;

    /**
     * 里程
     */
    private Double mileage;

    /**
     * 油量
     */
    private Double fuel;

    /**
     * 行驶记录仪速度
     */
    private Double recoderSpeed;

    /**
     * 状态
     */
    private Long status;

    /**
     * 报警标志
     */
    private Long alarmFlag;

    /**
     * 视频报警
     */
    private Long jt1078AlarmFlag;

    /**
     * 附加信息,json字符串
     */
    @Column(length = 2048)
    private String extraInfo;

    /**
     * 状态位(JSON字符串)
     */
    private String statusBits;

    /**
     * JT808报警位(JSON字符串)
     */
    private String alarmBits;

    /**
     * JT1078报警位(JSON字符串)
     */
    private String jt1078AlarmBits;

    /**
     * 主动安全报警列表(JSON字符串)
     */
    private String safetyAlarmList;

}