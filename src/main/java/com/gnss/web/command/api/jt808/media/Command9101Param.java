package com.gnss.web.command.api.jt808.media;

import com.gnss.core.annotations.DownCommand;
import com.gnss.core.constants.MediaDataTypeEnum;
import com.gnss.core.exception.ApplicationException;
import com.gnss.core.model.config.ConfigItem;
import com.gnss.core.model.config.MediaServerConfig;
import com.gnss.core.proto.TerminalProto;
import com.gnss.core.service.IDownCommandMessage;
import com.gnss.core.service.SpringBeanService;
import com.gnss.web.common.constant.GnssConstants;
import com.gnss.web.common.constant.LiveProtocolEnum;
import com.gnss.web.global.service.CacheService;
import com.gnss.web.utils.GnssUtil;
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
 * <p>Description: JT808 0x9101指令参数</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2018-9-14
 */
@ApiModel("0x9101实时音视频传输请求")
@Data
@DownCommand(messageId = 0x9101, respMessageId = 0x0001, desc = "实时音视频传输请求")
public class Command9101Param implements IDownCommandMessage {

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
    private int channelId = 1;

    @ApiModelProperty(value = "数据类型(0:音视频,1:视频,2:双向对讲,3:监听,4:中心广播,5:透传),默认0:音视频", required = true, position = 6)
    @NotNull(message = "数据类型不能为空")
    private MediaDataTypeEnum dataType = MediaDataTypeEnum.AV;

    @ApiModelProperty(value = "码流类型(0:主码流,1:子码流),默认1", required = true, position = 7)
    @Range(min = 0, max = 1, message = "码流类型为0或1")
    private int streamType = 1;

    @Override
    public byte[] buildMessageBody(TerminalProto terminalInfo) throws Exception {
        byte[] msgBodyArr = null;
        ByteBuf msgBody = null;
        try {
            //从缓存查询流媒体服务器配置
            CacheService cacheService = SpringBeanService.getBean(CacheService.class);
            String nodeName = serverType == 0 ? GnssConstants.JAVA_MEDIA_SERVER_NAME : GnssConstants.GO_MEDIA_SERVER_NAME;
            MediaServerConfig mediaServerConfig = cacheService.getMediaServerInfo(nodeName);
            if (mediaServerConfig == null || !mediaServerConfig.getIsRunning()) {
                throw new ApplicationException("流媒体服务器未启动");
            }
            //流媒体服务器配置项
            ConfigItem configItem = GnssUtil.getMediaConfigItem(dataType, streamType, mediaServerConfig);
            serverTcpPort = configItem.getTcpPort();
            serverUdpPort = configItem.getUdpPort();
            serverIp = mediaServerConfig.getWanIp();
            byte[] serverIpArr = serverIp.getBytes();

            msgBody = Unpooled.buffer(serverIpArr.length + 8);
            msgBody.writeByte(serverIpArr.length).writeBytes(serverIpArr)
                    .writeShort(serverTcpPort).writeShort(serverUdpPort)
                    .writeByte(channelId).writeByte(dataType.getValue())
                    .writeByte(streamType);
            msgBodyArr = msgBody.array();
        } catch (Exception e) {
            throw e;
        } finally {
            if (msgBody != null) {
                ReferenceCountUtil.release(msgBody);
            }
        }
        return msgBodyArr;
    }

    @Override
    public String toString() {
        Map<String, Object> msgBodyItems = new LinkedHashMap<>();
        msgBodyItems.put("服务器IP", serverIp);
        msgBodyItems.put("服务器TCP端口", serverTcpPort);
        msgBodyItems.put("服务器UDP端口", serverUdpPort);
        msgBodyItems.put("逻辑通道号", channelId);
        msgBodyItems.put("数据类型", String.format("%d:%s", dataType.getValue(), dataType.getDesc()));
        msgBodyItems.put("码流类型", streamType == 0 ? "0:主码流" : "1:子码流");
        return msgBodyItems.toString();
    }
}