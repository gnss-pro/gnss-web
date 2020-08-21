package com.gnss.web.command.api.jt808.basic;

import com.gnss.core.annotations.DownCommand;
import com.gnss.core.service.IDownCommandMessage;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * <p>Description: JT808 0x8702指令参数</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2019-9-14
 */
@ApiModel("0x8702上报驾驶员身份信息请求")
@Data
@DownCommand(messageId = 0x8702, respMessageId = 0x0702, desc = "上报驾驶员身份信息请求")
public class Command8702Param implements IDownCommandMessage {

}