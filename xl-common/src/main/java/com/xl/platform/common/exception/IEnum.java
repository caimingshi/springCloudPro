package com.xl.platform.common.exception;
public interface IEnum<T> {

    /**
     * 返回code
     * @return
     */
    T getCode();

    String getMessage();
}
