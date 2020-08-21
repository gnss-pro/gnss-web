package com.gnss.web.consumer;

import com.alibaba.fastjson.JSON;
import com.gnss.core.constants.AlarmTypeEnum;
import com.gnss.core.constants.TerminalStatusEnum;
import com.gnss.core.proto.LocationProto;
import com.gnss.core.proto.TerminalStatusProto;
import com.gnss.web.common.constant.GnssConstants;
import com.gnss.web.constants.RabbitConstants;
import com.gnss.web.global.service.CacheService;
import com.gnss.web.terminal.service.AlarmService;
import com.gnss.web.terminal.service.LocationService;
import com.gnss.web.terminal.service.TerminalStatusService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

/**
 * <p>Description: 终端状态订阅</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2019-1-18
 */
@Component
@Slf4j
@RabbitListener(queues = RabbitConstants.TERMINAL_STATUS_QUEUE)
public class TerminalStatusReceiver {

    /**
     * 位置计数器
     */
    private LongAdder locationCounter = new LongAdder();

    @Autowired
    private TerminalStatusService terminalStatusService;

    @Autowired
    private AlarmService alarmService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private CacheService cacheService;

    @RabbitHandler
    public void handleTerminalStatus(TerminalStatusProto terminalStatusProto, Channel channel, Message message) throws Exception {
        try {
            TerminalStatusEnum terminalStatus = terminalStatusProto.getTerminalStatus();
            if (terminalStatus == TerminalStatusEnum.SERVER_STARTUP || terminalStatus == TerminalStatusEnum.SERVER_SHUTDOWN) {
                String nodeName = terminalStatusProto.getNodeName();
                //网关启动或关闭事件,如果JT808节点则更新节点下的所有终端为离线
                if (nodeName.startsWith(GnssConstants.JT808_SERVER_NAME)) {
                    terminalStatusService.updateAllOffline(terminalStatus, nodeName);
                }
                //更新缓存的服务器状态信息
                cacheService.updateServerInfo(terminalStatus, terminalStatusProto.getServerStatus());
            } else if (terminalStatus == TerminalStatusEnum.OFFLINE) {
                //更新某台终端离线
                terminalStatusService.updateOffline(terminalStatusProto.getTerminalInfo());
            } else if (terminalStatus == TerminalStatusEnum.ONLINE) {
                //更新某台终端在线
                terminalStatusService.updateOnline(terminalStatusProto.getTerminalInfo());
            } else if (terminalStatus == TerminalStatusEnum.LOCATION) {
                locationCounter.increment();
                //处理实时位置
                handleLocation(terminalStatusProto);
            } else if (terminalStatus == TerminalStatusEnum.HISTORY_LOCATION) {
                //盲区补报
                locationService.save(terminalStatusProto);
            }
        } catch (Exception e) {
            log.error("更新终端状态异常:{}", terminalStatusProto, e);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
    }

    /**
     * 重置位置计数
     */
    public void resetLocationCounter() {
        locationCounter.reset();
        log.info("重置位置计数器");
    }

    /**
     * 获取收到位置数量
     *
     * @return
     */
    public long getLocationCount() {
        long count = locationCounter.longValue();
        return count;
    }

    /**
     * 处理实时位置
     * @param terminalStatusProto
     */
    private void handleLocation(TerminalStatusProto terminalStatusProto) {
        LocationProto location = terminalStatusProto.getLocation();
        //防止判断状态位时空指针
        if (location.getStatusBits() == null) {
            location.setStatusBits(Collections.emptyList());
        }
        //防止判断报警位时空指针
        if (location.getAlarmBits() == null) {
            location.setAlarmBits(Collections.emptyList());
        }
        //防止判断JT1078报警位时空指针
        if (location.getJt1078AlarmBits() == null) {
            location.setJt1078AlarmBits(Collections.emptyList());
        }
        location.setStatusBitsJson(JSON.toJSONString(location.getStatusBits()));
        location.setAlarmBitsJson(JSON.toJSONString(location.getAlarmBits()));
        location.setJt1078AlarmBitsJson(JSON.toJSONString(location.getJt1078AlarmBits()));
        //获取JT808和JT1078的报警类型
        List<AlarmTypeEnum> alarmTypeList = getAlarmTypeList(location.getAlarmBits(), location.getJt1078AlarmBits());
        location.setAlarmTypeList(alarmTypeList);

        //获取上一次终端状态
        TerminalStatusProto prevTerminalStatus = terminalStatusService.getLastStatus(terminalStatusProto.getTerminalInfo().getTerminalId());
        //更新实时位置状态
        terminalStatusService.update(terminalStatusProto);
        //处理报警
        alarmService.handleAlarm(prevTerminalStatus, terminalStatusProto);
        //保存位置
        locationService.save(terminalStatusProto);
    }

    /**
     * 获取JT808和JT1078的报警类型
     * @param jt808AlarmBits
     * @param jt1078AlarmBits
     * @return
     */
    private List<AlarmTypeEnum> getAlarmTypeList(List<Integer> jt808AlarmBits, List<Integer> jt1078AlarmBits) {
        List<AlarmTypeEnum> jt808AlarmList = jt808AlarmBits.stream()
                .map(alarmBit -> {
                    AlarmTypeEnum alarmType = null;
                    try {
                        alarmType  = AlarmTypeEnum.valueOf("ALARM_" + alarmBit);
                    } catch (Exception e) {
                        alarmType = AlarmTypeEnum.UNKNOWN;
                    }
                    return alarmType;
                })
                .filter(alarmType -> alarmType != AlarmTypeEnum.UNKNOWN)
                .collect(Collectors.toList());
        List<AlarmTypeEnum> jt1078AlarmList = jt1078AlarmBits.stream()
                .map(alarmBit -> {
                    AlarmTypeEnum alarmType = null;
                    try {
                        alarmType  = AlarmTypeEnum.valueOf("JT1078_ALARM_" + alarmBit);
                    } catch (Exception e) {
                        alarmType = AlarmTypeEnum.UNKNOWN;
                    }
                    return alarmType;
                })
                .filter(alarmType -> alarmType != AlarmTypeEnum.UNKNOWN)
                .collect(Collectors.toList());
        jt808AlarmList.addAll(jt1078AlarmList);
        return jt808AlarmList;
    }
}
