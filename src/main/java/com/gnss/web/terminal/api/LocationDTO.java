package com.gnss.web.terminal.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>Description: 位置信息</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2019/10/14
 */
@ApiModel("位置信息")
@Data
public class LocationDTO {

    @ApiModelProperty(value = "ID")
    private String locationId;

    @ApiModelProperty(value = "是否定位：0未定位，1定位", position = 1)
    private int gpsValid;

    @ApiModelProperty(value = "纬度", position = 2)
    private Double lat;

    @ApiModelProperty(value = "经度", position = 3)
    private Double lon;

    @ApiModelProperty(value = "高程", position = 4)
    private Integer altitude;

    @ApiModelProperty(value = "速度", position = 5)
    private Double speed;

    @ApiModelProperty(value = "方向", position = 6)
    private Integer direction;

    @ApiModelProperty(value = "时间", position = 7)
    private String gpsTime;

    @ApiModelProperty(value = "里程", position = 8)
    private Double mileage;

    @ApiModelProperty(value = "油量", position = 9)
    private Double fuel;

    @ApiModelProperty(value = "行驶记录仪速度", position = 10)
    private Double recoderSpeed;

    @ApiModelProperty(value = "状态", position = 11)
    private long status;

    @ApiModelProperty(value = "报警标志", position = 14)
    private long alarmFlag;

    @ApiModelProperty(value = "视频报警", position = 15)
    private Long jt1078AlarmFlag;

    @ApiModelProperty(value = "附加信息,json字符串", position = 16)
    private String extraInfo;
}