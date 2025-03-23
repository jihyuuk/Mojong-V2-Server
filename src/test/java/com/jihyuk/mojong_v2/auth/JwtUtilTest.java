package com.jihyuk.mojong_v2.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jihyuk.mojong_v2.model.enums.ROLE;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

import static com.jihyuk.mojong_v2.model.enums.ROLE.*;
import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private static JwtUtil util = new JwtUtil("this-is-test-secret-key-for-jwt-token");
    private final  String TEST_USERNAME = "홍길동";
    private final ROLE TEST_ROLE = ROLE_ADMIN;

    @Test
    void generateToken() throws JsonProcessingException {
        //세팅
        String token = util.generateToken(TEST_USERNAME, TEST_ROLE);

        String[] split = token.split("\\.");

        String header = new String(Base64.getDecoder().decode(split[0]), StandardCharsets.UTF_8);
        String payload = new String(Base64.getDecoder().decode(split[1]), StandardCharsets.UTF_8);

        ObjectMapper om = new ObjectMapper();
        Map<String, Object> headerMap = om.readValue(header, Map.class);
        Map<String, Object> payloadMap = om.readValue(payload, Map.class);

        //헤더 검증
        assertEquals(headerMap.get("alg"), "HS256");
        assertEquals(headerMap.get("typ"), "JWT");

        //payload의 username, role 검증
        assertEquals(payloadMap.get("username"), TEST_USERNAME);
        assertEquals(payloadMap.get("role"), TEST_ROLE.toString());

        //만료일 검증
        long exp = (Integer)payloadMap.get("exp") - (Integer)payloadMap.get("iat");
        //assertEquals(exp * 1000, JwtUtil.EXPIRATION_TIME);

        //출력해보기
        System.out.println("token = " + token);
        System.out.println("header = " + header);
        System.out.println("payload = " + payload);
    }

    @Test
    void getUsernameFromToken() {
        //case
        String token = util.generateToken(TEST_USERNAME, TEST_ROLE);
        //when
        String getUsername = util.getUsernameFromToken(token);
        //then
        assertEquals(getUsername, TEST_USERNAME);
    }

    @Test
    void getRoleFromToken() {
        //case
        String token = util.generateToken(TEST_USERNAME, TEST_ROLE);
        //when
        String role = util.getRoleFromToken(token);
        //then
        assertEquals(role, TEST_ROLE.toString());
    }

    @Test
    void validateToken() {
        //정상 토큰
        String token = util.generateToken(TEST_USERNAME, TEST_ROLE);
        //만료일 지난 토큰
        String expiredToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6Iu2Zjeq4uOuPmSIsInJvbGUiOiJST0xFX0FETUlOIiwiaWF0IjoxNzQyNjAxOTQyLCJleHAiOjE3NDI2MDE5NDJ9.YXcJg2AuAiikXqXleKU-xjqW9pj5e2x4mmrJd-o6Xjw";
        //jwt 구조 안 맞는 토큰
        String fakeToken ="dab0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6Iu2Zjeq4uOuPmSIsInJvbGUiOiJST0xFX0FETUlOIiwiaWF0IjoxNzQyNjAxOTQyLCJleHAiOjE3NDI2MDE5NDJ9.YXcJg2AuAiikXqXleKU-xjqW9pj5e2x4mmrJd-o6Xjc";
        //시그니처 안 맞는 토큰
        String signatureToken="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6Iu2Zjeq4uOuPmSIsInJvbGUiOiJST0xFX0FETUlOIiwiaWF0IjoxNzEzNzA2OTQwLCJleHAiOjE3MjE0ODI5NDB9.IddFig77o3OHp_rEUZLVsON8IKRmSRoxpuk_tmK7DGU";

        //정상 토큰 검증
        assertEquals(util.validateToken(token), true);
        //만료일 지난 토큰 검증
        assertThrows(ExpiredJwtException.class, () -> util.validateToken(expiredToken));
        //jwt 구조 안 맞는 토큰 검증
        assertThrows(MalformedJwtException.class, () -> util.validateToken(fakeToken));
        //시그니처 안 맞는 토큰 검증
        assertThrows(SignatureException.class, () -> util.validateToken(signatureToken));
    }
}