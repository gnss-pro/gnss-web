package com.gnss.web;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
@EnableJpaAuditing
@EnableCaching
@EnableSwagger2Doc
public class GnssWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(GnssWebApplication.class, args);
    }

}