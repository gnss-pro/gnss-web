package com.gnss.web.command.controller;

import com.gnss.web.command.api.CommandRequestDTO;
import com.gnss.web.command.api.CommandResultDTO;
import com.gnss.web.command.api.jt808.basic.Command8103Param;
import com.gnss.web.command.api.jt808.basic.Command8104Param;
import com.gnss.web.command.api.jt808.basic.Command8105Param;
import com.gnss.web.command.api.jt808.basic.Command8106Param;
import com.gnss.web.command.api.jt808.basic.Command8107Param;
import com.gnss.web.command.api.jt808.basic.Command8201Param;
import com.gnss.web.command.api.jt808.basic.Command8202Param;
import com.gnss.web.command.api.jt808.basic.Command8203Param;
import com.gnss.web.command.api.jt808.basic.Command8204Param;
import com.gnss.web.command.api.jt808.basic.Command8300Param;
import com.gnss.web.command.api.jt808.basic.Command8301Param;
import com.gnss.web.command.api.jt808.basic.Command8302Param;
import com.gnss.web.command.api.jt808.basic.Command8303Param;
import com.gnss.web.command.api.jt808.basic.Command8304Param;
import com.gnss.web.command.api.jt808.basic.Command8400Param;
import com.gnss.web.command.api.jt808.basic.Command8401Param;
import com.gnss.web.command.api.jt808.basic.Command8500Param;
import com.gnss.web.command.api.jt808.basic.Command8600Param;
import com.gnss.web.command.api.jt808.basic.Command8601Param;
import com.gnss.web.command.api.jt808.basic.Command8602Param;
import com.gnss.web.command.api.jt808.basic.Command8603Param;
import com.gnss.web.command.api.jt808.basic.Command8604Param;
import com.gnss.web.command.api.jt808.basic.Command8605Param;
import com.gnss.web.command.api.jt808.basic.Command8606Param;
import com.gnss.web.command.api.jt808.basic.Command8607Param;
import com.gnss.web.command.api.jt808.basic.Command8700Param;
import com.gnss.web.command.api.jt808.basic.Command8702Param;
import com.gnss.web.command.api.jt808.basic.Command8801Param;
import com.gnss.web.command.api.jt808.basic.Command8802Param;
import com.gnss.web.command.api.jt808.basic.Command8803Param;
import com.gnss.web.command.api.jt808.basic.Command8804Param;
import com.gnss.web.command.api.jt808.basic.Command8805Param;
import com.gnss.web.command.api.jt808.basic.Command8900Param;
import com.gnss.web.command.api.jt808.basic.Command8A00Param;
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
 * <p>Description: JT808基本指令操作接口</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author menghuan
 * @version 1.0.1
 * @date 2019/2/14
 */
@Api(tags = "JT808基本指令操作")
@RestController
@RequestMapping("/api/v1/commands/jt808/basic")
public class Jt808BasicCommandController {

    @Autowired
    private CommandOperationService commandOperationService;

    @ApiOperation("设置终端参数")
    @PostMapping("/sendCommand8103")
    public ApiResultDTO<CommandResultDTO> sendCommand8103(@ApiParam("指令信息") @Valid @RequestBody CommandRequestDTO<Command8103Param> commandRequestDTO) {
        CommandResultDTO resultDTO = commandOperationService.sendCommandEntity(commandRequestDTO);
        return ApiResultDTO.success(resultDTO);
    }

    @ApiOperation("查询终端参数")
    @PostMapping("/sendCommand8104")
    public ApiResultDTO<CommandResultDTO> sendCommand8104(@ApiParam("指令信息") @Valid @RequestBody CommandRequestDTO<Command8104Param> commandRequestDTO) {
        CommandResultDTO resultDTO = commandOperationService.sendCommandEntity(commandRequestDTO);
        return ApiResultDTO.success(resultDTO);
    }

    @ApiOperation("终端控制")
    @PostMapping("/sendCommand8105")
    public ApiResultDTO<CommandResultDTO> sendCommand8105(@ApiParam("指令信息") @Valid @RequestBody CommandRequestDTO<Command8105Param> commandRequestDTO) {
        CommandResultDTO resultDTO = commandOperationService.sendCommandEntity(commandRequestDTO);
        return ApiResultDTO.success(resultDTO);
    }

    @ApiOperation("查询指定终端参数")
    @PostMapping("/sendCommand8106")
    public ApiResultDTO<CommandResultDTO> sendCommand8106(@ApiParam("指令信息") @Valid @RequestBody CommandRequestDTO<Command8106Param> commandRequestDTO) {
        CommandResultDTO resultDTO = commandOperationService.sendCommandEntity(commandRequestDTO);
        return ApiResultDTO.success(resultDTO);
    }

