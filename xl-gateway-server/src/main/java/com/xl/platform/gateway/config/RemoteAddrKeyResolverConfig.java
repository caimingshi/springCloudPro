package com.xl.platform.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import reactor.core.publisher.Mono;

/**
 * <p>
 * 网关限流配置
 * </p>
 * <pre> Created: 2020/02/22 12:07  </pre>
 *
 * @author caimingshi
 * @version 1.0
 * @since JDK 1.8
 */
@Configuration
public class RemoteAddrKeyResolverConfig {

    /**
     * 基于ip限流
     * @return
     */
    @Bean
    @Primary
    public KeyResolver ipKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getRemoteAddress().getHostString());
    }


    /**
     * 基于用户限流（可自定义规则）
     * @return
     */
    @Bean
    KeyResolver userKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getQueryParams().getFirst("userId"));
    }


    /**
     * 基于接口限流
     * @return
     */
    @Bean
    KeyResolver apiKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getPath().value());
    }

}
