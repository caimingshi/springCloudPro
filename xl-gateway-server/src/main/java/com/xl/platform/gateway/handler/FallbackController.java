package com.xl.platform.gateway.handler;

import com.xl.platform.core.response.RespInfo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 网关层面全局降级
 * </p>
 * <pre> Created: 2020-03-01 10:57  </pre>
 *
 * @author caimingshi
 * @version 1.0
 * @since JDK 1.8
 */
@RestController
public class FallbackController {

    @GetMapping("/fallback")
    public RespInfo fallback() {
        return RespInfo.builder().code(HttpStatus.OK.value()).message("系统繁忙，请稍后再试").Data(null).build();
    }

}
