package com.xl.platform.a.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xl.platform.a.entity.AdminUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


@Mapper
public interface AdminUserMapper extends BaseMapper<AdminUser> {
}
