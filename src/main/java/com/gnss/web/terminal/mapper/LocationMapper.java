package com.gnss.web.terminal.mapper;

import com.gnss.core.utils.TimeUtil;
import com.gnss.web.info.domain.Terminal;
import com.gnss.web.terminal.api.TrackDTO;
import com.gnss.web.terminal.domain.Location;

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
public class LocationMapper {

    public static List<TrackDTO> fromLocations(List<Location> locationList) {
        if (locationList == null) {
            return null;
        }

        List<TrackDTO> list = new ArrayList<>(locationList.size());
        for (Location location : locationList) {
            list.add(locationToTrackDTO(location));
        }

        return list;
    }

    protected static TrackDTO locationToTrackDTO(Location location) {
        if (location == null) {
            return null;
        }
        Terminal terminal = location.getTerminal();

        TrackDTO trackDTO = new TrackDTO();
        if (terminal != null) {
            trackDTO.setVehicleNum(terminal.getVehicleNum());
            trackDTO.setSimNum(terminal.getPhoneNum());
            trackDTO.setPlateColor(terminal.getVehiclePlateColor().getDesc());
        }
        trackDTO.setLocationId(location.getId().toString());
        trackDTO.setGpsValid(location.getGpsValid());
        trackDTO.setLat(location.getLat());
        trackDTO.setLon(location.getLon());
        trackDTO.setAltitude(location.getAltitude());
        trackDTO.setSpeed(location.getSpeed());
        trackDTO.setDirection(location.getDirection());
        trackDTO.setMileage(location.getMileage());
        trackDTO.setFuel(location.getFuel());
        if (location.getTime() != null) {
            trackDTO.setGpsTime(TimeUtil.formatTime(location.getTime()));
        }
        trackDTO.setRecoderSpeed(location.getRecoderSpeed());
        trackDTO.setStatus(location.getStatus());
        trackDTO.setAlarmFlag(location.getAlarmFlag());
        trackDTO.setJt1078AlarmFlag(location.getJt1078AlarmFlag());
        trackDTO.setExtraInfo(location.getExtraInfo());

        return trackDTO;
    }
}