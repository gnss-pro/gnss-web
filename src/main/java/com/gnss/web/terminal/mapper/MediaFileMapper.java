package com.gnss.web.terminal.mapper;

import com.gnss.core.constants.VehiclePlateColorEnum;
import com.gnss.core.proto.LocationProto;
import com.gnss.core.proto.MediaFileProto;
import com.gnss.core.utils.TimeUtil;
import com.gnss.web.info.domain.Terminal;
import com.gnss.web.terminal.api.LocationDTO;
import com.gnss.web.terminal.api.MediaFileDTO;
import com.gnss.web.terminal.domain.Location;
import com.gnss.web.terminal.domain.MediaFile;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>Description: 由程序自动生成</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2019/10/14
 */
public class MediaFileMapper {

    public static MediaFile fromMediaFileProto(MediaFileProto mediaFileProto) {
        if (mediaFileProto == null) {
            return null;
        }

        MediaFile mediaFile = new MediaFile();

        mediaFile.setLocation(locationProtoToLocation(mediaFileProto.getLocation()));
        if (mediaFileProto.getMediaId() != null) {
            mediaFile.setMediaId(mediaFileProto.getMediaId());
        }
        mediaFile.setMediaType(mediaFileProto.getMediaType());
        mediaFile.setMediaFormatCode(mediaFileProto.getMediaFormatCode());
        mediaFile.setEventItemCode(mediaFileProto.getEventItemCode());
        if (mediaFileProto.getChannelId() != null) {
            mediaFile.setChannelId(mediaFileProto.getChannelId());
        }
        if (mediaFileProto.getFileSize() != null) {
            mediaFile.setFileSize(mediaFileProto.getFileSize());
        }

        return mediaFile;
    }

    public static Location locationProtoToLocation(LocationProto locationProto) {
        if (locationProto == null) {
            return null;
        }

        Location location = new Location();
        location.setStatusBits(locationProto.getStatusBitsJson());
        location.setAlarmBits(locationProto.getAlarmBitsJson());
        location.setSafetyAlarmList(locationProto.getSafetyAlarmListJson());
        if (locationProto.getGpsValid() != null) {
            location.setGpsValid(locationProto.getGpsValid());
        }
        location.setAcc(locationProto.getAcc());
        location.setLat(locationProto.getLat());
        location.setLon(locationProto.getLon());
        location.setAltitude(locationProto.getAltitude());
        location.setSpeed(locationProto.getSpeed());
        location.setDirection(locationProto.getDirection());
        location.setTime(locationProto.getTime());
        location.setMileage(locationProto.getMileage());
        location.setFuel(locationProto.getFuel());
        location.setRecoderSpeed(locationProto.getRecoderSpeed());
        location.setStatus(locationProto.getStatus());
        location.setAlarmFlag(locationProto.getAlarmFlag());
        location.setJt1078AlarmFlag(locationProto.getJt1078AlarmFlag());
        location.setExtraInfo(locationProto.getExtraInfo());
        return location;
    }

    public static MediaFileDTO fromMediaFile(MediaFile mediaFile) {
        if (mediaFile == null) {
            return null;
        }

        MediaFileDTO mediaFileDTO = new MediaFileDTO();
        mediaFileDTO.setSimNum(mediaFileTerminalSimNum(mediaFile));
        if (mediaFile.getId() != null) {
            mediaFileDTO.setMediaFileId(String.valueOf(mediaFile.getId()));
        }
        mediaFileDTO.setVehicleNum(mediaFileTerminalVehicleNum(mediaFile));
        mediaFileDTO.setPlateColor(mediaFileTerminalPlateColor(mediaFile));
        mediaFileDTO.setTerminalNum(mediaFileTerminalTerminalNum(mediaFile));
        Long id = mediaFileTerminalId(mediaFile);
        if (id != null) {
            mediaFileDTO.setTerminalId(String.valueOf(id));
        }
        mediaFileDTO.setCreatedDate(mediaFile.getCreatedDate());
        mediaFileDTO.setLastModifiedDate(mediaFile.getLastModifiedDate());
        mediaFileDTO.setMediaId(mediaFile.getMediaId());
        mediaFileDTO.setChannelId(mediaFile.getChannelId());
        if (mediaFile.getMediaType() != null) {
            mediaFileDTO.setMediaType(mediaFile.getMediaType().getDesc());
        }
        if (mediaFile.getMediaFormatCode() != null) {
            mediaFileDTO.setMediaFormatCode(mediaFile.getMediaFormatCode().getDesc());
        }
        if (mediaFile.getEventItemCode() != null) {
            mediaFileDTO.setEventItemCode(mediaFile.getEventItemCode().getDesc());
        }
        mediaFileDTO.setFileName(mediaFile.getFileName());
        mediaFileDTO.setFileSize(String.valueOf(mediaFile.getFileSize()));
        mediaFileDTO.setBase64FilePath(mediaFile.getBase64FilePath());
        mediaFileDTO.setLocation(locationToLocationDTO(mediaFile.getLocation()));
        return mediaFileDTO;
    }

