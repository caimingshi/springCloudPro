/*
 * 合肥币秀科技有限公司
 * Copyright (C) 2019 All Rights Reserved.
 */
package com.xl.platform.a.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("adminuser")
public class AdminUser implements Serializable {
    private static final long serialVersionUID = 3868776065020082292L;
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String email;

    private String mobile;

    private String passWord;

    private Integer roleId;

    private String roleName;

    private Integer sex;

    private String userName;
}
