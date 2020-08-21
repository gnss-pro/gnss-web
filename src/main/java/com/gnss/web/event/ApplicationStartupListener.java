package com.gnss.web.event;

import com.alibaba.fastjson.JSON;
import com.gnss.core.constants.TerminalStatusEnum;
import com.gnss.core.constants.VehiclePlateColorEnum;
import com.gnss.core.constants.VehicleStatusEnum;
import com.gnss.core.model.config.MediaServerConfig;
import com.gnss.core.proto.TerminalProto;
import com.gnss.core.proto.TerminalStatusProto;
import com.gnss.core.service.RedisService;
import com.gnss.web.common.constant.GnssConstants;
import com.gnss.web.global.service.CacheService;
import com.gnss.web.info.domain.Terminal;
import com.gnss.web.info.service.TerminalService;
import com.gnss.web.terminal.service.TerminalStatusService;
import io.lettuce.core.RedisChannelHandler;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisConnectionStateListener;
import io.lettuce.core.RedisURI;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.net.SocketAddress;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>Description: 系统初始化执行</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2019-1-18
 */
@Component
@ConfigurationProperties(prefix = "gnss")
@Slf4j
@Order(value = 1)
public class ApplicationStartupListener implements CommandLineRunner, ApplicationListener<ContextClosedEvent> {

    /**
     * 注入配置文件的go流媒体服务器配置
     */
    @Getter
    @Setter
    private MediaServerConfig goMediaServer;

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Value("${spring.redis.password}")
    private String redisPassword;

    @Value("${gnss.test.vehicleCount}")
    private int testVehicleCount;

    @Value("${gnss.test.phoneNumberStart}")
    private long phoneNumberStart;

    @Value("${gnss.test.vehiclePlateColor}")
    private int vehiclePlateColor;

    @Autowired
    private RedisService redisService;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private TerminalStatusService terminalStatusService;

    @Autowired
    private TerminalService terminalService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public void run(String... args) throws Exception {
        //缓存go流媒体服务器配置
        goMediaServer.setNodeName(GnssConstants.GO_MEDIA_SERVER_NAME);
        goMediaServer.setIsRunning(true);
        cacheService.putServerInfo(GnssConstants.GO_MEDIA_SERVER_NAME, goMediaServer);
        //配置Redis连接时需要加载的数据
        initRedisClient();
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        log.error("*****程序关闭*****");
    }

    /**
     * 配置Redis连接时需要加载的数据
     */
    private void initRedisClient() {
        RedisURI redisUri = RedisURI.builder()
                .withHost(redisHost)
                .withPort(redisPort)
                .withPassword(redisPassword)
                .withTimeout(Duration.of(10, ChronoUnit.SECONDS))
                .build();
        RedisClient redisClient = RedisClient.create(redisUri);
        redisClient.addListener(new RedisConnectionStateListener() {

            @Override
            public void onRedisConnected(RedisChannelHandler<?, ?> connection, SocketAddress socketAddress) {
                log.info("Redis客户端连接({}:{})成功", redisHost, redisPort);
                loadTerminalInfo();
            }

            @Override
            public void onRedisDisconnected(RedisChannelHandler<?, ?> redisChannelHandler) {
                log.info("Redis客户端断开连接({}:{})", redisHost, redisPort);
            }

            @Override
            public void onRedisExceptionCaught(RedisChannelHandler<?, ?> redisChannelHandler, Throwable throwable) {
                log.error("Redis客户端异常({}:{})", redisHost, redisPort, throwable);
            }
        });
        redisClient.connect();
    }

    /**
     * 缓存终端信息
     */
    private void loadTerminalInfo() {
        log.info("删除缓存Redis终端信息");
        redisService.deleteAllTerminalInfo();
        String nodeName = "jt808-server";
        String redisKey = "terminal_status";
        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();

        //添加模拟测试的终端信息，正式开发时需要从数据库查找所有终端信息
        int batchSize = 5000;
        List<TerminalProto> terminalInfoList = new ArrayList<>(batchSize);
        TerminalProto terminalInfo = null;
        VehiclePlateColorEnum vehiclePlateColorEnum = VehiclePlateColorEnum.fromValue(vehiclePlateColor);
        StopWatch sw = new StopWatch("Redis缓存终端信息");
        sw.start("加载模拟的" + testVehicleCount + "个终端信息");
        Map<String, String> terminalStatusMap = new HashMap<>(testVehicleCount);
        for (int i = 1; i <= testVehicleCount; i++) {
            terminalInfo = new TerminalProto();
            Long terminalId = 1000000L + i;
            terminalInfo.setTerminalId(terminalId);
            terminalInfo.setTerminalStrId(terminalId.toString());
            terminalInfo.setTerminalNum(String.valueOf(terminalId));
            terminalInfo.setTerminalSimCode(String.valueOf(phoneNumberStart + i));
            terminalInfo.setVehicleNum(String.format("测B%05d", i));
            terminalInfo.setVehiclePlateColor(vehiclePlateColorEnum);
            terminalInfoList.add(terminalInfo);
            if (i % batchSize == 0 || i == testVehicleCount) {
                redisService.batchPutTerminalInfo(terminalInfoList);
                terminalInfoList.clear();
                log.info("已缓存{}个终端信息", i);
            }

            //创建离线的终端状态
            TerminalStatusProto terminalStatusProto = new TerminalStatusProto();
            terminalStatusProto.setTerminalInfo(terminalInfo);
            terminalStatusProto.setTerminalStatus(TerminalStatusEnum.OFFLINE);
            terminalStatusProto.setVehicleStatus(VehicleStatusEnum.OFFLINE);
            terminalStatusProto.setNodeName(nodeName);
            terminalStatusService.putLastStatus(terminalId, terminalStatusProto);
            terminalStatusMap.put(terminalInfo.getTerminalStrId(), JSON.toJSONString(terminalStatusProto));
        }
        hashOperations.putAll(redisKey, terminalStatusMap);
        sw.stop();

        //加载页面中添加的保存在数据库的终端信息
        sw.start("加载页面增加的终端信息");
        List<Terminal> terminalList = terminalService.findAll();
        if (!terminalList.isEmpty()) {
            List<TerminalProto> terminalProtoList = terminalList.stream().map(terminal -> {
                TerminalProto terminalProto = new TerminalProto();
                terminalProto.setTerminalId(terminal.getId());
                terminalProto.setTerminalStrId(terminal.getId().toString());
                terminalProto.setTerminalNum(terminal.getTerminalNum());
                terminalProto.setTerminalSimCode(terminal.getPhoneNum());
                terminalProto.setVehicleNum(terminal.getVehicleNum());
                terminalProto.setVehiclePlateColor(terminal.getVehiclePlateColor());

                //创建离线的终端状态
                TerminalStatusProto terminalStatusProto = new TerminalStatusProto();
                terminalStatusProto.setTerminalInfo(terminalProto);
                terminalStatusProto.setTerminalStatus(TerminalStatusEnum.OFFLINE);
                terminalStatusProto.setVehicleStatus(VehicleStatusEnum.OFFLINE);
                terminalStatusService.putLastStatus(terminal.getId(), terminalStatusProto);
                return terminalProto;
            }).collect(Collectors.toList());
            redisService.batchPutTerminalInfo(terminalProtoList);
            log.info("已缓存{}个页面增加的终端信息", terminalProtoList.size());
        }
        sw.stop();
        Stream.of(sw.getTaskInfo()).forEach(taskInfo -> log.info("{}  耗时:{} ms", taskInfo.getTaskName(), taskInfo.getTimeMillis()));
    }

}