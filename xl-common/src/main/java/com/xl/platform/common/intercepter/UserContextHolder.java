package com.xl.platform.common.intercepter;

import com.xl.platform.core.util.jwt.UserInfo;

/**
 * 本地线程存放用户信息
 */
public class UserContextHolder {
	public static ThreadLocal<UserInfo> context = new InheritableThreadLocal<UserInfo>();
	public static UserInfo currentUser() {
		return context.get();
	}
	public static void set(UserInfo user) {
		context.set(user);
	}
	public static void shutdown() {
		context.remove();
	}
}
