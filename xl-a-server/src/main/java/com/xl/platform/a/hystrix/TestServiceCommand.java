package com.xl.platform.a.hystrix;


import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.xl.platform.core.response.RespInfo;
import org.springframework.stereotype.Component;

public class TestServiceCommand extends HystrixCommand<RespInfo>{
	
	 	private final String name;
	 	
	    public TestServiceCommand(String name) {
	        super(HystrixCommandGroupKey.Factory.asKey("A"));
	        this.name = name;
	    }

	    @Override
	    protected RespInfo run() throws Exception{
	    	throw new Exception(name);
	    }
	    
	    @Override
	    protected RespInfo getFallback() {
			return RespInfo.builder().Data("aaafallback").build();
	    }
	    
}

