package com.gnss.web.terminal.service;

import com.gnss.core.constants.CommonConstant;
import com.gnss.core.constants.TerminalStatusEnum;
import com.gnss.core.constants.VehicleStatusEnum;
import com.gnss.core.proto.LocationProto;
import com.gnss.core.proto.TerminalProto;
import com.gnss.core.proto.TerminalStatusProto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * <p>Description: 终端状态服务</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2019/2/14
 */
@Service
@Slf4j
public class TerminalStatusService {

    /**
     * 终端最后状态Map
     */
    private Map<Long, TerminalStatusProto> lastStatusMap = new HashMap<>();

    /**
     * 更新终端状态
     *
     * @param terminalStatusProto
     */
    public void update(TerminalStatusProto terminalStatusProto) {
        LocationProto location = terminalStatusProto.getLocation();
        TerminalProto terminalInfo = terminalStatusProto.getTerminalInfo();
        Long terminalId = terminalInfo.getTerminalId();
        VehicleStatusEnum vehicleStatus = getVehicleStatus(location);
        terminalStatusProto.setVehicleStatus(vehicleStatus);
        lastStatusMap.put(terminalId, terminalStatusProto);
        log.debug("更新终端状态成功,终端信息:{}", terminalInfo);
    }

    /**
     * 将节点的所有终端更新为离线状态
     *
     * @param nodeName
     * @return
     */
    public void updateAllOffline(String nodeName) {
        lastStatusMap.forEach((terminalId, terminalStatusProto) -> {
            if (Objects.equals(nodeName, terminalStatusProto.getNodeName())) {
                terminalStatusProto.setTerminalStatus(TerminalStatusEnum.OFFLINE);
                terminalStatusProto.setVehicleStatus(VehicleStatusEnum.OFFLINE);
            }
        });
        log.info("更新{}的所有终端状态为离线,在线数量:{}", nodeName, getTerminalOnlineCount());
    }

    /**
     * 所有终端更新为离线状态
     *
     * @param terminalStatus
     * @param nodeName
     */
    public void updateAllOffline(TerminalStatusEnum terminalStatus, String nodeName) {
        lastStatusMap.forEach((terminalId, terminalStatusProto) -> {
            terminalStatusProto.setTerminalStatus(TerminalStatusEnum.OFFLINE);
            terminalStatusProto.setVehicleStatus(VehicleStatusEnum.OFFLINE);
        });
        log.info("更新{}所有终端状态为离线,事件:{},在线数量:{}", nodeName, terminalStatus.name(), getTerminalOnlineCount());
    }

    /**
     * 更新终端离线
     *
     * @param terminalProto
     */
    public void updateOffline(TerminalProto terminalProto) {
        Long terminalId = terminalProto.getTerminalId();
        lastStatusMap.computeIfPresent(terminalId, (k, terminalStatusProto) -> {
            terminalStatusProto.setTerminalStatus(TerminalStatusEnum.OFFLINE);
            terminalStatusProto.setVehicleStatus(VehicleStatusEnum.OFFLINE);
            return terminalStatusProto;
        });
        log.info("更新终端状态为离线,终端信息:{}", terminalProto);
    }

    /**
     * 更新终端上线
     *
     * @param terminalInfo
     */
    public void updateOnline(TerminalProto terminalInfo) {
        Long terminalId = terminalInfo.getTerminalId();
        lastStatusMap.computeIfPresent(terminalId, (k, terminalStatusProto) -> {
            terminalStatusProto.setNodeName(terminalInfo.getNodeName());
            terminalStatusProto.setTerminalInfo(terminalInfo);
            terminalStatusProto.setTerminalStatus(TerminalStatusEnum.ONLINE);
            terminalStatusProto.setVehicleStatus(VehicleStatusEnum.ONLINE);
            return terminalStatusProto;
        });
        log.info("更新终端状态为在线,终端信息:{},在线数量:{}", terminalInfo, getTerminalOnlineCount());
    }

    /**
     * 查询终端数量
     *
     * @return
     */
    public long getTerminalCount() {
        return lastStatusMap.size();
    }

    /**
     * 查询终端在线数
     *
     * @return
     */
    public long getTerminalOnlineCount() {
        long onlineCount = lastStatusMap.entrySet().stream()
                .filter(entry -> entry.getValue().getTerminalStatus() != TerminalStatusEnum.OFFLINE
                ).count();
        return onlineCount;
    }

    /**
     * 保存最后状态
     *
     * @param terminalId
     * @param terminalStatusProto
     */
    public void putLastStatus(Long terminalId, TerminalStatusProto terminalStatusProto) {
        lastStatusMap.put(terminalId, terminalStatusProto);
    }

    /**
     * 获取最后状态
     *
     * @param terminalId
     * @return
     */
    public TerminalStatusProto getLastStatus(Long terminalId) {
        return lastStatusMap.get(terminalId);
    }

    /**
     * 获取车辆状态
     * 车辆状态 0:离线 1:行驶 2:停车 3:怠速 4:报警 5:未定位 6:在线
     *
     * @param location
     * @return
     */
    private VehicleStatusEnum getVehicleStatus(LocationProto location) {
        if (Objects.equals(location.getGpsValid(), CommonConstant.NO)) {
            return VehicleStatusEnum.NO_POSITION;
        }
        if (!CollectionUtils.isEmpty(location.getAlarmTypeList())) {
            return VehicleStatusEnum.ALARM;
        }
        if (Objects.equals(location.getAcc(), CommonConstant.NO)) {
            return VehicleStatusEnum.STOP;
        }
        Double speed = location.getSpeed();
        if (speed != null) {
            return speed == 0 ? VehicleStatusEnum.IDLING : VehicleStatusEnum.RUNNING;
        }
        return VehicleStatusEnum.ONLINE;
    }

}