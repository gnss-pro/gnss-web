package com.gnss.web.terminal.domain;

import com.gnss.core.constants.AlarmTypeEnum;
import com.gnss.web.common.domain.BaseEntity;
import com.gnss.web.constants.AlarmSourceEnum;
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
import java.time.LocalDateTime;

/**
 * <p>Description: 报警</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2018-12-26
 */
@Entity
@Table(name = "terminal_alarm", indexes = {
        @Index(name = "alarm_idx_start_time", columnList = "startTime"),
        @Index(name = "alarm_idx_end_time", columnList = "endTime"),
        @Index(name = "alarm_idx_created_date", columnList = "createdDate"),
        @Index(name = "alarm_idx_last_modified_date", columnList = "lastModifiedDate")
})
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@ToString(exclude = {"terminal"})
public class Alarm extends BaseEntity {

    /**
     * 报警类型
     */
    @Enumerated
    private AlarmTypeEnum alarmType;

    /**
     * 报警来源
     */
    @Enumerated
    private AlarmSourceEnum alarmSource;

    /**
     * 关联终端
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "terminal_id", nullable = false)
    private Terminal terminal;

    /**
     * 报警开始纬度
     */
    private Double startLat;

    /**
     * 报警开始经度
     */
    private Double startLon;

    /**
     * 报警开始速度
     */
    private Double startSpeed;

    /**
     * 报警开始时间
     */
    private Long startTime;

    /**
     * 报警开始行驶记录仪速度
     */
    private Double startRecoderSpeed;

    /**
     * 报警开始(报警标志)
     */
    private Long startAlarmFlag;

    /**
     * 报警开始(JT1078报警标志)
     */
    private Long startJt1078AlarmFlag;

    /**
     * 报警开始状态
     */
    private Long startStatus;

    /**
     * 报警开始附加信息,json字符串
     */
    @Column(length = 1500)
    private String startExtraInfo;

    /**
     * 报警开始报警标志位,json字符串
     */
    private String startAlarmBits;

    /**
     * 报警开始JT1078报警位,json字符串
     */
    private String startJt1078AlarmBits;

    /**
     * 报警开始状态位,json字符串
     */
    private String startStatusBits;

    /**
     * 开始位置
     */
    @Column(length = 1500)
    private String startLocation;

    /**
     * 报警结束纬度
     */
    private Double endLat;

    /**
     * 报警结束经度
     */
    private Double endLon;

    /**
     * 报警结束速度
     */
    private Double endSpeed;

    /**
     * 报警结束时间
     */
    private Long endTime;

    /**
     * 报警结束行驶记录仪速度
     */
    private Double endRecoderSpeed;

    /**
     * 报警结束(报警标志)
     */
    private Long endAlarmFlag;

    /**
     * 报警结束(JT1078报警标志)
     */
    private Long endJt1078AlarmFlag;

    /**
     * 报警结束状态
     */
    private Long endStatus;

    /**
     * 报警结束附加信息,json字符串
     */
    @Column(length = 3000)
    private String endExtraInfo;

    /**
     * 报警结束报警标志位,json字符串
     */
    private String endAlarmBits;

    /**
     * 报警结束JT1078报警标志位,json字符串
     */
    private String endJt1078AlarmBits;

    /**
     * 报警结束状态位,json字符串
     */
    private String endStatusBits;

    /**
     * 结束位置
     */
    @Column(length = 1500)
    private String endLocation;

    /**
     * 报警级别
     */
    private Integer alarmLevel;

    /**
     * 报警信息
     */
    private String alarmInfo;

    /**
     * 处理人
     */
    private String handleBy;

    /**
     * 是否处理(0:未处理,1:已处理)
     */
    @Column(nullable = false, columnDefinition = "char(1) default '0'")
    private int isHandle;

    /**
     * 处理内容
     */
    private String handleContent;

    /**
     * 处理时间
     */
    private LocalDateTime handleTime;

    /**
     * 报警时长(秒)
     */
    private Long alarmDuration;

    /**
     * 关联的主动安全报警信息
     */
    @OneToOne(mappedBy = "terminalAlarm", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private ActiveSafetyAlarm activeSafetyAlarm;
}