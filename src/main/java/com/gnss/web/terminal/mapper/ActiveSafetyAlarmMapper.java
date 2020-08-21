package com.gnss.web.terminal.mapper;

import com.alibaba.fastjson.JSON;
import com.gnss.core.proto.ActiveSafetyAlarmProto;
import com.gnss.core.utils.TimeUtil;
import com.gnss.web.info.domain.Terminal;
import com.gnss.web.terminal.api.SafetyAlarmDetailDTO;
import com.gnss.web.terminal.domain.ActiveSafetyAlarm;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Description: 由程序自动生成</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2019/10/14
 */
public class ActiveSafetyAlarmMapper {

    public static ActiveSafetyAlarm toActiveSafetyAlarm(ActiveSafetyAlarmProto activeSafetyAlarmProto) {
        if (activeSafetyAlarmProto == null) {
            return null;
        }

        ActiveSafetyAlarm activeSafetyAlarm = new ActiveSafetyAlarm();
        activeSafetyAlarm.setTime(activeSafetyAlarmProto.getTimestamp());
        activeSafetyAlarm.setAlarmType(activeSafetyAlarmProto.getAlarmType());
        activeSafetyAlarm.setActiveSafetyType(activeSafetyAlarmProto.getActiveSafetyType());
        activeSafetyAlarm.setAlarmId(activeSafetyAlarmProto.getAlarmId());
        activeSafetyAlarm.setFlagStatus(activeSafetyAlarmProto.getFlagStatus());
        activeSafetyAlarm.setEventType(activeSafetyAlarmProto.getEventType());
        activeSafetyAlarm.setAlarmLevel(activeSafetyAlarmProto.getAlarmLevel());
        activeSafetyAlarm.setSpeed(activeSafetyAlarmProto.getSpeed());
        activeSafetyAlarm.setAltitude(activeSafetyAlarmProto.getAltitude());
        activeSafetyAlarm.setLat(activeSafetyAlarmProto.getLat());
        activeSafetyAlarm.setLon(activeSafetyAlarmProto.getLon());
        if (activeSafetyAlarmProto.getTime() != null) {
            activeSafetyAlarm.setTime(Long.parseLong(activeSafetyAlarmProto.getTime()));
        }
        activeSafetyAlarm.setVehicleStatus(activeSafetyAlarmProto.getVehicleStatus());
        activeSafetyAlarm.setExtraData(activeSafetyAlarmProto.getExtraData());
        activeSafetyAlarm.setAttachmentCount(activeSafetyAlarmProto.getAttachmentCount());

        activeSafetyAlarm.setVehicleStatusBits(JSON.toJSONString(activeSafetyAlarmProto.getVehicleStatusBits()));
        activeSafetyAlarm.setAlarmFlag(JSON.toJSONString(activeSafetyAlarmProto.getAlarmFlag()));

        return activeSafetyAlarm;
    }

    public static List<ActiveSafetyAlarm> toActiveSafetyAlarms(List<ActiveSafetyAlarmProto> activeSafetyAlarmProtos) {
        if (activeSafetyAlarmProtos == null) {
            return null;
        }

        List<ActiveSafetyAlarm> list = new ArrayList<ActiveSafetyAlarm>(activeSafetyAlarmProtos.size());
        for (ActiveSafetyAlarmProto activeSafetyAlarmProto : activeSafetyAlarmProtos) {
            list.add(toActiveSafetyAlarm(activeSafetyAlarmProto));
        }

        return list;
    }

    public static List<SafetyAlarmDetailDTO> fromAlarms(List<ActiveSafetyAlarm> alarmList) {
        if (alarmList == null) {
            return null;
        }

        List<SafetyAlarmDetailDTO> list = new ArrayList<>(alarmList.size());
        for (ActiveSafetyAlarm activeSafetyAlarm : alarmList) {
            list.add(fromAlarm(activeSafetyAlarm));
        }

        return list;
    }

    public static SafetyAlarmDetailDTO fromAlarm(ActiveSafetyAlarm alarm) {
        if (alarm == null) {
            return null;
        }

        SafetyAlarmDetailDTO safetyAlarmDetailDTO = new SafetyAlarmDetailDTO();

        if (alarm.getId() != null) {
            safetyAlarmDetailDTO.setAlarmId(String.valueOf(alarm.getId()));
        }
        safetyAlarmDetailDTO.setSimNum(alarmTerminalSimNum(alarm));
        safetyAlarmDetailDTO.setVehicleNum(alarmTerminalVehicleNum(alarm));
        safetyAlarmDetailDTO.setAlarmLevel(alarm.getAlarmLevel());
        safetyAlarmDetailDTO.setSpeed(alarm.getSpeed());
        safetyAlarmDetailDTO.setAltitude(alarm.getAltitude());
        safetyAlarmDetailDTO.setLat(alarm.getLat());
        safetyAlarmDetailDTO.setLon(alarm.getLon());
        safetyAlarmDetailDTO.setVehicleStatus(alarm.getVehicleStatus());
        safetyAlarmDetailDTO.setVehicleStatusBits(alarm.getVehicleStatusBits());
        safetyAlarmDetailDTO.setCompletedCount(alarm.getCompletedCount());
        safetyAlarmDetailDTO.setAttachmentCount(alarm.getAttachmentCount());
        safetyAlarmDetailDTO.setExtraData(alarm.getExtraData());

        safetyAlarmDetailDTO.setAlarmType(alarm.getAlarmType().getDesc());
        safetyAlarmDetailDTO.setActiveSafetyType(alarm.getActiveSafetyType().name());
        safetyAlarmDetailDTO.setActiveSafetyTypeDesc(alarm.getActiveSafetyType().getDesc());
        safetyAlarmDetailDTO.setTime(TimeUtil.formatTime(alarm.getTime()));
        safetyAlarmDetailDTO.setPlateColor(alarm.getTerminal().getVehiclePlateColor().getDesc());

        return safetyAlarmDetailDTO;
    }

    private static String alarmTerminalSimNum(ActiveSafetyAlarm activeSafetyAlarm) {
        if (activeSafetyAlarm == null) {
            return null;
        }
        Terminal terminal = activeSafetyAlarm.getTerminal();
        if (terminal == null) {
            return null;
        }
        String simNum = terminal.getPhoneNum();
        if (simNum == null) {
            return null;
        }
        return simNum;
    }

    private static String alarmTerminalVehicleNum(ActiveSafetyAlarm activeSafetyAlarm) {
        if (activeSafetyAlarm == null) {
            return null;
        }
        Terminal terminal = activeSafetyAlarm.getTerminal();
        if (terminal == null) {
            return null;
        }
        String vehicleNum = terminal.getVehicleNum();
        if (vehicleNum == null) {
            return null;
        }
        return vehicleNum;
    }
}
