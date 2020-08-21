package com.gnss.web.command.controller;

import com.alibaba.fastjson.JSON;
import com.gnss.core.constants.CommandSendResultEnum;
import com.gnss.core.constants.MediaDataTypeEnum;
import com.gnss.core.constants.jt1078.ChannelTypeEnum;
import com.gnss.core.exception.ApplicationException;
import com.gnss.core.model.config.MediaServerConfig;
import com.gnss.core.model.jt808.CommonReplyParam;
import com.gnss.web.command.api.CommandRequestDTO;
import com.gnss.web.command.api.CommandResultDTO;
import com.gnss.web.command.api.MediaCommandResultDTO;
import com.gnss.web.command.api.jt808.media.Command9003Param;
import com.gnss.web.command.api.jt808.media.Command9101Param;
import com.gnss.web.command.api.jt808.media.Command9102Param;
import com.gnss.web.command.api.jt808.media.Command9201Param;
import com.gnss.web.command.api.jt808.media.Command9202Param;
import com.gnss.web.command.api.jt808.media.Command9205Param;
import com.gnss.web.command.api.jt808.media.Command9206Param;
import com.gnss.web.command.api.jt808.media.Command9207Param;
import com.gnss.web.command.api.jt808.media.Command9301Param;
import com.gnss.web.command.api.jt808.media.Command9302Param;
import com.gnss.web.command.api.jt808.media.Command9303Param;
import com.gnss.web.command.api.jt808.media.Command9304Param;
import com.gnss.web.command.api.jt808.media.Command9305Param;
import com.gnss.web.command.api.jt808.media.Command9306Param;
import com.gnss.web.command.service.CommandOperationService;
import com.gnss.web.common.api.ApiResultDTO;
import com.gnss.web.common.constant.GnssConstants;
import com.gnss.web.common.constant.LiveProtocolEnum;
import com.gnss.web.global.service.CacheService;
import com.gnss.web.terminal.service.RecordFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>Description: JT1078流媒体指令操作接口</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author menghuan
 * @version 1.0.1
 * @date 2019/2/14
 */
@Api(tags = "JT808流媒体指令操作")
@RestController
@RequestMapping("/api/v1/commands/jt808/media")
public class Jt808MediaCommandController {

    @Autowired
    private CommandOperationService commandOperationService;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private RecordFileService recordFileService;

    @ApiOperation("查询终端音视频属性")
    @PostMapping("/sendCommand9003")
    public ApiResultDTO<CommandResultDTO> sendCommand9003(@ApiParam("指令信息") @Valid @RequestBody CommandRequestDTO<Command9003Param> commandRequestDTO) {
        CommandResultDTO resultDTO = commandOperationService.sendCommandEntity(commandRequestDTO);
        return ApiResultDTO.success(resultDTO);
    }

    @ApiOperation("实时音视频传输请求")
    @PostMapping("/sendCommand9101")
    public ApiResultDTO<MediaCommandResultDTO> sendCommand9101(@ApiParam("指令信息") @Valid @RequestBody CommandRequestDTO<Command9101Param> commandRequestDTO) {
        CommandResultDTO resultDTO = commandOperationService.sendCommandEntity(commandRequestDTO);
        MediaCommandResultDTO mediaCommandResultDTO = buildMediaCommandResult(resultDTO, commandRequestDTO.getParamsEntity());
        return new ApiResultDTO<>(mediaCommandResultDTO);
    }

    @ApiOperation("音视频实时传输控制")
    @PostMapping("/sendCommand9102")
    public ApiResultDTO<MediaCommandResultDTO> sendCommand9102(@ApiParam("指令信息") @Valid @RequestBody CommandRequestDTO<Command9102Param> commandRequestDTO) {
        CommandResultDTO resultDTO = commandOperationService.sendCommandEntity(commandRequestDTO);
        MediaCommandResultDTO mediaCommandResultDTO = buildMediaCommandResult(resultDTO, commandRequestDTO.getParamsEntity());
        return ApiResultDTO.success(mediaCommandResultDTO);
    }

    @ApiOperation("平台下发远程录像回放请求")
    @PostMapping("/sendCommand9201")
    public ApiResultDTO<MediaCommandResultDTO> sendCommand9201(@ApiParam("指令信息") @Valid @RequestBody CommandRequestDTO<Command9201Param> commandRequestDTO) {
        CommandResultDTO resultDTO = commandOperationService.sendCommandEntity(commandRequestDTO);
        MediaCommandResultDTO mediaCommandResultDTO = buildMediaCommandResult(resultDTO, commandRequestDTO.getParamsEntity());
        return new ApiResultDTO<>(mediaCommandResultDTO);
    }

    @ApiOperation("平台下发远程录像回放控制")
    @PostMapping("/sendCommand9202")
    public ApiResultDTO<CommandResultDTO> sendCommand9202(@ApiParam("指令信息") @Valid @RequestBody CommandRequestDTO<Command9202Param> commandRequestDTO) {
        CommandResultDTO resultDTO = commandOperationService.sendCommandEntity(commandRequestDTO);
        return ApiResultDTO.success(resultDTO);
    }

