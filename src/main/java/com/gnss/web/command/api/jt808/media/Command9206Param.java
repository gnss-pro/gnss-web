package com.gnss.web.command.api.jt808.media;

import com.alibaba.fastjson.JSON;
import com.gnss.core.annotations.DownCommand;
import com.gnss.core.constants.RpcEnum;
import com.gnss.core.constants.jt1078.AvItemTypeEnum;
import com.gnss.core.constants.jt1078.MemoryTypeEnum;
import com.gnss.core.constants.jt1078.StreamTypeEnum;
import com.gnss.core.exception.ApplicationException;
import com.gnss.core.model.config.FileServerConfig;
import com.gnss.core.proto.RpcProto;
import com.gnss.core.proto.TerminalProto;
import com.gnss.core.rpc.FtpUploadCommand;
import com.gnss.core.service.IDownCommandMessage;
import com.gnss.core.service.SpringBeanService;
import com.gnss.core.utils.TimeUtil;
import com.gnss.mqutil.producer.RabbitMessageSender;
import com.gnss.web.constants.FileStorageEnum;
import com.gnss.web.global.service.CacheService;
import com.gnss.web.info.domain.Terminal;
import com.gnss.web.terminal.domain.RecordFile;
import com.gnss.web.terminal.service.RecordFileService;
import com.gnss.web.utils.CommonIdGenerator;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCountUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>Description: JT808 0x9206指令参数</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2018-9-14
 */
@ApiModel("0x9206文件上传指令")
@Data
@Slf4j
@DownCommand(messageId = 0x9206, respMessageId = 0x0001, desc = "文件上传指令")
public class Command9206Param implements IDownCommandMessage {

    @ApiModelProperty(value = "文件上传路径", hidden = true)
    private String filePath;

    @ApiModelProperty(value = "逻辑通道号", required = true, position = 1)
    @Range(min = 1, max = 127, message = "逻辑通道号范围1-127")
    private int channelId = 1;

    @ApiModelProperty(value = "开始时间", required = true, position = 2)
    @NotNull(message = "开始时间不能为空")
    private Long startTime;

    @ApiModelProperty(value = "结束时间", required = true, position = 3)
    @NotNull(message = "结束时间不能为空")
    private Long endTime;

    @ApiModelProperty(value = "报警标志", required = true, position = 4)
    private long alarmFlag;

    @ApiModelProperty(value = "音视频资源类型(0:音视频,1:音频,2:视频,3:视频或音视频)", required = true, position = 5)
    @NotNull(message = "音视频资源类型不能为空")
    private AvItemTypeEnum avItemType;

    @ApiModelProperty(value = "码流类型(0:所有码流,1:主码流,2:子码流)", required = true, position = 6)
    @NotNull(message = "码流类型不能为空")
    private StreamTypeEnum streamType;

    @ApiModelProperty(value = "存储器类型(0:所有存储器,1:主存储器,2:灾备存储器)", required = true, position = 7)
    @NotNull(message = "存储器类型不能为空")
    private MemoryTypeEnum memoryType;

    @ApiModelProperty(value = "任务执行条件,默认0x07", position = 8)
    private int taskCondition = 0x07;

    @ApiModelProperty(value = "服务器地址", hidden = true)
    private String serverIp;

    @ApiModelProperty(value = "端口", hidden = true)
    private int serverPort;

    @ApiModelProperty(value = "用户名", hidden = true)
    private String username;

    @ApiModelProperty(value = "密码", hidden = true)
    private String password;

    @ApiModelProperty(value = "录像文件ID", hidden = true)
    private Long recordFileId;

    @Override
    public byte[] buildMessageBody(TerminalProto terminalInfo) throws Exception {
        //开始时间
        byte[] startTimeArr = TimeUtil.timestampToBcdTime(startTime);
        //结束时间
        byte[] endTimeArr = TimeUtil.timestampToBcdTime(endTime);
        //从缓存查询文件服务器配置
        CacheService cacheService = SpringBeanService.getBean(CacheService.class);
        FileServerConfig fileServerConfig = cacheService.getFileServerInfo();
        if (fileServerConfig == null || !fileServerConfig.getIsRunning()) {
            throw new ApplicationException("文件服务器未启动");
        }

        //FTP服务器信息
        serverIp = fileServerConfig.getWanIp();
        serverPort = fileServerConfig.getFtpPort();
        username = fileServerConfig.getFtpUsername();
        password = fileServerConfig.getFtpPassword();
        byte[] serverIpArr = serverIp.getBytes();
        byte[] ftpUserArr = username.getBytes();
        byte[] ftpPasswordArr = password.getBytes();
        //发送指令到文件服务器创建录像上传路径
        FtpUploadCommand ftpUploadCommand = sendFtpUploadCommand(terminalInfo);
        filePath = ftpUploadCommand.getFilePath();
        //保存数据库记录
        saveRecordFile(terminalInfo, ftpUploadCommand);

        byte[] filePathArr = this.filePath.getBytes();
        ByteBuf msgBody = Unpooled.buffer(serverIpArr.length + ftpUserArr.length + ftpPasswordArr.length + filePathArr.length + 31);
        msgBody.writeByte(serverIpArr.length)
                .writeBytes(serverIpArr)
                .writeShort(serverPort)
                .writeByte(ftpUserArr.length)
                .writeBytes(ftpUserArr)
                .writeByte(ftpPasswordArr.length)
                .writeBytes(ftpPasswordArr)
                .writeByte(filePathArr.length)
                .writeBytes(filePathArr)
                .writeByte(channelId)
                .writeBytes(startTimeArr)
                .writeBytes(endTimeArr)
                .writeLong(alarmFlag)
                .writeByte(avItemType.getValue())
                .writeByte(streamType.getValue())
                .writeByte(memoryType.getValue())
                .writeByte(taskCondition);
        byte[] msgBodyArr = msgBody.array();
        ReferenceCountUtil.release(msgBody);
        return msgBodyArr;
    }

