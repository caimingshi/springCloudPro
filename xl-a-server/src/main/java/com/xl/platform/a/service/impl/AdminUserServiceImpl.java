/*
 * 合肥币秀科技有限公司
 * Copyright (C) 2019 All Rights Reserved.
 */
package com.xl.platform.a.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xl.platform.a.entity.AdminUser;
import com.xl.platform.a.mapper.AdminUserMapper;
import com.xl.platform.a.service.AdminUserService;
import org.springframework.stereotype.Service;

@Service
public class AdminUserServiceImpl
        extends ServiceImpl<AdminUserMapper, AdminUser>
        implements AdminUserService {
}
