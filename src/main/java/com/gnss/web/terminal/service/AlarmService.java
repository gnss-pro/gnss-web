package com.gnss.web.terminal.service;

import com.gnss.core.constants.AlarmTypeEnum;
import com.gnss.core.proto.ActiveSafetyAlarmProto;
import com.gnss.core.proto.AlarmFlagProto;
import com.gnss.core.proto.LocationProto;
import com.gnss.core.proto.TerminalProto;
import com.gnss.core.proto.TerminalStatusProto;
import com.gnss.web.common.service.BaseService;
import com.gnss.web.constants.AlarmSourceEnum;
import com.gnss.web.info.domain.Terminal;
import com.gnss.web.terminal.dao.AlarmRepository;
import com.gnss.web.terminal.domain.ActiveSafetyAlarm;
import com.gnss.web.terminal.domain.Alarm;
import com.gnss.web.terminal.mapper.ActiveSafetyAlarmMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.partitioningBy;

/**
 * <p>Description: 报警服务</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2019-04-08
 */
@Service
@Slf4j
public class AlarmService extends BaseService<Alarm> {

    @Autowired
    private AlarmRepository alarmRepository;

    @Autowired
    private ActiveSafetyAlarmService activeSafetyAlarmService;

    @Autowired
    private GeocoderService geocoderService;

    /**
     * 批量删除报警
     *
     * @param idList
     */
    public int batchDelete(List<Long> idList) {
        return alarmRepository.deleteByIdIn(idList);
    }

    /**
     * 处理报警
     *
     * @param prevTerminalStatus  上一次终端状态
     * @param terminalStatusProto 当前终端状态
     * @return
     */
    @Async
    public boolean handleAlarm(TerminalStatusProto prevTerminalStatus, TerminalStatusProto terminalStatusProto) {
        //获取上一个位置信息
        TerminalProto terminalInfo = terminalStatusProto.getTerminalInfo();
        LocationProto prevLocation = prevTerminalStatus == null ? null : prevTerminalStatus.getLocation();
        //最新位置的时间不能小于上一个位置的时间
        LocationProto currLocation = terminalStatusProto.getLocation();
        if (prevLocation != null && currLocation.getTime() < prevLocation.getTime()) {
            log.error("当前定位时间小于上一次定位时间,终端手机号:{},上一次位置:{},当前位置:{}", terminalInfo.getTerminalSimCode(), prevLocation, currLocation);
            return false;
        }
        String locationDesc = handleJt808Alarm(terminalInfo, prevLocation, currLocation);
        //处理主动安全报警
        handleActiveSafetyAlarm(terminalInfo, prevLocation, currLocation, locationDesc);
        return true;
    }

    /**
     * 处理报警(终端产生)
     *
     * @param terminalInfo 终端信息
     * @param prevLocation 上一个位置
     * @param currLocation 当前位置
     * @return
     */
    public String handleJt808Alarm(TerminalProto terminalInfo, LocationProto prevLocation, LocationProto currLocation) {
        List<AlarmTypeEnum> currAlarmTypeList = currLocation.getAlarmTypeList();
        String locationDesc = null;
        //上一次位置不存在,直接新增报警
        if (prevLocation == null) {
            if (currAlarmTypeList.isEmpty()) {
                return locationDesc;
            }
            //查询百度地理位置描述
            locationDesc = geocoderService.getBaiduAddress(currLocation.getLat(), currLocation.getLon());
            startAlarm(terminalInfo, AlarmSourceEnum.TERMINAL, currLocation, currAlarmTypeList, locationDesc);
            return locationDesc;
        }

        List<AlarmTypeEnum> prevAlarmTypeList = prevLocation.getAlarmTypeList();
        Map<Boolean, List<AlarmTypeEnum>> groups = currAlarmTypeList.stream()
                .collect(partitioningBy(alarmTypeEnum -> prevAlarmTypeList.contains(alarmTypeEnum)));
        //当前位置与上一个位置的交集
        List<AlarmTypeEnum> intersectList = groups.get(Boolean.TRUE);
        //当前位置与上一个位置的差集
        List<AlarmTypeEnum> currSubstractList = groups.get(Boolean.FALSE);
        //上一个位置与当前位置的差集
        List<AlarmTypeEnum> prevSubstractList = prevAlarmTypeList.stream()
                .filter(t -> !intersectList.contains(t))
                .collect(Collectors.toList());
        if (prevSubstractList.isEmpty() && currSubstractList.isEmpty()) {
            return locationDesc;
        }
        //查询百度地理位置描述
        locationDesc = geocoderService.getBaiduAddress(currLocation.getLat(), currLocation.getLon());
        //停止报警
        stopAlarm(terminalInfo, currLocation, prevSubstractList, locationDesc);
        //开始报警
        startAlarm(terminalInfo, AlarmSourceEnum.TERMINAL, currLocation, currSubstractList, locationDesc);
        return locationDesc;
    }

