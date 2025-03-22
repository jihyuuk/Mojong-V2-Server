package com.jihyuk.mojong_v2.controller;

import com.jihyuk.mojong_v2.model.dto.JoinParam;
import com.jihyuk.mojong_v2.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    //회원가입
    @PostMapping("/join")
    public ResponseEntity<String> join(@Valid JoinParam param){
        try {
            authService.joinUser(param);
            return ResponseEntity.ok("회원 가입 성공!");
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(CONFLICT).body(e.getMessage());
        }
    }


}