    @ApiOperation("查询资源列表")
    @PostMapping("/sendCommand9205")
    public ApiResultDTO<CommandResultDTO> sendCommand9205(@ApiParam("指令信息") @Valid @RequestBody CommandRequestDTO<Command9205Param> commandRequestDTO) {
        CommandResultDTO resultDTO = commandOperationService.sendCommandEntity(commandRequestDTO);
        return ApiResultDTO.success(resultDTO);
    }

    @ApiOperation("文件上传指令")
    @PostMapping("/sendCommand9206")
    public ApiResultDTO<CommandResultDTO> sendCommand9206(@ApiParam("指令信息") @Valid @RequestBody CommandRequestDTO<Command9206Param> commandRequestDTO) {
        CommandResultDTO resultDTO = commandOperationService.sendCommandEntity(commandRequestDTO);
        //处理文件上传指令应答
        handleFileUploadResponse(resultDTO, commandRequestDTO.getParamsEntity());
        return ApiResultDTO.success(resultDTO);
    }

    @ApiOperation("文件上传控制")
    @PostMapping("/sendCommand9207")
    public ApiResultDTO<CommandResultDTO> sendCommand9207(@ApiParam("指令信息") @Valid @RequestBody CommandRequestDTO<Command9207Param> commandRequestDTO) {
        CommandResultDTO resultDTO = commandOperationService.sendCommandEntity(commandRequestDTO);
        return ApiResultDTO.success(resultDTO);
    }

    @ApiOperation("云台旋转")
    @PostMapping("/sendCommand9301")
    public ApiResultDTO<CommandResultDTO> sendCommand9301(@ApiParam("指令信息") @Valid @RequestBody CommandRequestDTO<Command9301Param> commandRequestDTO) {
        CommandResultDTO resultDTO = commandOperationService.sendCommandEntity(commandRequestDTO);
        return ApiResultDTO.success(resultDTO);
    }

    @ApiOperation("云台调整焦距控制")
    @PostMapping("/sendCommand9302")
    public ApiResultDTO<CommandResultDTO> sendCommand9302(@ApiParam("指令信息") @Valid @RequestBody CommandRequestDTO<Command9302Param> commandRequestDTO) {
        CommandResultDTO resultDTO = commandOperationService.sendCommandEntity(commandRequestDTO);
        return ApiResultDTO.success(resultDTO);
    }

    @ApiOperation("云台调整光圈控制")
    @PostMapping("/sendCommand9303")
    public ApiResultDTO<CommandResultDTO> sendCommand9303(@ApiParam("指令信息") @Valid @RequestBody CommandRequestDTO<Command9303Param> commandRequestDTO) {
        CommandResultDTO resultDTO = commandOperationService.sendCommandEntity(commandRequestDTO);
        return ApiResultDTO.success(resultDTO);
    }

    @ApiOperation("云台雨刷控制")
    @PostMapping("/sendCommand9304")
    public ApiResultDTO<CommandResultDTO> sendCommand9304(@ApiParam("指令信息") @Valid @RequestBody CommandRequestDTO<Command9304Param> commandRequestDTO) {
        CommandResultDTO resultDTO = commandOperationService.sendCommandEntity(commandRequestDTO);
        return ApiResultDTO.success(resultDTO);
    }

    @ApiOperation("红外补光控制")
    @PostMapping("/sendCommand9305")
    public ApiResultDTO<CommandResultDTO> sendCommand9305(@ApiParam("指令信息") @Valid @RequestBody CommandRequestDTO<Command9305Param> commandRequestDTO) {
        CommandResultDTO resultDTO = commandOperationService.sendCommandEntity(commandRequestDTO);
        return ApiResultDTO.success(resultDTO);
    }

    @ApiOperation("云台变倍控制")
    @PostMapping("/sendCommand9306")
    public ApiResultDTO<CommandResultDTO> sendCommand9306(@ApiParam("指令信息") @Valid @RequestBody CommandRequestDTO<Command9306Param> commandRequestDTO) {
        CommandResultDTO resultDTO = commandOperationService.sendCommandEntity(commandRequestDTO);
        return ApiResultDTO.success(resultDTO);
    }

