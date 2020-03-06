package com.xl.platform.b.api.fallback;



import com.xl.platform.b.api.model.User;
import com.xl.platform.b.api.service.BServiceApiService;
import com.xl.platform.common.exception.ErrorCode;
import com.xl.platform.core.response.RespInfo;
import org.springframework.stereotype.Component;

/**
 */
@Component
public class BClientFallback implements BServiceApiService {
	
	@Override
	public RespInfo test1(User user) {
		return RespInfo.builder().code(ErrorCode.BUSI_EXCEPTION.getCode()).Data("test1降級").build();
	}
	@Override
	public RespInfo test2() {
		return RespInfo.builder().code(ErrorCode.BUSI_EXCEPTION.getCode()).Data("test2降級").build();
	}
	
}
