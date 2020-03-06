package com.xl.platform.gateway.config;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * apollo配置
 * </p>
 * <pre> Created: 2020/02/22 12:07  </pre>
 *
 * @author caimingshi
 * @version 1.0
 * @since JDK 1.8
 */
@Configuration
@EnableApolloConfig(value = "application", order = 10)
public class GatewayApolloConfig {
}
