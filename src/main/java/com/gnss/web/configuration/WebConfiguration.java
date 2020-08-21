package com.gnss.web.configuration;

import com.gnss.core.service.SpringBeanService;
import com.gnss.mqutil.event.DownCommandRegister;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <p>Description: web配置</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2019-1-18
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Value("${gnss.downCommand.scanPrefix}")
    private String scanPrefix;

    /**
     * Spring Bean服务
     *
     * @return
     */
    @Bean
    public SpringBeanService springBeanService() {
        return new SpringBeanService();
    }

    /**
     * 下行指令注册器
     *
     * @return
     */
    @Bean
    public DownCommandRegister downCommandRegister() {
        DownCommandRegister downCommandRegister = new DownCommandRegister(scanPrefix);
        return downCommandRegister;
    }

    /**
     * 跨域策略
     *
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*")
                .allowCredentials(true)
                .maxAge(3600 * 24);
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.defaultContentType(MediaType.APPLICATION_JSON);
    }

    /**
     * swagger请求映射
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }
}