package com.xl.platform.a.controller;

import com.alibaba.fastjson.JSONObject;
import com.xl.platform.a.entity.AdminUser;
import com.xl.platform.a.hystrix.TestServiceCommand;
import com.xl.platform.a.service.AdminUserService;
import com.xl.platform.b.api.model.User;
import com.xl.platform.b.api.service.BServiceApiService;
import com.xl.platform.common.config.web.RequestJson;
import com.xl.platform.common.exception.ErrorCode;
import com.xl.platform.common.intercepter.UserContextHolder;
import com.xl.platform.core.config.redis.RedisUtil;
import com.xl.platform.core.response.RespInfo;
import com.xl.platform.core.util.RandomUtil;
import com.xl.platform.core.util.jwt.JwtUtil;
import com.xl.platform.core.util.jwt.UserInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.List;

/**
 * @version 1.0
 * @Author caimingshi
 * @ClassName TestController
 * @Date 2020/2/28 0:00
 * @Since JDK 1.8
 * @Description TODO
 **/
@RestController
@RequestMapping("/testA")
@Api(tags = {"A服务测试"})
public class TestAController {

    @Autowired
    private final BServiceApiService bServiceApiService = null;
    @Autowired
    private final AdminUserService adminUserService = null;
    @Autowired
    private RedisUtil redisUtil;

    @PostMapping("/test1")
    @ApiOperation("新增用户测试")
    public RespInfo test1(@RequestBody @ApiParam(name="用户",value="传入json格式",required=true) User user){
        System.out.println(Thread.currentThread().getName());
        return RespInfo.builder().Data(bServiceApiService.test1(user)).code(ErrorCode.OK.getCode()).build();
    }

    @GetMapping("/test2")
    @ApiOperation("查询用户测试")
    public RespInfo test2(){
        List<AdminUser> adminUserList = adminUserService.selectList(null);
        return RespInfo.builder().Data(adminUserList).code(ErrorCode.OK.getCode()).build();
    }

    @PostMapping("/test3")
    public RespInfo test2(@RequestJson String lala){
        return RespInfo.builder().Data(lala).code(ErrorCode.OK.getCode()).build();
    }

    @GetMapping("/test4")
    public RespInfo test4(){
        RespInfo result = new TestServiceCommand("lalala").execute();
        return result;
    }

    @GetMapping("/test5")
    public RespInfo test5(){
        return RespInfo.builder().Data("testA5").code(ErrorCode.OK.getCode()).build();
    }

    @ApiOperation("登录")
    @GetMapping("/login")
    public RespInfo login(){
        //默认登录成功
        //存放用户信息
        UserInfo userInfo = new UserInfo();
        userInfo.setUid("111111111111111111");
        userInfo.setRoleId(1);
        userInfo.setUserId(2);
        userInfo.setUserName("zhangsan");
        userInfo.setRoleList(new ArrayList<>());
        //生成token,不需要过期时间
        String token = RandomUtil.generateString(32);
        userInfo.setToken(token);
        redisUtil.set("xl_111111111111111111", JSONObject.toJSONString(userInfo));
        //生成一个网关token
        String gatewayToken = JwtUtil.createJWT("111111111111111111", token, "test", "test", 100000000);
        return RespInfo.builder().Data(gatewayToken).code(ErrorCode.OK.getCode()).build();
    }
}
