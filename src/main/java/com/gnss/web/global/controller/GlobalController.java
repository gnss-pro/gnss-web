package com.gnss.web.global.controller;

import com.alibaba.fastjson.JSON;
import com.gnss.core.constants.AlarmTypeEnum;
import com.gnss.core.constants.jt808.ParamSettingEnum;
import com.gnss.core.model.DownCommandInfo;
import com.gnss.core.model.config.FileServerConfig;
import com.gnss.core.model.config.Jt808ServerConfig;
import com.gnss.core.model.config.WebServerConfig;
import com.gnss.core.service.RedisService;
import com.gnss.mqutil.event.DownCommandRegister;
import com.gnss.web.common.api.ApiResultDTO;
import com.gnss.web.global.api.AlarmTypeDTO;
import com.gnss.web.global.api.ParamSettingDTO;
import com.gnss.web.global.service.CacheService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>Description: 全局接口</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2019/6/24
 */
@Api(tags = "全局接口")
@RestController
@RequestMapping("/api/v1/global")
public class GlobalController {

    @Value("${spring.application.name}")
    private String nodeName;

    @Autowired
    private RedisService redisService;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private DownCommandRegister downCommandRegister;

    @ApiOperation("查询终端参数列表")
    @GetMapping(value = "/terminalParamInfo")
    public ApiResultDTO<ParamSettingDTO> getTerminalParamInfo() {
        List<ParamSettingDTO> paramSettingDTOList = Arrays.stream(ParamSettingEnum.values()).map(paramSettingEnum -> {
            ParamSettingDTO paramSettingDTO = new ParamSettingDTO();
            paramSettingDTO.setParamId(paramSettingEnum.getValue());
            paramSettingDTO.setParamDesc(paramSettingEnum.getDesc());
            return paramSettingDTO;
        }).collect(Collectors.toList());
        return ApiResultDTO.success(paramSettingDTOList);
    }

    @ApiOperation("根据报警类型前缀查询报警类型列表")
    @PostMapping(value = "/alarmTypeList")
    public ApiResultDTO<List<AlarmTypeDTO>> getAlarmTypeList(@ApiParam("报警类型前缀列表") @RequestBody List<String> typeList) {
        if (CollectionUtils.isEmpty(typeList)) {
            return ApiResultDTO.success(Collections.emptyList());
        }
        List<AlarmTypeDTO> alarmTypeDTOList = Arrays.stream(AlarmTypeEnum.values()).filter(alarmTypeEnum -> {
            //判断报警类型的前缀
            for (String namePrefix : typeList) {
                if (alarmTypeEnum.name().startsWith(namePrefix)) {
                    return true;
                }
            }
            return false;
        }).map(alarmTypeEnum -> {
            AlarmTypeDTO alarmTypeDTO = new AlarmTypeDTO();
            alarmTypeDTO.setCode(alarmTypeEnum.getCode());
            alarmTypeDTO.setDesc(alarmTypeEnum.getDesc());
            return alarmTypeDTO;
        }).collect(Collectors.toList());
        return ApiResultDTO.success(alarmTypeDTOList);
    }

    @ApiOperation("查询文件服务器配置")
    @GetMapping(value = "/fileServerInfo")
    public ApiResultDTO<FileServerConfig> getFileServerConfig() {
        FileServerConfig serverConfig = cacheService.getFileServerInfo();
        if (serverConfig == null) {
            return ApiResultDTO.failure(null, "文件服务器未启动");
        }
        return ApiResultDTO.success(serverConfig);
    }

    @ApiOperation("查询JT808服务器配置")
    @GetMapping(value = "/jt808ServerInfo")
    public ApiResultDTO<Jt808ServerConfig> getJt808ServerInfo() {
        Jt808ServerConfig serverConfig = cacheService.getJt808ServerInfo();
        if (serverConfig == null) {
            return ApiResultDTO.failure(null, "JT808服务器未启动");
        }
        return ApiResultDTO.success(serverConfig);
    }

    @ApiOperation("查询所有服务器信息")
    @GetMapping(value = "/allServerInfo")
    public ApiResultDTO<Map<String, String>> getAllServerInfo() {
        //其他服务器的信息
        Map<String, String> serverInfoMap = redisService.getAllServerInfo();
        //已支持的下行指令列表
        List<DownCommandInfo> supportedDownCommands = downCommandRegister.getSupportedDownCommand();
        //构造Web服务器信息
        WebServerConfig webServerConfig = new WebServerConfig();
        webServerConfig.setNodeName(nodeName);
        webServerConfig.setServerName("Web后台");
        webServerConfig.setLanIp("127.0.0.1");
        webServerConfig.setIsRunning(true);
        webServerConfig.setSupportedDownCommands(supportedDownCommands);
        serverInfoMap.put(nodeName, JSON.toJSONString(webServerConfig));
        return ApiResultDTO.success(serverInfoMap);
    }
}