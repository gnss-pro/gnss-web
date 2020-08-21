package com.gnss.web.command.api.jt808.safety;

import com.gnss.core.annotations.DownCommand;
import com.gnss.core.exception.ApplicationException;
import com.gnss.core.model.config.FileServerConfig;
import com.gnss.core.proto.TerminalProto;
import com.gnss.core.service.IDownCommandMessage;
import com.gnss.core.service.SpringBeanService;
import com.gnss.core.utils.CommonUtil;
import com.gnss.web.global.service.CacheService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCountUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>Description: JT808 0x9208指令参数</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2018-9-14
 */
@ApiModel("0x9208报警附件上传指令")
@Data
@DownCommand(messageId = 0x9208, respMessageId = 0x0001, desc = "报警附件上传指令")
public class Command9208Param implements IDownCommandMessage {

    @ApiModelProperty(value = "附件服务器IP", hidden = true)
    private String serverIp;

    @ApiModelProperty(value = "附件服务器TCP端口", hidden = true)
    private int serverTcpPort;

    @ApiModelProperty(value = "附件服务器UDP端口", hidden = true)
    private int serverUdpPort;

    @ApiModelProperty(value = "报警标识号", required = true, position = 4)
    @NotNull(message = "报警标识号不能为空")
    private byte[] alarmFlagArr;

    @ApiModelProperty(value = "报警编号", required = true, position = 5)
    @NotBlank(message = "报警编号不能为空")
    private String alarmNum;

    @Override
    public byte[] buildMessageBody(TerminalProto terminalInfo) throws Exception {
        //从缓存查询文件服务器配置
        CacheService cacheService = SpringBeanService.getBean(CacheService.class);
        FileServerConfig fileServerConfig = cacheService.getFileServerInfo();
        if (fileServerConfig == null || !fileServerConfig.getIsRunning()) {
            throw new ApplicationException("文件服务器未启动");
        }

        serverIp = fileServerConfig.getWanIp();
        serverTcpPort = fileServerConfig.getAttachmentPort();
        serverUdpPort = serverTcpPort;
        byte[] serverIpArr = serverIp.getBytes();
        ByteBuf msgBody = Unpooled.buffer(serverIpArr.length + 69);
        msgBody.writeByte(serverIpArr.length)
                .writeBytes(serverIpArr)
                .writeShort(serverTcpPort)
                .writeShort(serverUdpPort)
                .writeBytes(alarmFlagArr);
        CommonUtil.writeGbkString(msgBody, alarmNum, 32);
        msgBody.writeBytes(new byte[16]);
        byte[] msgBodyArr = msgBody.array();
        ReferenceCountUtil.release(msgBody);
        return msgBodyArr;
    }

    @Override
    public String toString() {
        Map<String, Object> msgBodyItems = new LinkedHashMap<>();
        msgBodyItems.put("附件服务器IP", serverIp);
        msgBodyItems.put("附件服务器TCP端口", serverTcpPort);
        msgBodyItems.put("附件服务器UDP端口", serverUdpPort);
        msgBodyItems.put("报警标识号", new String(alarmFlagArr).trim());
        msgBodyItems.put("报警编号", alarmNum);
        return msgBodyItems.toString();
    }
}