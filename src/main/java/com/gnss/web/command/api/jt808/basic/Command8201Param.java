package com.gnss.web.command.api.jt808.basic;

import com.gnss.core.annotations.DownCommand;
import com.gnss.core.service.IDownCommandMessage;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * <p>Description: JT808 0x8201指令参数</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2018-9-14
 */
@ApiModel("0x8201位置信息查询")
@Data
@DownCommand(messageId = 0x8201, respMessageId = 0x0201, desc = "位置信息查询")
public class Command8201Param implements IDownCommandMessage {

}