    private static String mediaFileTerminalSimNum(MediaFile mediaFile) {
        if (mediaFile == null) {
            return null;
        }
        Terminal terminal = mediaFile.getTerminal();
        if (terminal == null) {
            return null;
        }
        String simNum = terminal.getPhoneNum();
        if (simNum == null) {
            return null;
        }
        return simNum;
    }

    private static String mediaFileTerminalVehicleNum(MediaFile mediaFile) {
        if (mediaFile == null) {
            return null;
        }
        Terminal terminal = mediaFile.getTerminal();
        if (terminal == null) {
            return null;
        }
        String vehicleNum = terminal.getVehicleNum();
        if (vehicleNum == null) {
            return null;
        }
        return vehicleNum;
    }

    private static String mediaFileTerminalPlateColor(MediaFile mediaFile) {
        if (mediaFile == null) {
            return null;
        }
        Terminal terminal = mediaFile.getTerminal();
        if (terminal == null) {
            return null;
        }
        VehiclePlateColorEnum vehiclePlateColor = terminal.getVehiclePlateColor();
        if (vehiclePlateColor == null) {
            return null;
        }
        return vehiclePlateColor.getDesc();
    }

    private static String mediaFileTerminalTerminalNum(MediaFile mediaFile) {
        if (mediaFile == null) {
            return null;
        }
        Terminal terminal = mediaFile.getTerminal();
        if (terminal == null) {
            return null;
        }
        String terminalNum = terminal.getTerminalNum();
        if (terminalNum == null) {
            return null;
        }
        return terminalNum;
    }

    private static Long mediaFileTerminalId(MediaFile mediaFile) {
        if (mediaFile == null) {
            return null;
        }
        Terminal terminal = mediaFile.getTerminal();
        if (terminal == null) {
            return null;
        }
        Long id = terminal.getId();
        if (id == null) {
            return null;
        }
        return id;
    }

    protected static LocationDTO locationToLocationDTO(Location location) {
        if (location == null) {
            return null;
        }

        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setLocationId(location.getId().toString());
        locationDTO.setGpsValid(location.getGpsValid());
        locationDTO.setLat(location.getLat());
        locationDTO.setLon(location.getLon());
        locationDTO.setAltitude(location.getAltitude());
        locationDTO.setSpeed(location.getSpeed());
        locationDTO.setDirection(location.getDirection());
        locationDTO.setMileage(location.getMileage());
        locationDTO.setFuel(location.getFuel());
        if (location.getTime() != null) {
            locationDTO.setGpsTime(TimeUtil.formatTime(location.getTime()));
        }
        locationDTO.setRecoderSpeed(location.getRecoderSpeed());
        locationDTO.setStatus(location.getStatus());
        locationDTO.setAlarmFlag(location.getAlarmFlag());
        locationDTO.setJt1078AlarmFlag(location.getJt1078AlarmFlag());
        locationDTO.setExtraInfo(location.getExtraInfo());

        return locationDTO;
    }

    public static List<MediaFileDTO> fromMediaFileList(List<MediaFile> content) {
        if (content == null || content.isEmpty()) {
            return Collections.emptyList();
        }
        return content.stream().map(MediaFileMapper::fromMediaFile).collect(Collectors.toList());
    }
}
