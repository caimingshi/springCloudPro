package com.xl.platform.gateway.filter;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xl.platform.core.config.redis.RedisUtil;
import com.xl.platform.core.response.RespInfo;
import com.xl.platform.core.util.jwt.JwtUtil;
import com.xl.platform.core.util.jwt.UserInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * <p>
 * 全局token验证过滤器
 * </p>
 * <pre> Created: 2020/02/28 11:23  </pre>
 *
 * @author caimingshi
 * @version 1.0
 * @since JDK 1.8
 */
@Component
@ConfigurationProperties("xl.platform.jwt")
@Slf4j
@Data
public class AuthFilter implements GlobalFilter , Ordered{

	private static final String HEADER_AUTH = "Authorization";

	private String[] skipAuthUrls;//免验证的路径数组

	@Value("${xl.platform.jwt.sso}")
	private boolean sso;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private RedisUtil redisUtil;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		String url = exchange.getRequest().getURI().getPath();

		//跳过不需要验证的路径
		if(null != skipAuthUrls&& Arrays.asList(skipAuthUrls).contains(url)){
			return chain.filter(exchange);
		}
		//获取token
		String token = exchange.getRequest().getHeaders().getFirst(HEADER_AUTH);
		ServerHttpResponse resp = exchange.getResponse();
		if(StringUtils.isBlank(token)){//head中没有token
			return authError(resp,"请登陆");
		}else{//head中有token
			try {
				Claims claims = JwtUtil.checkToken(token, objectMapper);
				String serviceToken = claims.get("token", String.class);
				String uid = claims.get("uid", String.class);
				//验证内部服务token是否失效,单点登录
				if(sso){
					String userInfoStr = redisUtil.get("xl_".concat(uid));
					if(StringUtils.isNotEmpty(userInfoStr)){
						UserInfo userInfo = JSONObject.parseObject(userInfoStr, UserInfo.class);
						if(!StringUtils.equals(userInfo.getToken(), serviceToken)){
							return authError(resp,"登录已过期");
						}
					}else{
						return authError(resp,"服务认证失败");
					}
				}
				//todo 这里可以根据业务需要进行验证

				//如果认证通过，则传到下层服务
				ServerHttpRequest request = exchange.getRequest();
				ServerHttpRequest.Builder mutate = request.mutate();
				mutate.header("uid", uid);
				mutate.header("token", serviceToken);
				ServerHttpRequest buildReuqest =  mutate.build();
				return chain.filter(exchange.mutate().request(buildReuqest).build());
			}catch (ExpiredJwtException e){
				log.error(e.getMessage(),e);
				if(e.getMessage().contains("Allowed clock skew")){
					return authError(resp,"认证已过期");
				}else{
					return authError(resp,"认证失败");
				}
			}catch (Exception e) {
				log.error(e.getMessage(), e);
				return authError(resp,"认证失败");
			}
		}
	}

	/**
	 * 认证错误输出
	 * @param resp 响应对象
	 * @param mess 错误信息
	 * @return
	 */
	private Mono<Void> authError(ServerHttpResponse resp,String mess) {
		resp.setStatusCode(HttpStatus.OK);
		resp.getHeaders().add("Content-Type","application/json;charset=UTF-8");
		RespInfo respInfo = RespInfo.builder().code(HttpStatus.UNAUTHORIZED.value()).message(mess).Data(mess).build();
		String returnStr = "";
		try {
			returnStr = objectMapper.writeValueAsString(respInfo);
		} catch (JsonProcessingException e) {
			log.error(e.getMessage(),e);
		}
		DataBuffer buffer = resp.bufferFactory().wrap(returnStr.getBytes(StandardCharsets.UTF_8));
		return resp.writeWith(Flux.just(buffer));
	}

    @Override
    public int getOrder() {
        return -100;
    }
}
