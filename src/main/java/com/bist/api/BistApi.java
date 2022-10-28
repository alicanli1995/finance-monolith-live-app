package com.bist.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableFeignClients
@EnableMongoAuditing
@SpringBootApplication
public class BistApi {

    public static void main(String[] args) {
        SpringApplication.run(BistApi.class, args);
    }
}
