package com.xl.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringCloudApplication
@EnableEurekaClient
@EnableFeignClients(basePackages = "com.xl.platform.**.api.service")
public class BServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(BServerApplication.class, args);
    }

}
