package com.gnss.web.terminal.domain;

import com.gnss.core.constants.AlarmTypeEnum;
import com.gnss.core.constants.safety.ActiveSafetyEnum;
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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Description: 主动安全报警</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2018-08-25
 */
@Entity
@Table(name = "terminal_safety_alarm", indexes = {
        @Index(name = "safety_alarm_idx_created_date", columnList = "createdDate"),
        @Index(name = "safety_alarm_idx_last_modified_date", columnList = "lastModifiedDate")})
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@ToString(exclude = {"terminal"})
public class ActiveSafetyAlarm extends BaseEntity {

    public ActiveSafetyAlarm() {
    }

    public ActiveSafetyAlarm(Long id) {
        setId(id);
    }

    /**
     * 关联终端
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "terminal_id", nullable = false, columnDefinition = "bigint COMMENT '终端ID'")
    private Terminal terminal;

    /**
     * 关联终端报警
     */
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "terminal_alarm_id", nullable = false, columnDefinition = "bigint COMMENT '关联终端报警ID'")
    private Alarm terminalAlarm;

    /**
     * 报警类型
     */
    @Enumerated
    @Column(nullable = false, columnDefinition = "int COMMENT '报警类型'")
    private AlarmTypeEnum alarmType;

    /**
     * 主动安全类型
     */
    @Enumerated
    @Column(nullable = false, columnDefinition = "tinyint COMMENT '主动安全类型(0:ADAS 1:DSM 2:TPMS 3:BSD)'")
    private ActiveSafetyEnum activeSafetyType;

    /**
     * 报警ID
     */
    @Column(nullable = false, columnDefinition = "bigint COMMENT '报警ID,协议字段'")
    private Long alarmId;

    /**
     * 标志状态(0x00:不可用,0x01:开始标志,0x02:结束标志)
     */
    @Column(columnDefinition = "int COMMENT '标志状态(0x00:不可用,0x01:开始标志,0x02:结束标志)'")
    private Integer flagStatus;

    /**
     * 报警/事件类型
     */
    @Column(columnDefinition = "int COMMENT '报警/事件类型'")
    private Integer eventType;

    /**
     * 报警级别
     */
    @Column(columnDefinition = "int COMMENT '报警级别'")
    private Integer alarmLevel;

    /**
     * 车速,单位Km/h,范围0~250
     */
    @Column(columnDefinition = "int COMMENT '车速,单位Km/h,范围0~250'")
    private Integer speed;

    /**
     * 高程,海拔高度,单位为米
     */
    @Column(columnDefinition = "int COMMENT '高程,海拔高度,单位为米'")
    private Integer altitude;

    /**
     * 纬度
     */
    @Column(columnDefinition = "double COMMENT '纬度'")
    private Double lat;

    /**
     * 经度
     */
    @Column(columnDefinition = "double COMMENT '经度'")
    private Double lon;

    /**
     * GMT+8时间戳
     */
    @Column(columnDefinition = "bigint COMMENT 'GMT+8时间戳'")
    private Long time;

    /**
     * 车辆状态
     */
    @Column(columnDefinition = "int COMMENT '车辆状态'")
    private Integer vehicleStatus;

    /**
     * 车辆状态位JSON
     */
    @Column(columnDefinition = "varchar(255) COMMENT '车辆状态位JSON'")
    private String vehicleStatusBits;

    /**
     * 报警标识号JSON
     */
    @Column(columnDefinition = "varchar(1000) COMMENT '报警标识号JSON'")
    private String alarmFlag;

    /**
     * 扩展数据JSON
     */
    @Column(columnDefinition = "varchar(1000) COMMENT '扩展数据JSON'")
    private String extraData;

    /**
     * 已完成上传的附件数量
     */
    @Column(columnDefinition = "int default 0 COMMENT '已完成上传附件数量'")
    private Integer completedCount = 0;

    /**
     * 附件数量
     */
    @Column(columnDefinition = "int default 0 COMMENT '附件数量'")
    private Integer attachmentCount;

    /**
     * 关联附件
     */
    @OneToMany(mappedBy = "activeSafetyAlarm", fetch = FetchType.LAZY)
    private List<AttachmentInfo> attachments = new ArrayList<>();
}