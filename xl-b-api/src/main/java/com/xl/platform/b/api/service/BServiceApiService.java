package com.xl.platform.b.api.service;


import com.xl.platform.b.api.fallback.BClientFallback;
import com.xl.platform.b.api.model.User;
import com.xl.platform.core.response.RespInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@FeignClient(name = "xl-b-server", fallback = BClientFallback.class)
public interface BServiceApiService {
	
	@RequestMapping(value = "/b/testB/test1", method = RequestMethod.GET)
    RespInfo test1(User user);
    
    @RequestMapping(value = "/b/testB/test2", method = RequestMethod.GET)
    RespInfo test2();

}
