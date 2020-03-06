package com.xl.platform.common.config.Interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 *  Feign传递用户上下文
 *  @author cms
 */
public class FeignUserContextInterceptor implements RequestInterceptor {

	@Override
	public void apply(RequestTemplate template) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        template.header("token", request.getHeader("token"));
        template.header("uid", request.getHeader("uid"));
	}

}