    /**
     * 构建流媒体指令结果
     *
     * @param resultDTO
     * @param paramsEntity
     * @return
     */
    private MediaCommandResultDTO buildMediaCommandResult(CommandResultDTO resultDTO, Object paramsEntity) {
        MediaCommandResultDTO mediaCommandResultDTO = new MediaCommandResultDTO();
        mediaCommandResultDTO.setTerminalId(resultDTO.getTerminalId());
        mediaCommandResultDTO.setTerminalNum(resultDTO.getTerminalNum());
        mediaCommandResultDTO.setSimCode(resultDTO.getSimCode());
        mediaCommandResultDTO.setVehicleNum(resultDTO.getVehicleNum());
        mediaCommandResultDTO.setSendResult(resultDTO.getSendResult());
        mediaCommandResultDTO.setResultDesc(resultDTO.getResultDesc());

        //返回流媒体播放地址
        if (resultDTO.getSendResult() == CommandSendResultEnum.SUCCESS) {
            String streamUrl = null;
            LiveProtocolEnum liveProtocol = null;
            String streamApp = "";
            int channelId = 1;
            int serverType = 0;
            if (paramsEntity instanceof Command9101Param) {
                Command9101Param command9101Param = (Command9101Param) paramsEntity;
                serverType = command9101Param.getServerType();
                MediaDataTypeEnum dataType = command9101Param.getDataType();
                int streamType = command9101Param.getStreamType();
                liveProtocol = command9101Param.getLiveProtocol();
                channelId = command9101Param.getChannelId();
                //实时音视频传输请求数据类型对应的流地址app
                streamApp = getChannelType(dataType, streamType);
            } else if (paramsEntity instanceof Command9201Param) {
                //平台下发远程录像回放请求对应的流地址app
                Command9201Param command9201Param = (Command9201Param) paramsEntity;
                serverType = command9201Param.getServerType();
                streamApp = ChannelTypeEnum.PLAYBACK.getAppName();
                liveProtocol = command9201Param.getLiveProtocol();
                channelId = command9201Param.getChannelId();
            }
            //从缓存查询流媒体服务器配置
            String nodeName = serverType == 0 ? GnssConstants.JAVA_MEDIA_SERVER_NAME : GnssConstants.GO_MEDIA_SERVER_NAME;
            MediaServerConfig mediaServerConfig = cacheService.getMediaServerInfo(nodeName);
            if (mediaServerConfig == null || !mediaServerConfig.getIsRunning()) {
                throw new ApplicationException("流媒体服务器未启动");
            }

            int port = mediaServerConfig.getRtmpPort();
            //flv播放地址以.flv结尾
            String postfix = "";
            if (liveProtocol == LiveProtocolEnum.WEBSOCKET_FLV) {
                port = mediaServerConfig.getWsFlvPort();
                postfix = ".flv";
            } else if (liveProtocol == LiveProtocolEnum.HTTP_FLV) {
                port = mediaServerConfig.getHttpFlvPort();
                postfix = ".flv";
            }
            String terminalSimCode = resultDTO.getSimCode();
            //java流媒体服务器的终端手机号为12位,不足补0
            if (serverType == 0) {
                terminalSimCode = StringUtils.leftPad(terminalSimCode, 12, '0');
            }
            //构造播放的url
            streamUrl = String.format("%s://%s:%d/%s/%s_%d%s", liveProtocol.getProtocol(), mediaServerConfig.getWanIp(), port, streamApp, terminalSimCode, channelId, postfix);
            mediaCommandResultDTO.setStreamUrl(streamUrl);
        }
        return mediaCommandResultDTO;
    }

    /**
     * 实时音视频传输请求数据类型对应的流地址app
     *
     * @param dataType
     * @param streamType
     * @return
     */
    private String getChannelType(MediaDataTypeEnum dataType, int streamType) {
        ChannelTypeEnum channelTypeEnum = null;
        if (dataType == MediaDataTypeEnum.AV || dataType == MediaDataTypeEnum.VIDEO) {
            channelTypeEnum = streamType == 0 ? ChannelTypeEnum.MAIN_VIDEO_AUDIO : ChannelTypeEnum.VIDEO_AUDIO;
        } else if (dataType == MediaDataTypeEnum.TALK) {
            channelTypeEnum = ChannelTypeEnum.TALK;
        } else if (dataType == MediaDataTypeEnum.MONITOR) {
            channelTypeEnum = ChannelTypeEnum.LISTEN;
        } else if (dataType == MediaDataTypeEnum.BROADCAST) {
            channelTypeEnum = ChannelTypeEnum.CENTER_BROADCAST;
        } else if (dataType == MediaDataTypeEnum.TRANSFER) {
            channelTypeEnum = ChannelTypeEnum.TRANSFER;
        }
        return channelTypeEnum == null ? "" : channelTypeEnum.getAppName();
    }

    /**
     * 处理文件上传指令应答
     *
     * @param resultDTO
     * @param paramsEntity
     */
    private void handleFileUploadResponse(CommandResultDTO resultDTO, Command9206Param paramsEntity) {
        if (resultDTO.getSendResult() == CommandSendResultEnum.SUCCESS) {
            //转换成JT808的通用应答0x0001
            CommonReplyParam commonReplyParam = JSON.parseObject(resultDTO.getResponseParams(), CommonReplyParam.class);
            //更新消息流水号
            int replyMsgFlowId = commonReplyParam.getReplyMsgFlowId();
            recordFileService.updateMessageFlowId(paramsEntity.getRecordFileId(), replyMsgFlowId);
        } else {
            //更新文件状态为失败
            int fileStatus = 2;
            recordFileService.updateFileStatus(paramsEntity.getRecordFileId(), fileStatus);
        }
    }
}