    @ApiOperation("查询终端属性")
    @PostMapping("/sendCommand8107")
    public ApiResultDTO<CommandResultDTO> sendCommand8107(@ApiParam("指令信息") @Valid @RequestBody CommandRequestDTO<Command8107Param> commandRequestDTO) {
        CommandResultDTO resultDTO = commandOperationService.sendCommandEntity(commandRequestDTO);
        return ApiResultDTO.success(resultDTO);
    }

    @ApiOperation("位置信息查询")
    @PostMapping("/sendCommand8201")
    public ApiResultDTO<CommandResultDTO> sendCommand8201(@ApiParam("指令信息") @Valid @RequestBody CommandRequestDTO<Command8201Param> commandRequestDTO) {
        CommandResultDTO resultDTO = commandOperationService.sendCommandEntity(commandRequestDTO);
        return ApiResultDTO.success(resultDTO);
    }

    @ApiOperation("临时位置跟踪控制")
    @PostMapping("/sendCommand8202")
    public ApiResultDTO<CommandResultDTO> sendCommand8202(@ApiParam("指令信息") @Valid @RequestBody CommandRequestDTO<Command8202Param> commandRequestDTO) {
        CommandResultDTO resultDTO = commandOperationService.sendCommandEntity(commandRequestDTO);
        return ApiResultDTO.success(resultDTO);
    }

    @ApiOperation("人工确认报警消息")
    @PostMapping("/sendCommand8203")
    public ApiResultDTO<CommandResultDTO> sendCommand8203(@ApiParam("指令信息") @Valid @RequestBody CommandRequestDTO<Command8203Param> commandRequestDTO) {
        CommandResultDTO resultDTO = commandOperationService.sendCommandEntity(commandRequestDTO);
        return ApiResultDTO.success(resultDTO);
    }

    @ApiOperation("链路检测")
    @PostMapping("/sendCommand8204")
    public ApiResultDTO<CommandResultDTO> sendCommand8204(@ApiParam("指令信息") @Valid @RequestBody CommandRequestDTO<Command8204Param> commandRequestDTO) {
        CommandResultDTO resultDTO = commandOperationService.sendCommandEntity(commandRequestDTO);
        return ApiResultDTO.success(resultDTO);
    }

    @ApiOperation("文本信息下发参数")
    @PostMapping("/sendCommand8300")
    public ApiResultDTO<CommandResultDTO> sendCommand8300(@ApiParam("指令信息") @Valid @RequestBody CommandRequestDTO<Command8300Param> commandRequestDTO) {
        CommandResultDTO resultDTO = commandOperationService.sendCommandEntity(commandRequestDTO);
        return ApiResultDTO.success(resultDTO);
    }

    @ApiOperation("事件设置")
    @PostMapping("/sendCommand8301")
    public ApiResultDTO<CommandResultDTO> sendCommand8301(@ApiParam("指令信息") @Valid @RequestBody CommandRequestDTO<Command8301Param> commandRequestDTO) {
        CommandResultDTO resultDTO = commandOperationService.sendCommandEntity(commandRequestDTO);
        return ApiResultDTO.success(resultDTO);
    }

    @ApiOperation("提问下发")
    @PostMapping("/sendCommand8302")
    public ApiResultDTO<CommandResultDTO> sendCommand8302(@ApiParam("指令信息") @Valid @RequestBody CommandRequestDTO<Command8302Param> commandRequestDTO) {
        CommandResultDTO resultDTO = commandOperationService.sendCommandEntity(commandRequestDTO);
        return ApiResultDTO.success(resultDTO);
    }

    @ApiOperation("信息点播菜单设置")
    @PostMapping("/sendCommand8303")
    public ApiResultDTO<CommandResultDTO> sendCommand8303(@ApiParam("指令信息") @Valid @RequestBody CommandRequestDTO<Command8303Param> commandRequestDTO) {
        CommandResultDTO resultDTO = commandOperationService.sendCommandEntity(commandRequestDTO);
        return ApiResultDTO.success(resultDTO);
    }

    @ApiOperation("信息服务")
    @PostMapping("/sendCommand8304")
    public ApiResultDTO<CommandResultDTO> sendCommand8304(@ApiParam("指令信息") @Valid @RequestBody CommandRequestDTO<Command8304Param> commandRequestDTO) {
        CommandResultDTO resultDTO = commandOperationService.sendCommandEntity(commandRequestDTO);
        return ApiResultDTO.success(resultDTO);
    }

