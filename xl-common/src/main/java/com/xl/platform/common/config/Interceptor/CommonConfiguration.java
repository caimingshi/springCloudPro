package com.xl.platform.common.config.Interceptor;

import feign.Feign;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
public class CommonConfiguration extends WebMvcConfigurerAdapter{
	/**
	 * 请求拦截器
	 */
	@Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserContextInterceptor());
    }
	
    /**
     * 创建Feign请求拦截器，在发送请求前设置认证的用户上下文信息
     */
    @Bean
    @ConditionalOnClass(Feign.class)
    public FeignUserContextInterceptor feignTokenInterceptor() {
        return new FeignUserContextInterceptor();
    }

    @Bean
    public RequestContextListener requestContextListener(){
        return new RequestContextListener();
    }
    @Bean
    public SpringCloudHystrixConcurrencyStrategy springCloudHystrixConcurrencyStrategy() {
        return new SpringCloudHystrixConcurrencyStrategy();
    }
}
