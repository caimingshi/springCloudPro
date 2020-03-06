package com.xl.platform.core.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author caimingshi
 * @version 1.0
 * @ClassName RespInfo
 * @date 2019/8/6
 * description：接口响应基类
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RespInfo implements Serializable {
    private int code;
    private String message;
    private Object Data;
}