    /**
     * 发送指令到文件服务器创建录像上传路径
     *
     * @param terminalInfo
     * @return
     */
    private FtpUploadCommand sendFtpUploadCommand(TerminalProto terminalInfo) throws Exception {
        recordFileId = CommonIdGenerator.generate();
        //构造FTP上传路径
        FtpUploadCommand ftpUploadCommand = new FtpUploadCommand();
        ftpUploadCommand.setTerminalInfo(terminalInfo);
        ftpUploadCommand.setRecordFileId(String.valueOf(recordFileId));
        //发送指令到文件服务器创建录像上传路径
        RpcProto rpcRequest = new RpcProto(RpcEnum.CREATE_FILE_PATH, ftpUploadCommand);
        RabbitMessageSender messageSender = SpringBeanService.getBean(RabbitMessageSender.class);
        RpcProto rpcResponse = messageSender.sendFileServerRpc(rpcRequest);
        if (rpcResponse == null) {
            throw new ApplicationException("文件服务器创建录像上传路径失败");
        }
        ftpUploadCommand = JSON.parseObject(rpcResponse.getContent(), FtpUploadCommand.class);
        if (ftpUploadCommand.getFilePath() == null) {
            throw new ApplicationException("文件服务器创建录像上传路径失败");
        }
        return ftpUploadCommand;
    }

    /**
     * 保存数据库记录
     *
     * @param terminalInfo
     * @param ftpUploadCommand
     */
    private void saveRecordFile(TerminalProto terminalInfo, FtpUploadCommand ftpUploadCommand) {
        String baseDir = Paths.get(ftpUploadCommand.getFtpHomeDir(), ftpUploadCommand.getFilePath()).toString();
        RecordFileService recordFileService = SpringBeanService.getBean(RecordFileService.class);
        RecordFile recordFile = new RecordFile();
        recordFile.setTerminal(new Terminal(terminalInfo.getTerminalId()));
        recordFile.setStorageType(FileStorageEnum.STORAGE_SERVER);
        recordFile.setId(recordFileId);
        recordFile.setAvItemType(avItemType);
        recordFile.setStreamType(streamType);
        recordFile.setChannelId(channelId);
        recordFile.setStartTime(startTime);
        recordFile.setEndTime(endTime);
        recordFile.setFilePath(baseDir);
        recordFileService.save(recordFile);
    }

    @Override
    public String toString() {
        Map<String, Object> msgBodyItems = new LinkedHashMap<>();
        msgBodyItems.put("服务器地址", serverIp);
        msgBodyItems.put("端口", serverPort);
        msgBodyItems.put("用户名", username);
        msgBodyItems.put("密码", password);
        msgBodyItems.put("文件上传路径", filePath);
        msgBodyItems.put("逻辑通道号", channelId);
        msgBodyItems.put("开始时间", startTime == 0 ? "0" : TimeUtil.formatTime(startTime));
        msgBodyItems.put("结束时间", endTime == 0 ? "0" : TimeUtil.formatTime(endTime));
        msgBodyItems.put("报警标志", String.format("0x%08x", alarmFlag));
        msgBodyItems.put("音视频资源类型", String.format("%d:%s", avItemType.getValue(), avItemType.getDesc()));
        msgBodyItems.put("码流类型", String.format("%d:%s", streamType.getValue(), streamType.getDesc()));
        msgBodyItems.put("存储器类型", String.format("%d:%s", memoryType.getValue(), memoryType.getDesc()));
        msgBodyItems.put("任务执行条件", String.format("0x%02x", taskCondition));
        return msgBodyItems.toString();
    }
}