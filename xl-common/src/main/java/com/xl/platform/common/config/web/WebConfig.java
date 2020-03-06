package com.xl.platform.common.config.web;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * <p> </p>
 *
 * <pre> Created: 2019/8/20 13:24 </pre>
 *
 * @author cms
 * @version 1.0
 * @since JDK 1.8
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final RequestJsonHandlerMethodArgumentResolver requestJsonHandlerMethodArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(requestJsonHandlerMethodArgumentResolver);
        WebMvcConfigurer.super.addArgumentResolvers(resolvers);
    }
}
