package com.jihyuk.mojong_v2.auth;

import io.jsonwebtoken.Jwts;
import com.jihyuk.mojong_v2.model.ROLE;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

@Component
public class JwtUtil {

    //시크릿 키
    private static SecretKey SECRET_KEY;
    //만료일
    public static final long EXPIRATION_TIME = 1000L * 60 * 60 * 24 * 60; // 60일

    //시크릿 키 생성자 등록
    public JwtUtil(@Value("${spring.jwt.key}") String key) {
        SECRET_KEY = new SecretKeySpec(key.getBytes(), "HmacSHA256");
    }

    //토큰 생성
    //refresh token 없이 access token 하나만으로 간단히 구현
    public String generateToken(String username, ROLE role){
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .claim("username", username)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) //3달
                .signWith(SECRET_KEY)
                .compact();
    }

    //토큰으로 부터 username 추출
    public String getUsernameFromToken(String token){
        return Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token).getPayload().get("username", String.class);
    }

    //토큰으로 부터 role 추출
    public String getRoleFromToken(String token){
        return Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }

    // 토큰 유효성 검사
    public boolean validateToken(String token)  {
        Jwts.parser().verifyWith(SECRET_KEY).build().parseClaimsJws(token);//런타임 에러로 발생(김영한님께 배운 내용이닷 ㅋㅋㅋ)
        return true;
    }
}
