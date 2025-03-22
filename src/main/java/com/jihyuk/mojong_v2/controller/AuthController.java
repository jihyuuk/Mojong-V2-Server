package com.jihyuk.mojong_v2.controller;

import com.jihyuk.mojong_v2.model.dto.UserParam;
import com.jihyuk.mojong_v2.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    //회원가입
    @PostMapping("/join")
    public ResponseEntity<String> join(@Valid UserParam param) {
        try {
            authService.joinUser(param);
            return ResponseEntity.ok("회원 가입 성공!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(CONFLICT).body(e.getMessage());
        }
    }

    //로그인
    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid UserParam param) {
        try {
            String token = authService.loginUser(param);
            return ResponseEntity.ok()
                    .header("Authorization", "Bearer " + token)//헤더에 토큰
                    .body("로그인 성공! " + param.getUsername() + "님 안녕하세요.");
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(UNAUTHORIZED).body(e.getMessage());
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(FORBIDDEN).body(e.getMessage());
        }
    }

    //게스트
    @GetMapping("/guest-token")
    public ResponseEntity<String> guest() {
        String guestToken = authService.guestToken();

        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + guestToken)
                .body("토큰 정상 발행");
    }


}
