package com.xl.platform.common.config.Interceptor;

import com.xl.platform.common.intercepter.UserContextHolder;
import com.xl.platform.core.util.jwt.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

/**
 * 获取请求信息，存储用户信息
 */
public class UserContextInterceptor implements HandlerInterceptor {
	private static final Logger log = LoggerFactory.getLogger(UserContextInterceptor.class);
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse respone, Object arg2) throws Exception {
		//从head中拿到信息往下传递
		UserInfo userInfo = new UserInfo();
		userInfo.setToken(request.getHeader("token"));
		userInfo.setUid(request.getHeader("uid"));
		UserContextHolder.set(userInfo);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse respone, Object arg2, ModelAndView arg3)
			throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse respone, Object arg2, Exception arg3)
			throws Exception {
		UserContextHolder.shutdown();
	}
	
	
	
}