    /**
     * 处理主动安全报警
     *
     * @param terminalInfo 终端信息
     * @param prevLocation 上一个位置
     * @param currLocation 当前位置
     * @param locationDesc 位置描述
     */
    private void handleActiveSafetyAlarm(TerminalProto terminalInfo, LocationProto prevLocation, LocationProto currLocation, String locationDesc) {
        //停止上一次报警
        if (prevLocation != null && CollectionUtils.isNotEmpty(prevLocation.getSafetyAlarmTypeList())) {
            List<AlarmTypeEnum> stopAlarmList = prevLocation.getSafetyAlarmTypeList();
            if (locationDesc == null) {
                locationDesc = geocoderService.getBaiduAddress(currLocation.getLat(), currLocation.getLon());
            }
            stopAlarm(terminalInfo, currLocation, stopAlarmList, locationDesc);
            log.info("停止上一次主动安全报警,终端手机号:{},报警类型:{}", terminalInfo.getTerminalSimCode(), stopAlarmList);
        }
        if (currLocation.getActiveSafetyAlarms().isEmpty()) {
            return;
        }
        //查询位置描述
        if (locationDesc == null) {
            locationDesc = geocoderService.getBaiduAddress(currLocation.getLat(), currLocation.getLon());
        }
        //保存主动安全报警并发送附件上传指令
        String finalLocationDesc = locationDesc;
        currLocation.getActiveSafetyAlarms().forEach(activeSafetyAlarmProto -> {
            AlarmFlagProto alarmFlag = activeSafetyAlarmProto.getAlarmFlag();
            Alarm alarm = buildAlarm(terminalInfo.getTerminalId(), AlarmSourceEnum.TERMINAL, activeSafetyAlarmProto.getAlarmType(), currLocation, activeSafetyAlarmProto, finalLocationDesc);
            alarm = save(alarm);
            log.info("保存主动安全报警,终端手机号:{},报警信息:{}", terminalInfo.getTerminalSimCode(), activeSafetyAlarmProto);
            //附件数量大于0时通知上传主动安全报警附件
            if (activeSafetyAlarmProto.getAttachmentCount() > 0) {
                activeSafetyAlarmService.notifyUploadAttachment(terminalInfo, alarm.getActiveSafetyAlarm(), alarmFlag);
            }
        });
    }

    /**
     * 开始报警
     *
     * @param terminalInfo  终端信息
     * @param alarmSource   报警来源
     * @param location      位置
     * @param alarmTypeList 报警列表
     * @param locationDesc  位置描述
     */
    private void startAlarm(TerminalProto terminalInfo, AlarmSourceEnum alarmSource, LocationProto location, List<AlarmTypeEnum> alarmTypeList, String locationDesc) {
        if (alarmTypeList.isEmpty()) {
            return;
        }
        Long terminalId = terminalInfo.getTerminalId();
        List<Alarm> alarmList = alarmTypeList.stream()
                .map(alarmType -> buildAlarm(terminalId, alarmSource, alarmType, location, null, locationDesc))
                .collect(Collectors.toList());
        save(alarmList);
        log.info("开始报警,报警来源:{},终端手机号:{},报警列表:{}", alarmSource, terminalInfo.getTerminalSimCode(), alarmTypeList);
    }

