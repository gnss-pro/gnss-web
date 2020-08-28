package com.gnss.web.command.api.jt808.media;

import com.gnss.core.annotations.DownCommand;
import com.gnss.core.constants.jt1078.AvItemTypeEnum;
import com.gnss.core.constants.jt1078.ChannelTypeEnum;
import com.gnss.core.constants.jt1078.MemoryTypeEnum;
import com.gnss.core.constants.jt1078.PlaybackActionEnum;
import com.gnss.core.constants.jt1078.PlaybackFastTimeEnum;
import com.gnss.core.constants.jt1078.StreamTypeEnum;
import com.gnss.core.exception.ApplicationException;
import com.gnss.core.model.config.ConfigItem;
import com.gnss.core.model.config.MediaServerConfig;
import com.gnss.core.proto.TerminalProto;
import com.gnss.core.service.IDownCommandMessage;
import com.gnss.core.service.SpringBeanService;
import com.gnss.core.utils.TimeUtil;
import com.gnss.web.common.constant.GnssConstants;
import com.gnss.web.common.constant.LiveProtocolEnum;
import com.gnss.web.global.service.CacheService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCountUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>Description: JT808 0x9201指令参数</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2018-9-14
 */
@ApiModel("0x9201平台下发远程录像回放请求")
@Data
@DownCommand(messageId = 0x9201, respMessageId = 0x1205, desc = "平台下发远程录像回放请求")
public class Command9201Param implements IDownCommandMessage {

    @ApiModelProperty(value = "流媒体服务器类型(0:JAVA流媒体服务器;1:go流媒体服务器)")
    private int serverType = 0;

    @ApiModelProperty(value = "直播协议类型", required = true, position = 1)
    @NotNull(message = "直播协议类型不能为空")
    private LiveProtocolEnum liveProtocol = LiveProtocolEnum.WEBSOCKET_FLV;

    @ApiModelProperty(value = "流媒体服务器IP", position = 2, hidden = true)
    private String serverIp;

    @ApiModelProperty(value = "流媒体服务器TCP端口", position = 3, hidden = true)
    private int serverTcpPort;

    @ApiModelProperty(value = "流媒体服务器UDP端口", position = 4, hidden = true)
    private int serverUdpPort;

    @ApiModelProperty(value = "逻辑通道号", position = 5)
    @Range(min = 1, max = 127, message = "逻辑通道号范围为1-127")
    private int channelId;

    @ApiModelProperty(value = "音视频类型(0:音视频,1:音频,2:视频,3:视频或音视频)", required = true, position = 6)
    @NotNull(message = "音视频类型不能为空")
    private AvItemTypeEnum avItemType;

    @ApiModelProperty(value = "码流类型(0:全部类型,1:主码流,2:子码流)", required = true, position = 7)
    @NotNull(message = "码流类型不能为空")
    private StreamTypeEnum streamType;

    @ApiModelProperty(value = "存储器类型(0:所有存储器,1:主存储器,2:灾备存储器)", required = true, position = 8)
    @NotNull(message = "存储器类型不能为空")
    private MemoryTypeEnum memoryType;

    @ApiModelProperty(value = "回放方式(0:正常回放,1:快进回放,2:关键帧快退回放,3:关键帧播放,4:单帧上传)", required = true, position = 9)
    @NotNull(message = "回放方式不能为空")
    private PlaybackActionEnum actionType;

    @ApiModelProperty(value = "快进或快退倍数(0:无效,1:1倍,2:2倍,3:4倍,4:8倍,5:16倍)", required = true, position = 10)
    @NotNull(message = "快进或快退倍数不能为空")
    private PlaybackFastTimeEnum fastTime;

    @ApiModelProperty(value = "开始时间", required = true, position = 11)
    @NotNull(message = "开始时间不能为空")
    private Long startTime;

    @ApiModelProperty(value = "结束时间", required = true, position = 12)
    @NotNull(message = "结束时间不能为空")
    private Long endTime;

    @Override
    public byte[] buildMessageBody(TerminalProto terminalInfo) throws Exception {
        //开始时间
        byte[] startTimeArr = TimeUtil.timestampToBcdTime(startTime);
        //结束时间
        byte[] endTimeArr = TimeUtil.timestampToBcdTime(endTime);

        //从缓存查询流媒体服务器配置
        CacheService cacheService = SpringBeanService.getBean(CacheService.class);
        String nodeName = serverType == 0 ? GnssConstants.JAVA_MEDIA_SERVER_NAME : GnssConstants.GO_MEDIA_SERVER_NAME;
        MediaServerConfig mediaServerConfig = cacheService.getMediaServerInfo(nodeName);
        if (mediaServerConfig == null || !mediaServerConfig.getIsRunning()) {
            throw new ApplicationException("流媒体服务器未启动");
        }
        Map<ChannelTypeEnum, ConfigItem> servers = mediaServerConfig.getServers();
        ConfigItem configItem = servers.get(ChannelTypeEnum.PLAYBACK);
        if (configItem == null) {
            throw new ApplicationException("流媒体服务器未配置" + ChannelTypeEnum.PLAYBACK.getDesc() + "端口");
        }

        serverIp = mediaServerConfig.getWanIp();
        serverTcpPort = configItem.getTcpPort();
        serverUdpPort = configItem.getUdpPort();
        byte[] serverIpArr = serverIp.getBytes();
        ByteBuf msgBody = Unpooled.buffer(serverIpArr.length + 23);
        msgBody.writeByte(serverIpArr.length)
                .writeBytes(serverIpArr)
                .writeShort(serverTcpPort)
                .writeShort(serverUdpPort)
                .writeByte(channelId)
                .writeByte(avItemType.getValue())
                .writeByte(streamType.getValue())
                .writeByte(memoryType.getValue())
                .writeByte(actionType.getValue())
                .writeByte(fastTime.getValue())
                .writeBytes(startTimeArr)
                .writeBytes(endTimeArr);
        byte[] msgBodyArr = msgBody.array();
        ReferenceCountUtil.release(msgBody);
        return msgBodyArr;
    }

    @Override
    public String toString() {
        Map<String, Object> msgBodyItems = new LinkedHashMap<>();
        msgBodyItems.put("服务器IP", serverIp);
        msgBodyItems.put("服务器TCP端口", serverTcpPort);
        msgBodyItems.put("服务器UDP端口", serverUdpPort);
        msgBodyItems.put("逻辑通道号", channelId);
        msgBodyItems.put("音视频类型", String.format("%d:%s", avItemType.getValue(), avItemType.getDesc()));
        msgBodyItems.put("码流类型", String.format("%d:%s", streamType.getValue(), streamType.getDesc()));
        msgBodyItems.put("存储器类型", String.format("%d:%s", memoryType.getValue(), memoryType.getDesc()));
        msgBodyItems.put("回放方式", String.format("%d:%s", actionType.getValue(), actionType.getDesc()));
        msgBodyItems.put("快进或快退倍数", String.format("%d:%s", fastTime.getValue(), fastTime.getDesc()));
        msgBodyItems.put("开始时间", startTime == null ? "0" : TimeUtil.formatTime(startTime));
        msgBodyItems.put("结束时间", endTime == null ? "0" : TimeUtil.formatTime(endTime));
        return msgBodyItems.toString();
    }
}