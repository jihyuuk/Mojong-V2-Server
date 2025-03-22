package com.jihyuk.mojong_v2.service;

import com.jihyuk.mojong_v2.model.ROLE;
import com.jihyuk.mojong_v2.model.dto.JoinParam;
import com.jihyuk.mojong_v2.model.entity.User;
import com.jihyuk.mojong_v2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    //회원가입
    @Transactional
    public void joinUser(JoinParam param){
        String username = param.getUsername();
        String password = param.getPassword();

        //1. 중복체크
        if(userRepository.existsByUsername(username)){
            throw new IllegalArgumentException("이미 존재하는 아이디 입니다.");
        }

        //2. 유저 생성 및 저장
        User user = new User(username, password, ROLE.ROLE_STAFF);
        userRepository.save(user);
    }

    //로그인


}
