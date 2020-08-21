package com.gnss.web.terminal.controller;

import com.gnss.core.constants.TerminalStatusEnum;
import com.gnss.core.proto.LocationProto;
import com.gnss.core.proto.TerminalProto;
import com.gnss.core.proto.TerminalStatusProto;
import com.gnss.core.service.RedisService;
import com.gnss.core.utils.TimeUtil;
import com.gnss.web.common.api.ApiResultDTO;
import com.gnss.web.common.constant.GnssConstants;
import com.gnss.web.consumer.TerminalStatusReceiver;
import com.gnss.web.terminal.api.LoadTestInfoDTO;
import com.gnss.web.terminal.api.LocationDTO;
import com.gnss.web.terminal.api.TerminalStatusDTO;
import com.gnss.web.terminal.service.TerminalStatusService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>Description: JT808压力测试接口</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2019/2/14
 */
@Api(tags = "JT808压力测试")
@RestController
@RequestMapping("/api/v1/terminal/loadtest")
@Slf4j
public class Jt808LoadTestController {

    @Autowired
    private TerminalStatusReceiver terminalStatusReceiver;

    @Autowired
    private TerminalStatusService terminalStatusService;

    @Autowired
    private RedisService redisService;

    @ApiOperation("查询压力统计信息")
    @GetMapping("/loadTestInfo")
    public ApiResultDTO<LoadTestInfoDTO> loadTestInfo() {
        LoadTestInfoDTO loadTestInfo = buildLoadTestInfo();
        log.info("查询压力统计信息:{}", loadTestInfo);
        return new ApiResultDTO<>(loadTestInfo);
    }

    @ApiOperation("重置压力统计信息")
    @GetMapping("/resetLocationCount")
    public ApiResultDTO<LoadTestInfoDTO> resetLocationCount() {
        terminalStatusReceiver.resetLocationCounter();
        terminalStatusService.updateAllOffline(GnssConstants.JT808_SERVER_NAME);
        LoadTestInfoDTO loadTestInfo = buildLoadTestInfo();
        log.info("重置压力统计信息:{}", loadTestInfo);
        return new ApiResultDTO<>(loadTestInfo);
    }

    @ApiOperation("查询最后位置")
    @GetMapping("/location/{phoneNum}")
    public ApiResultDTO<TerminalStatusDTO> lastLocation(@ApiParam("终端手机号") @PathVariable String phoneNum) {
        log.info("查询最后位置,手机号:{}", phoneNum);
        TerminalProto terminalInfo = redisService.getTerminalInfoBySimCode(phoneNum);
        if (terminalInfo == null) {
            return new ApiResultDTO<>();
        }
        TerminalStatusProto terminalStatusProto = terminalStatusService.getLastStatus(terminalInfo.getTerminalId());
        if (terminalStatusProto == null) {
            return new ApiResultDTO<>();
        }
        TerminalStatusDTO terminalStatusDTO = new TerminalStatusDTO();
        terminalStatusDTO.setTerminalId(terminalInfo.getTerminalId());
        terminalStatusDTO.setVehicleNum(terminalInfo.getVehicleNum());
        terminalStatusDTO.setVehiclePlateColor(terminalInfo.getVehiclePlateColor().getDesc());
        terminalStatusDTO.setTerminalNum(terminalInfo.getTerminalNum());
        terminalStatusDTO.setSimNum(terminalInfo.getTerminalSimCode());
        terminalStatusDTO.setOnline(terminalStatusProto.getTerminalStatus() == TerminalStatusEnum.OFFLINE ? "离线" : "在线");
        terminalStatusDTO.setVehicleStatus(terminalStatusProto.getVehicleStatus().getDesc());
        LocationDTO location = buildLocationDTO(terminalStatusProto.getLocation());
        terminalStatusDTO.setLocation(location);
        return new ApiResultDTO<>(terminalStatusDTO);
    }

    /**
     * 构造压力测试状态信息
     *
     * @return
     */
    private LoadTestInfoDTO buildLoadTestInfo() {
        long locationCount = terminalStatusReceiver.getLocationCount();
        LoadTestInfoDTO loadTestInfoDTO = new LoadTestInfoDTO();
        loadTestInfoDTO.setTerminalTotalCount(terminalStatusService.getTerminalCount());
        loadTestInfoDTO.setTerminalOnlineCount(terminalStatusService.getTerminalOnlineCount());
        loadTestInfoDTO.setLocationCount(locationCount);
        return loadTestInfoDTO;
    }

    /**
     * 构造位置信息
     *
     * @param locationProto
     * @return
     */
    private LocationDTO buildLocationDTO(LocationProto locationProto) {
        if (locationProto == null) {
            return null;
        }
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setGpsValid(locationProto.getGpsValid());
        locationDTO.setLat(locationProto.getLat());
        locationDTO.setLon(locationProto.getLon());
        locationDTO.setAltitude(locationProto.getAltitude());
        locationDTO.setSpeed(locationProto.getSpeed());
        locationDTO.setDirection(locationProto.getDirection());
        locationDTO.setGpsTime(TimeUtil.formatTime(locationProto.getTime()));
        locationDTO.setMileage(locationProto.getMileage());
        locationDTO.setFuel(locationProto.getFuel());
        locationDTO.setRecoderSpeed(locationProto.getRecoderSpeed());
        locationDTO.setStatus(locationProto.getStatus());
        locationDTO.setAlarmFlag(locationProto.getAlarmFlag());
        locationDTO.setExtraInfo(locationProto.getExtraInfo());
        return locationDTO;
    }
}