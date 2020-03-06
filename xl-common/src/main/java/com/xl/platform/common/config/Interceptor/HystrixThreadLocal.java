package com.xl.platform.common.config.Interceptor;

public class HystrixThreadLocal {
    public static ThreadLocal<String> threadLocal = new ThreadLocal<>();
}