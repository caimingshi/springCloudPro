package com.xl.platform.gateway.filter;

import com.xl.platform.gateway.http.RecorderServerHttpRequestDecorator;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;

/**
 * <p>
 * 全局日志打印过滤器，用来打印全局请求和响应信息
 * </p>
 * <pre> Created: 2020/02/28 12:27  </pre>
 *
 * @author caimingshi
 * @version 1.0
 * @since JDK 1.8
 */
@Slf4j
@Component
public class AccessLogGlobalFilter implements GlobalFilter, Ordered {
 
    private static final String REQUEST_PREFIX = "请求信息： [ ";
 
    private static final String REQUEST_TAIL = " ]";
 
    private static final String RESPONSE_PREFIX = "；响应信息： [ ";
 
    private static final String RESPONSE_TAIL = " ]";
 
    private StringBuilder normalMsg = new StringBuilder();
 
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        RecorderServerHttpRequestDecorator requestDecorator = new RecorderServerHttpRequestDecorator(request);
        InetSocketAddress address = requestDecorator.getRemoteAddress();
        HttpMethod method = requestDecorator.getMethod();
        URI url = requestDecorator.getURI();
        HttpHeaders headers = requestDecorator.getHeaders();
        Flux<DataBuffer> body = requestDecorator.getBody();
        //读取requestBody传参
        AtomicReference<String> requestBody = new AtomicReference<>("");
        body.subscribe(buffer -> {
            CharBuffer charBuffer = StandardCharsets.UTF_8.decode(buffer.asByteBuffer());
            requestBody.set(charBuffer.toString());
        });
        String requestParams = requestBody.get();
        normalMsg.append(REQUEST_PREFIX);
        normalMsg.append("请求头：").append(headers);
        normalMsg.append("；参数：").append(requestParams);
        normalMsg.append("；ip：").append(address.getHostName() + address.getPort());
        normalMsg.append("；方式：").append(method.name());
        normalMsg.append("；路径：").append(url.getPath());
        normalMsg.append(REQUEST_TAIL);
 
        ServerHttpResponse response = exchange.getResponse();
 
        DataBufferFactory bufferFactory = response.bufferFactory();
        normalMsg.append(RESPONSE_PREFIX);
        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(response) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                if (body instanceof Flux) {
                    Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;
                    return super.writeWith(fluxBody.map(dataBuffer -> {
                        byte[] content = new byte[dataBuffer.readableByteCount()];
                        dataBuffer.read(content);
                        String responseResult = new String(content, Charset.forName("UTF-8"));
                        normalMsg.append("状态：").append(this.getStatusCode());
                        normalMsg.append("；响应体：").append(responseResult);
                        normalMsg.append(RESPONSE_TAIL);
                        log.info(normalMsg.toString());
                        return bufferFactory.wrap(content);
                    }));
                }
                return super.writeWith(body);
            }
        };
 
        return chain.filter(exchange.mutate().request(requestDecorator).response(decoratedResponse).build());
    }
 
    @Override
    public int getOrder() {
        return -200;
    }
 
}