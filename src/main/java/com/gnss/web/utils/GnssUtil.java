package com.gnss.web.utils;

import com.gnss.core.constants.MediaDataTypeEnum;
import com.gnss.core.constants.jt1078.ChannelTypeEnum;
import com.gnss.core.exception.ApplicationException;
import com.gnss.core.model.config.ConfigItem;
import com.gnss.core.model.config.MediaServerConfig;
import com.gnss.web.constants.TimeDurationEnum;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>Description: GNSS工具类</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2019-09-22
 */
public class GnssUtil {

    private GnssUtil() {
    }

    /**
     * 构造指令事件Key
     *
     * @param terminalId
     * @param downCommandId
     * @return
     */
    public static String buildCommandEventKey(long terminalId, String downCommandId) {
        return String.format("%d-%s", terminalId, downCommandId);
    }

    /**
     * 获取流媒体服务器端口
     *
     * @param avItemType
     * @param streamType
     * @param serverConfig
     * @return
     */
    public static ConfigItem getMediaConfigItem(MediaDataTypeEnum avItemType, int streamType, MediaServerConfig serverConfig) {
        Map<ChannelTypeEnum, ConfigItem> servers = serverConfig.getServers();
        ConfigItem configItem = null;
        ChannelTypeEnum channelType = ChannelTypeEnum.VIDEO_AUDIO;
        if (avItemType == MediaDataTypeEnum.AV || avItemType == MediaDataTypeEnum.VIDEO) {
            //主码流和子码流对应的端口
            channelType = streamType == 0 ? ChannelTypeEnum.MAIN_VIDEO_AUDIO : ChannelTypeEnum.VIDEO_AUDIO;
        } else if (avItemType == MediaDataTypeEnum.TALK) {
            channelType = ChannelTypeEnum.TALK;
        } else if (avItemType == MediaDataTypeEnum.MONITOR) {
            channelType = ChannelTypeEnum.LISTEN;
        } else if (avItemType == MediaDataTypeEnum.BROADCAST) {
            channelType = ChannelTypeEnum.CENTER_BROADCAST;
        } else if (avItemType == MediaDataTypeEnum.TRANSFER) {
            channelType = ChannelTypeEnum.TRANSFER;
        }
        configItem = servers.get(channelType);
        if (configItem == null) {
            throw new ApplicationException("流媒体服务器未配置" + channelType.getDesc() + "端口");
        }
        return configItem;
    }

    /**
     * 格式化时长
     *
     * @param timeDuration 时长(单位:秒)
     * @return
     */
    public static String formatTimeDuration(Long timeDuration) {
        if (timeDuration == null) {
            return "";
        }
        List<String> strList = new ArrayList<>();
        for (TimeDurationEnum durationEnum : TimeDurationEnum.values()) {
            int result = (int) (timeDuration / durationEnum.getValue());
            if (result != 0) {
                strList.add(result + durationEnum.getDesc());
                timeDuration = timeDuration % durationEnum.getValue();
            }
        }
        if (strList.isEmpty()) {
            return "0" + TimeDurationEnum.SECOND.getDesc();
        }
        return StringUtils.join(strList, "");
    }
}
