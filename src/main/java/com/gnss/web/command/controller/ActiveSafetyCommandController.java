package com.gnss.web.command.controller;

import com.gnss.web.command.api.CommandRequestDTO;
import com.gnss.web.command.api.CommandResultDTO;
import com.gnss.web.command.api.jt808.safety.Command8103Param;
import com.gnss.web.command.api.jt808.safety.Command8801Param;
import com.gnss.web.command.api.jt808.safety.Command8900Param;
import com.gnss.web.command.service.CommandOperationService;
import com.gnss.web.common.api.ApiResultDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>Description: JT808主动安全指令操作接口</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2019-1-18
 */
@Api(tags = "JT808主动安全指令操作")
@RestController
@RequestMapping("/api/v1/commands/jt808/safety")
public class ActiveSafetyCommandController {

    @Autowired
    private CommandOperationService commandOperationService;

    @ApiOperation("设置终端参数")
    @PostMapping("/sendCommand8103")
    public ApiResultDTO<CommandResultDTO> sendCommand8103(@ApiParam("指令信息") @Valid @RequestBody CommandRequestDTO<Command8103Param> commandRequestDTO) {
        CommandResultDTO resultDTO = commandOperationService.sendCommandEntity(commandRequestDTO);
        return ApiResultDTO.success(resultDTO);
    }

    @ApiOperation("外设立即拍照指令")
    @PostMapping("/sendCommand8801")
    public ApiResultDTO<CommandResultDTO> sendCommand8801(@ApiParam("指令信息") @Valid @RequestBody CommandRequestDTO<Command8801Param> commandRequestDTO) {
        CommandResultDTO resultDTO = commandOperationService.sendCommandEntity(commandRequestDTO);
        return ApiResultDTO.success(resultDTO);
    }

    @ApiOperation("查询基本信息")
    @PostMapping("/sendCommand8900")
    public ApiResultDTO<CommandResultDTO> sendCommand8900(@ApiParam("指令信息") @Valid @RequestBody CommandRequestDTO<Command8900Param> commandRequestDTO) {
        CommandResultDTO resultDTO = commandOperationService.sendCommandEntity(commandRequestDTO);
        return ApiResultDTO.success(resultDTO);
    }

}