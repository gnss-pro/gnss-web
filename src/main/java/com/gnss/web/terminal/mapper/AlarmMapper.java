package com.gnss.web.terminal.mapper;

import com.gnss.core.utils.TimeUtil;
import com.gnss.web.constants.AlarmSourceEnum;
import com.gnss.web.info.domain.Terminal;
import com.gnss.web.terminal.api.AlarmDTO;
import com.gnss.web.terminal.domain.Alarm;
import com.gnss.web.utils.GnssUtil;

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
public class AlarmMapper {

    public static AlarmDTO fromAlarm(Alarm alarm) {
        if (alarm == null) {
            return null;
        }

        AlarmDTO alarmDTO = new AlarmDTO();

        alarmDTO.setHandleUserName(alarmHandleByName(alarm));
        alarmDTO.setVehicleNum(alarmTerminalVehicleNum(alarm));
        if (alarm.getId() != null) {
            alarmDTO.setAlarmId(String.valueOf(alarm.getId()));
        }
        alarmDTO.setSimNum(alarmTerminalSimNum(alarm));
        alarmDTO.setAlarmSource(alarmAlarmSource(alarm));
        alarmDTO.setIsHandle(alarmIsHandle(alarm));
        alarmDTO.setStartLocation(alarm.getStartLocation());
        alarmDTO.setEndLocation(alarm.getEndLocation());

        alarmDTO.setPlateColor(alarm.getTerminal().getVehiclePlateColor().getDesc());
        alarmDTO.setAlarmDuration(GnssUtil.formatTimeDuration(alarm.getAlarmDuration()));
        alarmDTO.setAlarmType(alarm.getAlarmType().getDesc());
        alarmDTO.setStartTime(TimeUtil.formatTime(alarm.getStartTime()));
        alarmDTO.setEndTime(TimeUtil.formatTime(alarm.getEndTime()));

        return alarmDTO;
    }

    public static List<AlarmDTO> fromAlarms(List<Alarm> alarmsList) {
        if (alarmsList == null) {
            return null;
        }

        List<AlarmDTO> list = new ArrayList<AlarmDTO>(alarmsList.size());
        for (Alarm alarm : alarmsList) {
            list.add(fromAlarm(alarm));
        }

        return list;
    }

    private static String alarmHandleByName(Alarm alarm) {
        if (alarm == null) {
            return null;
        }
        String handleBy = alarm.getHandleBy();
        return handleBy;
    }

    private static String alarmTerminalVehicleNum(Alarm alarm) {
        if (alarm == null) {
            return null;
        }
        Terminal terminal = alarm.getTerminal();
        if (terminal == null) {
            return null;
        }
        String vehicleNum = terminal.getVehicleNum();
        return vehicleNum;
    }

    private static String alarmTerminalSimNum(Alarm alarm) {
        if (alarm == null) {
            return null;
        }
        Terminal terminal = alarm.getTerminal();
        if (terminal == null) {
            return null;
        }
        String simNum = terminal.getPhoneNum();
        return simNum;
    }

    private static String alarmAlarmSource(Alarm alarm) {
        if (alarm == null) {
            return null;
        }
        AlarmSourceEnum alarmSource = alarm.getAlarmSource();
        return alarmSource.getDesc();
    }

    private static String alarmIsHandle(Alarm alarm) {
        if (alarm == null) {
            return null;
        }
        int isHandle = alarm.getIsHandle();
        return isHandle == 0 ? "未处理" : "已处理";
    }
}
