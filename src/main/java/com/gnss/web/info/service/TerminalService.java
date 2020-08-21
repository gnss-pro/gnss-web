package com.gnss.web.info.service;

import com.gnss.core.constants.TerminalStatusEnum;
import com.gnss.core.constants.VehicleStatusEnum;
import com.gnss.core.exception.ApplicationException;
import com.gnss.core.proto.TerminalProto;
import com.gnss.core.proto.TerminalStatusProto;
import com.gnss.core.service.RedisService;
import com.gnss.web.common.service.BaseService;
import com.gnss.web.info.api.TerminalCreateDTO;
import com.gnss.web.info.dao.TerminalRepository;
import com.gnss.web.info.domain.Terminal;
import com.gnss.web.terminal.service.TerminalStatusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

/**
 * <p>Description: 终端信息服务</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2019/10/14
 */
@Service
@Slf4j
public class TerminalService extends BaseService<Terminal> {

    @Autowired
    private TerminalRepository terminalRepository;

    @Autowired
    private TerminalStatusService terminalStatusService;

    @Autowired
    private RedisService redisService;

    /**
     * 创建/更新终端信息
     *
     * @param createDTO
     * @return
     */
    public Terminal saveOrUpdate(TerminalCreateDTO createDTO) {
        Optional<Terminal> optional = terminalRepository.findByPhoneNum(createDTO.getTerminalNum());
        Terminal terminal = null;
        if (optional.isPresent()) {
            terminal = update(optional.get(), createDTO);
        } else {
            terminal = create(createDTO);
        }
        cacheTerminalInfo(terminal);
        return terminal;
    }

    /**
     * 根据终端手机号查询终端信息
     *
     * @param phoneNum
     * @return
     */
    public Optional<Terminal> findByPhoneNum(String phoneNum) {
        return terminalRepository.findByPhoneNum(phoneNum);
    }

    /**
     * 创建终端信息
     *
     * @param createDTO
     * @return
     */
    private Terminal create(TerminalCreateDTO createDTO) {
        terminalRepository.findByTerminalNum(createDTO.getTerminalNum()).ifPresent(terminal -> {
            log.error("创建终端失败,终端号码{}已存在", createDTO.getTerminalNum());
            throw new ApplicationException("终端号码" + createDTO.getTerminalNum() + "已存在");
        });

        terminalRepository.findByVehicleNum(createDTO.getVehicleNum()).ifPresent(terminal -> {
            log.error("创建终端失败,车牌号{}已存在", createDTO.getVehicleNum());
            throw new ApplicationException("车牌号" + createDTO.getVehicleNum() + "已存在");
        });

        Terminal terminal = new Terminal();
        terminal.setPhoneNum(createDTO.getPhoneNum());
        terminal.setTerminalNum(createDTO.getTerminalNum());
        terminal.setVehicleNum(createDTO.getVehicleNum());
        terminal.setVehiclePlateColor(createDTO.getVehiclePlateColor());
        return save(terminal);
    }

    /**
     * 更新终端信息
     *
     * @param terminal
     * @param createDTO
     * @return
     */
    private Terminal update(Terminal terminal, TerminalCreateDTO createDTO) {
        String terminalNum = createDTO.getTerminalNum();
        if (!Objects.equals(terminalNum, terminal.getTerminalNum()) && terminalRepository.findByTerminalNum(terminalNum).isPresent()) {
            log.error("更新终端失败,终端号码{}已存在", terminalNum);
            throw new ApplicationException("终端号码" + terminalNum + "已存在");
        }

        String vehicleNum = createDTO.getVehicleNum();
        if (!Objects.equals(vehicleNum, terminal.getVehicleNum()) && terminalRepository.findByVehicleNum(vehicleNum).isPresent()) {
            log.error("更新终端失败,车牌号{}已存在", vehicleNum);
            throw new ApplicationException("车牌号" + vehicleNum + "已存在");
        }

        terminal.setTerminalNum(terminalNum);
        terminal.setVehicleNum(vehicleNum);
        terminal.setVehiclePlateColor(createDTO.getVehiclePlateColor());
        return save(terminal);
    }

    /**
     * 缓存终端信息
     *
     * @param terminal
     */
    private void cacheTerminalInfo(Terminal terminal) {
        Long terminalId = terminal.getId();
        //缓存到Redis
        TerminalProto terminalProto = new TerminalProto();
        terminalProto.setTerminalId(terminalId);
        terminalProto.setTerminalStrId(terminal.getId().toString());
        terminalProto.setTerminalNum(terminal.getTerminalNum());
        terminalProto.setTerminalSimCode(terminal.getPhoneNum());
        terminalProto.setVehicleNum(terminal.getVehicleNum());
        terminalProto.setVehiclePlateColor(terminal.getVehiclePlateColor());
        redisService.putTerminalInfo(terminalProto);

        //创建离线的终端状态
        TerminalStatusProto terminalStatusProto = terminalStatusService.getLastStatus(terminalId);
        if (terminalStatusProto == null) {
            terminalStatusProto = new TerminalStatusProto();
            terminalStatusProto.setTerminalStatus(TerminalStatusEnum.OFFLINE);
            terminalStatusProto.setVehicleStatus(VehicleStatusEnum.OFFLINE);
        }
        terminalStatusProto.setTerminalInfo(terminalProto);
        terminalStatusService.putLastStatus(terminal.getId(), terminalStatusProto);
    }
}