package com.xl.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringCloudApplication
@EnableEurekaClient
@EnableFeignClients(basePackages = "com.xl.platform.**.api.service")
@EnableHystrix
@EnableAsync
public class AServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(AServerApplication.class, args);
    }

}
