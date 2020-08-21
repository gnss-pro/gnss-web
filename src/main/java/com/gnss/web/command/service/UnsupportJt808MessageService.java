package com.gnss.web.command.service;

import com.gnss.core.model.jt808.Jt808Message;
import com.gnss.core.proto.TerminalProto;
import org.springframework.stereotype.Service;

/**
 * <p>Description: 未支持的JT808消息处理服务</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2019-1-18
 */
@Service
public class UnsupportJt808MessageService {

    public Object process(TerminalProto terminalInfo, Jt808Message jt808Msg) {
        int msgId = jt808Msg.getMsgId();
        if (msgId == 0x0200) {

        }
        return null;
    }

}