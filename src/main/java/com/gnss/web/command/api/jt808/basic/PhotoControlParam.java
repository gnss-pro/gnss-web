package com.gnss.web.command.api.jt808.basic;

import com.gnss.core.proto.TerminalProto;
import com.gnss.core.utils.NumberUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Description: 定时/定距拍照控制参数</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2020/6/4
 */
@ApiModel("定时/定距拍照控制参数")
@Getter
@Setter
public class PhotoControlParam {

    @ApiModelProperty(value = "开关存储标志", position = 1)
    private List<Integer> flagBits;

    @ApiModelProperty(value = "单位(定时:0:秒,1:分;定距:0:米,1:千米)", position = 2)
    @Range(min = 0, max = 1, message = "单位为0或1")
    private int unit;

    @ApiModelProperty(value = "间隔", position = 3)
    private int interval;

    @ApiModelProperty(value = "开关存储标志", hidden = true)
    private int flagValue;

    public long buildMessageBody(TerminalProto terminalInfo) throws Exception {
        if (flagBits == null) {
            flagBits = Collections.EMPTY_LIST;
        }
        //每个bit值转换成整型
        flagValue = NumberUtil.bitsToInt(flagBits, 16);
        //转换单位和间隔
        int unitIntervalValue = (interval << 1) | unit;
        return (unitIntervalValue << 16) | flagValue;
    }

    @Override
    public String toString() {
        Map<String, Object> items = new LinkedHashMap<>();
        items.put("开关存储标志", NumberUtil.formatIntNum(flagValue));
        items.put("单位", unit);
        items.put("间隔", interval);
        return items.toString();
    }
}