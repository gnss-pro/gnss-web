package com.gnss.web.command.api.jt808.media;

import com.gnss.core.annotations.DownCommand;
import com.gnss.core.service.IDownCommandMessage;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * <p>Description: JT808 0x9003指令参数</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2018-9-14
 */
@ApiModel("0x9003查询终端音视频属性")
@Data
@DownCommand(messageId = 0x9003, respMessageId = 0x1003, desc = "查询终端音视频属性")
public class Command9003Param implements IDownCommandMessage {

}