    @ApiOperation("电话回拨参数")
    @PostMapping("/sendCommand8400")
    public ApiResultDTO<CommandResultDTO> sendCommand8400(@ApiParam("指令信息") @Valid @RequestBody CommandRequestDTO<Command8400Param> commandRequestDTO) {
        CommandResultDTO resultDTO = commandOperationService.sendCommandEntity(commandRequestDTO);
        return ApiResultDTO.success(resultDTO);
    }

    @ApiOperation("设置电话本")
    @PostMapping("/sendCommand8401")
    public ApiResultDTO<CommandResultDTO> sendCommand8401(@ApiParam("指令信息") @Valid @RequestBody CommandRequestDTO<Command8401Param> commandRequestDTO) {
        CommandResultDTO resultDTO = commandOperationService.sendCommandEntity(commandRequestDTO);
        return ApiResultDTO.success(resultDTO);
    }

    @ApiOperation("车辆控制")
    @PostMapping("/sendCommand8500")
    public ApiResultDTO<CommandResultDTO> sendCommand8500(@ApiParam("指令信息") @Valid @RequestBody CommandRequestDTO<Command8500Param> commandRequestDTO) {
        CommandResultDTO resultDTO = commandOperationService.sendCommandEntity(commandRequestDTO);
        return ApiResultDTO.success(resultDTO);
    }

    @ApiOperation("设置圆形区域")
    @PostMapping("/sendCommand8600")
    public ApiResultDTO<CommandResultDTO> sendCommand8600(@ApiParam("指令信息") @Valid @RequestBody CommandRequestDTO<Command8600Param> commandRequestDTO) {
        CommandResultDTO resultDTO = commandOperationService.sendCommandEntity(commandRequestDTO);
        return ApiResultDTO.success(resultDTO);
    }

    @ApiOperation("删除圆形区域")
    @PostMapping("/sendCommand8601")
    public ApiResultDTO<CommandResultDTO> sendCommand8601(@ApiParam("指令信息") @Valid @RequestBody CommandRequestDTO<Command8601Param> commandRequestDTO) {
        CommandResultDTO resultDTO = commandOperationService.sendCommandEntity(commandRequestDTO);
        return ApiResultDTO.success(resultDTO);
    }

    @ApiOperation("设置矩形区域")
    @PostMapping("/sendCommand8602")
    public ApiResultDTO<CommandResultDTO> sendCommand8602(@ApiParam("指令信息") @Valid @RequestBody CommandRequestDTO<Command8602Param> commandRequestDTO) {
        CommandResultDTO resultDTO = commandOperationService.sendCommandEntity(commandRequestDTO);
        return ApiResultDTO.success(resultDTO);
    }

    @ApiOperation("删除矩形区域")
    @PostMapping("/sendCommand8603")
    public ApiResultDTO<CommandResultDTO> sendCommand8603(@ApiParam("指令信息") @Valid @RequestBody CommandRequestDTO<Command8603Param> commandRequestDTO) {
        CommandResultDTO resultDTO = commandOperationService.sendCommandEntity(commandRequestDTO);
        return ApiResultDTO.success(resultDTO);
    }

    @ApiOperation("设置多边形区域")
    @PostMapping("/sendCommand8604")
    public ApiResultDTO<CommandResultDTO> sendCommand8604(@ApiParam("指令信息") @Valid @RequestBody CommandRequestDTO<Command8604Param> commandRequestDTO) {
        CommandResultDTO resultDTO = commandOperationService.sendCommandEntity(commandRequestDTO);
        return ApiResultDTO.success(resultDTO);
    }

    @ApiOperation("删除多边形区域")
    @PostMapping("/sendCommand8605")
    public ApiResultDTO<CommandResultDTO> sendCommand8605(@ApiParam("指令信息") @Valid @RequestBody CommandRequestDTO<Command8605Param> commandRequestDTO) {
        CommandResultDTO resultDTO = commandOperationService.sendCommandEntity(commandRequestDTO);
        return ApiResultDTO.success(resultDTO);
    }

    @ApiOperation("设置路线")
    @PostMapping("/sendCommand8606")
    public ApiResultDTO<CommandResultDTO> sendCommand8606(@ApiParam("指令信息") @Valid @RequestBody CommandRequestDTO<Command8606Param> commandRequestDTO) {
        CommandResultDTO resultDTO = commandOperationService.sendCommandEntity(commandRequestDTO);
        return ApiResultDTO.success(resultDTO);
    }

