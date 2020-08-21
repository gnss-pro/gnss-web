package com.gnss.web.info.controller;

import com.gnss.core.proto.TerminalProto;
import com.gnss.core.service.RedisService;
import com.gnss.web.common.api.ApiResultDTO;
import com.gnss.web.info.api.TerminalCreateDTO;
import com.gnss.web.info.api.TerminalDetailDTO;
import com.gnss.web.info.domain.Terminal;
import com.gnss.web.info.service.TerminalService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>Description: 终端信息接口</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2019/10/14
 */
@Api(tags = "终端信息")
@RestController
@RequestMapping("/api/v1/info/terminal")
@Slf4j
public class TerminalController {

    @Autowired
    private TerminalService terminalService;

    @Autowired
    private RedisService redisService;

    @ApiOperation("创建/更新终端")
    @PostMapping
    public ApiResultDTO<TerminalDetailDTO> saveOrUpdate(@ApiParam("终端信息") @Valid @RequestBody TerminalCreateDTO terminalCreateDTO) {
        Terminal terminal = terminalService.saveOrUpdate(terminalCreateDTO);
        TerminalDetailDTO terminalDetailDTO = toTerminalDetailDTO(terminal);
        log.info("创建/更新终端成功:{}", terminalDetailDTO);
        return new ApiResultDTO<>(terminalDetailDTO);
    }

    @ApiOperation("查询终端")
    @GetMapping(value = "/{phoneNum}")
    public ApiResultDTO<TerminalDetailDTO> findById(@ApiParam("终端手机号") @PathVariable String phoneNum) {
        TerminalProto terminalProto = redisService.getTerminalInfoBySimCode(phoneNum);
        TerminalDetailDTO terminalDetailDTO = toTerminalDetailDTO(terminalProto);
        return new ApiResultDTO<>(terminalDetailDTO);
    }

    /**
     * 数据库实体转DTO
     *
     * @param terminal
     * @return
     */
    private TerminalDetailDTO toTerminalDetailDTO(Terminal terminal) {
        if (terminal == null) {
            return null;
        }
        TerminalDetailDTO terminalDetailDTO = new TerminalDetailDTO();
        terminalDetailDTO.setId(terminal.getId().toString());
        terminalDetailDTO.setPhoneNum(terminal.getPhoneNum());
        terminalDetailDTO.setTerminalNum(terminal.getTerminalNum());
        terminalDetailDTO.setVehicleNum(terminal.getVehicleNum());
        terminalDetailDTO.setVehiclePlateColor(terminal.getVehiclePlateColor().getDesc());
        return terminalDetailDTO;
    }

    /**
     * Redis缓存实体转DTO
     *
     * @param terminalProto
     * @return
     */
    private TerminalDetailDTO toTerminalDetailDTO(TerminalProto terminalProto) {
        if (terminalProto == null) {
            return null;
        }
        TerminalDetailDTO terminalDetailDTO = new TerminalDetailDTO();
        terminalDetailDTO.setId(String.valueOf(terminalProto.getTerminalId()));
        terminalDetailDTO.setPhoneNum(terminalProto.getTerminalSimCode());
        terminalDetailDTO.setTerminalNum(terminalProto.getTerminalNum());
        terminalDetailDTO.setVehicleNum(terminalProto.getVehicleNum());
        terminalDetailDTO.setVehiclePlateColor(terminalProto.getVehiclePlateColor().getDesc());
        return terminalDetailDTO;
    }
}