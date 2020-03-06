package com.xl.platform.core.util.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * jwt工具类
 * </p>
 * <pre> Created: 2020/02/18 12:27  </pre>
 *
 * @author caimingshi
 * @version 1.0
 * @since JDK 1.8
 */
@Slf4j
public class JwtUtil {

    public static final String KEY = "xl_platform_2020";

    /**
     * 由字符串生成加密key
     *
     * @return
     */
    public static SecretKey generalKey(){
        byte[] encodedKey = Base64.decodeBase64(KEY);
        SecretKeySpec key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        return key;
    }

    /**
     * 创建jwt
     * @param uid 用户唯一标识
     * @param token 下游服务验证token
     * @param issuer 签发者
     * @param subject 主题
     * @param ttlMillis 过期时间
     * @return
     * @throws Exception
     */
    public static String createJWT(String uid, String token, String issuer, String subject, long ttlMillis){

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        // 生成JWT的时间
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        Map<String, Object> claims = new HashMap<>();
        claims.put("uid", uid);
        claims.put("token", token);

        // 生成签名的时候使用的秘钥secret
        SecretKey key = generalKey();

        // 下面就是在为payload添加各种标准声明和私有声明了
        JwtBuilder builder = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setIssuer(issuer)
                .setSubject(subject)
                .signWith(signatureAlgorithm, key); // 设置签名使用的签名算法和签名使用的秘钥

        // 设置过期时间
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }
        return builder.compact();
    }

    /**
     * 解密jwt
     *
     * @param jwt
     * @return
     * @throws Exception
     */
    public static Claims parseJWT(String jwt){
        SecretKey key = generalKey();  //签名秘钥，和生成的签名的秘钥一模一样
        Claims claims = Jwts.parser()  //得到DefaultJwtParser
                .setSigningKey(key)                 //设置签名的秘钥
                .parseClaimsJws(jwt).getBody();     //设置需要解析的jwt
        return claims;
    }

    /**
     * 检查token
     * @return
     */
    public static Claims checkToken(String jwtToken, ObjectMapper objectMapper) throws Exception {
        Claims claims = JwtUtil.parseJWT(jwtToken);
        String subject = claims.getSubject();
        /*
            TODO 对jwt里面的用户信息做判断
            根据自己的业务编写
         */
        return claims;
    }

    public static void main(String[] args) {
        System.out.println(createJWT("111122", "tttttt33333333333323223333333333", "test", "test", 100000000));
//        Claims claims = parseJWT("eyJhbGciOiJIUzI1NiJ9.eyJ1aWQiOiIxMTExMjIiLCJzdWIiOiJ0ZXN0IiwiaXNzIjoidGVzdCIsImV4cCI6MTU4MzA1OTk0MywiaWF0IjoxNTgzMDU5OTMzLCJ0b2tlbiI6IjU1NTU1NSJ9.AC4F0HfOqSy1ApNp-tlftDESGQsHfP-doD0ro--53uY");
//        System.out.println(claims.getExpiration());
    }

}
