package com.xl.platform.core.util.jwt;

import lombok.*;

import java.util.List;

/**
 * <p>
 * 用户信息模型
 * </p>
 * <pre> Created: 2020/02/28 12:27  </pre>
 *
 * @author caimingshi
 * @version 1.0
 * @since JDK 1.8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {

    private Integer userId;//用户id

    private String userName;//用户名

    private String uid; //用户唯一标识

    private String token;//用于内部服务验证的token

    private Integer roleId;//角色id

    private List<String> roleList;

}
