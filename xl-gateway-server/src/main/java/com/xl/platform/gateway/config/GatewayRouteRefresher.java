package com.xl.platform.gateway.config;

import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import com.xl.platform.gateway.route.DynamicRouteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 用来监听apollo中的网关路由配置，如果改变则动态更新（多实例通用）
 * </p>
 * <pre> Created: 2020-03-02 20:57  </pre>
 *
 * @author caimingshi
 * @version 1.0
 * @since JDK 1.8
 */
@Component
@Slf4j
public class GatewayRouteRefresher implements ApplicationContextAware {

  private ApplicationContext applicationContext;
  @Autowired
  private DynamicRouteService dynamicRouteService;
  @Autowired
  private GatewayProperties gatewayProperties;

  @ApolloConfigChangeListener(value = "application")
  public void onChange(ConfigChangeEvent changeEvent) {
    boolean gatewayRouteChanged = false;
    for (String changedKey : changeEvent.changedKeys()) {
      //如果更改的key是网关配置信息
      if (changedKey.startsWith("spring.cloud.gateway.")) {
        gatewayRouteChanged = true;
        break;
      }
    }
    if (gatewayRouteChanged) {
      gatewayPropertRefresher(changeEvent);
    }
  }

  /**
   * 刷新路由信息
   * @param changeEvent
   */
  private void gatewayPropertRefresher(ConfigChangeEvent changeEvent) {
    log.info("刷新路由配置");
    //更新配置
    this.applicationContext.publishEvent(new EnvironmentChangeEvent(changeEvent.changedKeys()));
    //更新路由
    gatewayProperties.getRoutes().forEach(dynamicRouteService::update);
    log.info("路由刷新成功");
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }
}