    @ApiOperation("删除路线")
    @PostMapping("/sendCommand8607")
    public ApiResultDTO<CommandResultDTO> sendCommand8607(@ApiParam("指令信息") @Valid @RequestBody CommandRequestDTO<Command8607Param> commandRequestDTO) {
        CommandResultDTO resultDTO = commandOperationService.sendCommandEntity(commandRequestDTO);
        return ApiResultDTO.success(resultDTO);
    }

    @ApiOperation("行驶记录数据采集命令")
    @PostMapping("/sendCommand8700")
    public ApiResultDTO<CommandResultDTO> sendCommand8700(@ApiParam("指令信息") @Valid @RequestBody CommandRequestDTO<Command8700Param> commandRequestDTO) {
        CommandResultDTO resultDTO = commandOperationService.sendCommandEntity(commandRequestDTO);
        return ApiResultDTO.success(resultDTO);
    }

    @ApiOperation("上报驾驶员身份信息请求")
    @PostMapping("/sendCommand8702")
    public ApiResultDTO<CommandResultDTO> sendCommand8702(@ApiParam("指令信息") @Valid @RequestBody CommandRequestDTO<Command8702Param> commandRequestDTO) {
        CommandResultDTO resultDTO = commandOperationService.sendCommandEntity(commandRequestDTO);
        return ApiResultDTO.success(resultDTO);
    }

    @ApiOperation("摄像头立即拍摄命令")
    @PostMapping("/sendCommand8801")
    public ApiResultDTO<CommandResultDTO> sendCommand8801(@ApiParam("指令信息") @Valid @RequestBody CommandRequestDTO<Command8801Param> commandRequestDTO) {
        CommandResultDTO resultDTO = commandOperationService.sendCommandEntity(commandRequestDTO);
        return ApiResultDTO.success(resultDTO);
    }

    @ApiOperation("存储多媒体数据检索")
    @PostMapping("/sendCommand8802")
    public ApiResultDTO<CommandResultDTO> sendCommand8802(@ApiParam("指令信息") @Valid @RequestBody CommandRequestDTO<Command8802Param> commandRequestDTO) {
        CommandResultDTO resultDTO = commandOperationService.sendCommandEntity(commandRequestDTO);
        return ApiResultDTO.success(resultDTO);
    }

    @ApiOperation("存储多媒体数据上传命令")
    @PostMapping("/sendCommand8803")
    public ApiResultDTO<CommandResultDTO> sendCommand8803(@ApiParam("指令信息") @Valid @RequestBody CommandRequestDTO<Command8803Param> commandRequestDTO) {
        CommandResultDTO resultDTO = commandOperationService.sendCommandEntity(commandRequestDTO);
        return ApiResultDTO.success(resultDTO);
    }

    @ApiOperation("录音开始命令")
    @PostMapping("/sendCommand8804")
    public ApiResultDTO<CommandResultDTO> sendCommand8804(@ApiParam("指令信息") @Valid @RequestBody CommandRequestDTO<Command8804Param> commandRequestDTO) {
        CommandResultDTO resultDTO = commandOperationService.sendCommandEntity(commandRequestDTO);
        return ApiResultDTO.success(resultDTO);
    }

    @ApiOperation("单条存储多媒体数据检索上传命令")
    @PostMapping("/sendCommand8805")
    public ApiResultDTO<CommandResultDTO> sendCommand8805(@ApiParam("指令信息") @Valid @RequestBody CommandRequestDTO<Command8805Param> commandRequestDTO) {
        CommandResultDTO resultDTO = commandOperationService.sendCommandEntity(commandRequestDTO);
        return ApiResultDTO.success(resultDTO);
    }

    @ApiOperation("数据下行透传")
    @PostMapping("/sendCommand8900")
    public ApiResultDTO<CommandResultDTO> sendCommand8900(@ApiParam("指令信息") @Valid @RequestBody CommandRequestDTO<Command8900Param> commandRequestDTO) {
        CommandResultDTO resultDTO = commandOperationService.sendCommandEntity(commandRequestDTO);
        return ApiResultDTO.success(resultDTO);
    }

    @ApiOperation("平台RSA公钥")
    @PostMapping("/sendCommand8A00")
    public ApiResultDTO<CommandResultDTO> sendCommand8A00(@ApiParam("指令信息") @Valid @RequestBody CommandRequestDTO<Command8A00Param> commandRequestDTO) {
        CommandResultDTO resultDTO = commandOperationService.sendCommandEntity(commandRequestDTO);
        return ApiResultDTO.success(resultDTO);
    }
}