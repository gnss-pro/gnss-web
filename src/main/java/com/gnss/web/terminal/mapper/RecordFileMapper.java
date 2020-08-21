package com.gnss.web.terminal.mapper;

import com.gnss.core.constants.VehiclePlateColorEnum;
import com.gnss.core.utils.TimeUtil;
import com.gnss.web.info.domain.Terminal;
import com.gnss.web.terminal.api.RecordFileDTO;
import com.gnss.web.terminal.domain.RecordFile;

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
public class RecordFileMapper {

    public static RecordFileDTO fromRecordFile(RecordFile recordFile) {
        if (recordFile == null) {
            return null;
        }

        RecordFileDTO recordFileDTO = new RecordFileDTO();
        recordFileDTO.setSimNum(recordFileTerminalSimNum(recordFile));
        if (recordFile.getId() != null) {
            recordFileDTO.setRecordFileId(String.valueOf(recordFile.getId()));
        }
        recordFileDTO.setVehicleNum(recordFileTerminalVehicleNum(recordFile));
        recordFileDTO.setPlateColor(recordFileTerminalPlateColor(recordFile));
        recordFileDTO.setCreatedDate(recordFile.getCreatedDate());
        recordFileDTO.setLastModifiedDate(recordFile.getLastModifiedDate());
        recordFileDTO.setChannelId(recordFile.getChannelId());
        recordFileDTO.setFileName(recordFile.getFileName());
        recordFileDTO.setFileSize(recordFile.getFileSize());
        recordFileDTO.setBase64FilePath(recordFile.getBase64FilePath());
        recordFileDTO.setStorageType(recordFile.getStorageType().getDesc());
        recordFileDTO.setAvItemType(recordFile.getAvItemType() == null ? "" : recordFile.getAvItemType().name());
        recordFileDTO.setAvItemTypeDesc(recordFile.getAvItemType() == null ? "" : recordFile.getAvItemType().getDesc());
        recordFileDTO.setMsgFlowId(recordFile.getMsgFlowId());
        recordFileDTO.setFileStatus(recordFile.getFileStatus());
        recordFileDTO.setStartTime(recordFile.getStartTime() == null ? "" : TimeUtil.formatTime(recordFile.getStartTime()));
        recordFileDTO.setEndTime(recordFile.getEndTime() == null ? "" : TimeUtil.formatTime(recordFile.getEndTime()));
        return recordFileDTO;
    }

    private static String recordFileTerminalSimNum(RecordFile recordFile) {
        if (recordFile == null) {
            return null;
        }
        Terminal terminal = recordFile.getTerminal();
        if (terminal == null) {
            return null;
        }
        String simNum = terminal.getPhoneNum();
        if (simNum == null) {
            return null;
        }
        return simNum;
    }

    private static String recordFileTerminalVehicleNum(RecordFile recordFile) {
        if (recordFile == null) {
            return null;
        }
        Terminal terminal = recordFile.getTerminal();
        if (terminal == null) {
            return null;
        }
        String vehicleNum = terminal.getVehicleNum();
        if (vehicleNum == null) {
            return null;
        }
        return vehicleNum;
    }

    private static String recordFileTerminalPlateColor(RecordFile recordFile) {
        if (recordFile == null) {
            return null;
        }
        Terminal terminal = recordFile.getTerminal();
        if (terminal == null) {
            return null;
        }
        VehiclePlateColorEnum vehiclePlateColor = terminal.getVehiclePlateColor();
        if (vehiclePlateColor == null) {
            return null;
        }
        return vehiclePlateColor.getDesc();
    }

    private static String recordFileTerminalTerminalNum(RecordFile recordFile) {
        if (recordFile == null) {
            return null;
        }
        Terminal terminal = recordFile.getTerminal();
        if (terminal == null) {
            return null;
        }
        String terminalNum = terminal.getTerminalNum();
        if (terminalNum == null) {
            return null;
        }
        return terminalNum;
    }

    private static Long mediaFileTerminalId(RecordFile recordFile) {
        if (recordFile == null) {
            return null;
        }
        Terminal terminal = recordFile.getTerminal();
        if (terminal == null) {
            return null;
        }
        Long id = terminal.getId();
        if (id == null) {
            return null;
        }
        return id;
    }

    public static List<RecordFileDTO> fromRecordFileList(List<RecordFile> recordFiles) {
        if (recordFiles == null || recordFiles.isEmpty()) {
            return Collections.emptyList();
        }
        return recordFiles.stream().map(RecordFileMapper::fromRecordFile).collect(Collectors.toList());
    }
}