    /**
     * 停止报警
     *
     * @param terminalInfo  终端信息
     * @param location      位置
     * @param alarmTypeList 报警列表
     * @param locationDesc  位置描述
     */
    private void stopAlarm(TerminalProto terminalInfo, LocationProto location, List<AlarmTypeEnum> alarmTypeList, String locationDesc) {
        if (alarmTypeList.isEmpty()) {
            return;
        }

        //根据报警列表查询未结束的报警
        Terminal terminal = new Terminal(terminalInfo.getTerminalId());
        List<Alarm> alarmList = alarmRepository.findByTerminalAndEndTimeIsNullAndAlarmTypeIn(terminal, alarmTypeList);
        if (alarmList.isEmpty()) {
            return;
        }

        //更新结束报警的信息
        alarmList.forEach(alarm -> {
            alarm.setEndLat(location.getLat());
            alarm.setEndLon(location.getLon());
            alarm.setEndSpeed(location.getSpeed());
            alarm.setEndRecoderSpeed(location.getRecoderSpeed());
            alarm.setEndExtraInfo(location.getExtraInfo());
            alarm.setEndTime(location.getTime());
            alarm.setEndStatus(location.getStatus());
            alarm.setEndStatusBits(location.getStatusBitsJson());
            alarm.setEndAlarmFlag(location.getAlarmFlag());
            alarm.setEndAlarmBits(location.getAlarmBitsJson());
            alarm.setEndJt1078AlarmFlag(location.getJt1078AlarmFlag());
            alarm.setEndJt1078AlarmBits(location.getJt1078AlarmBitsJson());
            alarm.setEndLocation(locationDesc);
            //计算报警时长,单位秒
            alarm.setAlarmDuration(Math.abs(alarm.getStartTime() - location.getTime()) / 1000);
        });
        save(alarmList);
        log.info("停止报警,终端手机号:{},报警列表:{}", terminalInfo.getTerminalSimCode(), alarmTypeList);
    }

    /**
     * 构造报警信息
     *
     * @param terminalId
     * @param alarmSource
     * @param alarmType
     * @param location
     * @param activeSafetyAlarmProto
     * @param locationDesc
     * @return
     */
    private Alarm buildAlarm(Long terminalId, AlarmSourceEnum alarmSource, AlarmTypeEnum alarmType, LocationProto location, ActiveSafetyAlarmProto activeSafetyAlarmProto, String locationDesc) {
        Alarm alarm = new Alarm();
        Terminal terminal = new Terminal(terminalId);
        alarm.setTerminal(terminal);
        alarm.setAlarmSource(alarmSource);
        alarm.setAlarmType(alarmType);
        alarm.setStartLat(location.getLat());
        alarm.setStartLon(location.getLon());
        alarm.setStartSpeed(location.getSpeed());
        alarm.setStartRecoderSpeed(location.getRecoderSpeed());
        alarm.setStartTime(location.getTime());
        alarm.setStartStatus(location.getStatus());
        alarm.setStartStatusBits(location.getStatusBitsJson());
        alarm.setStartAlarmFlag(location.getAlarmFlag());
        alarm.setStartAlarmBits(location.getAlarmBitsJson());
        alarm.setStartJt1078AlarmFlag(location.getJt1078AlarmFlag());
        alarm.setStartJt1078AlarmBits(location.getJt1078AlarmBitsJson());
        alarm.setStartExtraInfo(location.getExtraInfo());
        alarm.setStartLocation(locationDesc);
        //主动安全报警
        if (activeSafetyAlarmProto != null) {
            ActiveSafetyAlarm activeSafetyAlarm = ActiveSafetyAlarmMapper.toActiveSafetyAlarm(activeSafetyAlarmProto);
            alarm.setAlarmLevel(activeSafetyAlarmProto.getAlarmLevel());
            alarm.setActiveSafetyAlarm(activeSafetyAlarm);
            activeSafetyAlarm.setTerminalAlarm(alarm);
            activeSafetyAlarm.setTerminal(terminal);
        }
        return alarm;
    }
}