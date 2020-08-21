package com.gnss.web.configuration;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Scheduler;
import com.gnss.core.proto.CommandProto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * <p>Description: Caffeine配置</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2019-10-18
 */
@Slf4j
@Configuration
public class CaffeineConfiguration {

    @Value("${gnss.downCommand.expireTime}")
    private int downCommandExpireTime;

    /**
     * 下行指令缓存,五分钟超时
     *
     * @return
     */
    @Bean
    public Cache<String, CompletableFuture<CommandProto>> downCommandCache() {
        Caffeine caffein = Caffeine.newBuilder()
                .recordStats()
                .expireAfterWrite(downCommandExpireTime, TimeUnit.SECONDS)
                .scheduler(Scheduler.forScheduledExecutorService(Executors.newSingleThreadScheduledExecutor()))
                .removalListener((key, value, cause) -> {
                    log.debug("移除下行指令,键:{},值:{},cause:{}", key, value, cause);
                });
        return caffein.build();
    }

}