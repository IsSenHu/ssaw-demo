package com.ssaw.uaa.util;

import com.alibaba.fastjson.JSON;
import com.ssaw.ssawauthenticatecenterfeign.vo.user.UserInfoVO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.HashMap;
import java.util.Map;

/**
 * Jwt工具类
 * @author HuSen
 * @date 2019/4/27 14:13
 */
public class JwtUtil {

    private static final String SECRET = "HuSen";

    public static final String HEADER_AUTH = "Authorization";

    public static String generateToken(UserInfoVO user) {
        HashMap<String, Object> map = new HashMap<>(1);
        map.put("user", user);
        return Jwts.builder()
                .setSubject("userInfo").setClaims(map)
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    public static UserInfoVO validateToken(String token) {
        if (token != null) {
            Map<String, Object> body = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token)
                    .getBody();

            return JSON.parseObject(JSON.toJSONString(body.get("user")), UserInfoVO.class);
        } else {
            throw new RuntimeException("token is error, please check");
        }
    }
}