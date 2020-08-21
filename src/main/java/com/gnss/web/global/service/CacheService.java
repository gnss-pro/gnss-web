package com.gnss.web.global.service;

import com.alibaba.fastjson.JSON;
import com.gnss.core.constants.TerminalStatusEnum;
import com.gnss.core.model.config.BaseServerConfig;
import com.gnss.core.model.config.FileServerConfig;
import com.gnss.core.model.config.Jt808ServerConfig;
import com.gnss.core.model.config.MediaServerConfig;
import com.gnss.core.model.config.ServerStatus;
import com.gnss.core.service.RedisService;
import com.gnss.web.common.constant.GnssConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Description: 缓存服务</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2019/8/16
 */
@Slf4j
@Service
public class CacheService {

    @Autowired
    private RedisService redisService;

    private final Map<String, Object> serverInfoMap = new HashMap<>();

    /**
     * 缓存服务器信息
     *
     * @param nodeName
     * @param serverConfig
     */
    public void putServerInfo(String nodeName, Object serverConfig) {
        serverInfoMap.put(nodeName, serverConfig);
        //缓存redis
        redisService.putServerInfo(nodeName, JSON.toJSONString(serverConfig));
    }

    /**
     * 查询JT808服务器信息
     *
     * @return
     */
    public Jt808ServerConfig getJt808ServerInfo() {
        return getServerInfo(GnssConstants.JT808_SERVER_NAME, Jt808ServerConfig.class);
    }

    /**
     * 查询文件服务器信息
     *
     * @return
     */
    public FileServerConfig getFileServerInfo() {
        return getServerInfo(GnssConstants.FILE_SERVER_NAME, FileServerConfig.class);
    }

    /**
     * 查询流媒体服务器信息
     *
     * @param nodeName
     * @return
     */
    public MediaServerConfig getMediaServerInfo(String nodeName) {
        return getServerInfo(nodeName, MediaServerConfig.class);
    }

    /**
     * 获取缓存服务器信息
     *
     * @param nodeName
     * @param clazz
     * @param <T>
     * @return
     */
    private <T> T getServerInfo(String nodeName, Class<T> clazz) {
        Object obj = serverInfoMap.get(nodeName);
        if (obj == null) {
            T serverConfig = null;
            String serverInfoJson = redisService.getServerInfo(nodeName);
            if (serverInfoJson != null) {
                serverConfig = JSON.parseObject(serverInfoJson, clazz);
                serverInfoMap.put(nodeName, serverConfig);
            }
            return serverConfig;
        }
        return (T) obj;
    }

    /**
     * 更新服务器信息
     *
     * @param serverConfig
     * @param <T>
     */
    public <T extends BaseServerConfig> void updateServerInfo(T serverConfig) {
        serverInfoMap.put(serverConfig.getNodeName(), serverConfig);
        log.info("更新{}服务器信息,服务器信息:{}", serverConfig.getNodeName(), serverConfig);
    }

    /**
     * 更新服务器信息
     *
     * @param terminalStatus
     * @param serverInfoJson
     */
    public void updateServerInfo(TerminalStatusEnum terminalStatus, String serverInfoJson) {
        ServerStatus serverStatus = JSON.parseObject(serverInfoJson, ServerStatus.class);
        String nodeName = serverStatus.getNodeName();
        String serverConfigJson = serverStatus.getServerConfigJson();
        serverInfoMap.put(nodeName, JSON.parseObject(serverConfigJson, serverStatus.getClazz()));
        log.info("更新{}服务器信息,事件:{},服务器信息:{}", nodeName, terminalStatus.name(), serverConfigJson);
    }
}