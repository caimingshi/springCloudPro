package com.xl.platform.b.controller;

import com.alibaba.fastjson.JSONObject;
import com.xl.platform.a.api.model.User;
import com.xl.platform.common.config.web.RequestJson;
import com.xl.platform.common.exception.ErrorCode;
import com.xl.platform.common.intercepter.UserContextHolder;
import com.xl.platform.core.response.RespInfo;
import com.xl.platform.core.util.jwt.UserInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @version 1.0
 * @Author caimingshi
 * @ClassName TestBController
 * @Date 2020/2/28 0:00
 * @Since JDK 1.8
 * @Description TODO
 **/
@RestController
@RequestMapping("/testB")
@Api(tags = {"B服务测试"})
public class TestBController {

    @GetMapping("/test1")
    @ApiOperation("新增用户测试1")
    public RespInfo test1(User user){
        UserInfo userInfo = UserContextHolder.currentUser();
        return RespInfo.builder().Data(userInfo).code(ErrorCode.OK.getCode()).build();
    }

    @GetMapping("/test2")
    @ApiOperation("新增用户测试2")
    public RespInfo test2(){
        return RespInfo.builder().Data("testB2").code(ErrorCode.OK.getCode()).build();
    }

    @PostMapping("/test3")
    public RespInfo test2(@RequestJson String lala){
        return RespInfo.builder().Data(lala).code(ErrorCode.OK.getCode()).build();
    }

    @GetMapping("/test5")
    public RespInfo test5(){
        return RespInfo.builder().Data("testB5").code(ErrorCode.OK.getCode()).build();
    }
}
