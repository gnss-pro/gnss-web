package com.gnss.web.info.domain;

import com.gnss.core.constants.VehiclePlateColorEnum;
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
 * <p>Description: 终端信息</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2019/10/14
 */
@Entity
@Table(name = "sys_terminal", indexes = {
        @Index(name = "terminal_idx_terminal_num", columnList = "terminalNum"),
        @Index(name = "terminal_idx_phone_num", columnList = "phoneNum"),
        @Index(name = "terminal_idx_vehicle_num", columnList = "vehicleNum"),
        @Index(name = "terminal_idx_created_date", columnList = "createdDate"),
        @Index(name = "terminal_idx_last_modified_date", columnList = "lastModifiedDate"),
})
@EntityListeners(AuditingEntityListener.class)
@Data
public class Terminal {

    public Terminal() {
    }

    public Terminal(Long id) {
        setId(id);
    }

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
     * 终端手机号
     */
    @Column(nullable = false, unique = true)
    private String phoneNum;

    /**
     * 终端号码
     */
    @Column(nullable = false, unique = true)
    private String terminalNum;

    /**
     * 车牌号码
     */
    @Column(nullable = false, unique = true)
    private String vehicleNum;

    /**
     * 车牌颜色
     */
    @Enumerated
    @Column(nullable = false)
    private VehiclePlateColorEnum vehiclePlateColor = VehiclePlateColorEnum.YELLOW;
}