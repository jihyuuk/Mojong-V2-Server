package com.jihyuk.mojong_v2.service;

import com.jihyuk.mojong_v2.auth.JwtUtil;
import com.jihyuk.mojong_v2.model.ROLE;
import com.jihyuk.mojong_v2.model.dto.UserParam;
import com.jihyuk.mojong_v2.model.entity.Guest;
import com.jihyuk.mojong_v2.model.entity.User;
import com.jihyuk.mojong_v2.repository.GuestRepository;
import com.jihyuk.mojong_v2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final GuestRepository guestRepository;
    private final JwtUtil jwtUtil;

    //회원가입
    @Transactional
    public void joinUser(UserParam param) {
        String username = param.getUsername();
        String password = param.getPassword();

        //1. 중복체크
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("이미 존재하는 아이디 입니다.");
        }

        //2. 유저 생성 및 저장
        User user = new User(username, password, ROLE.ROLE_STAFF);
        userRepository.save(user);
    }

    //로그인
    @Transactional
    public String loginUser(UserParam param) {
        //간단하게 하기위해서 BCryptPasswordEncoder 생략
        String username = param.getUsername();
        String password = param.getPassword();

        //user 조회
        Optional<User> user = userRepository.findByUsername(username);
        //검증
        if (user.isEmpty() || !user.get().getPassword().equals(password)) {
            throw new BadCredentialsException("아이디 또는 비밀번호가 틀렸습니다.");
        }
        if (!user.get().isEnabled()){
            throw new AccessDeniedException("접속이 거부되었습니다.");
        }

        //응답 반환
        return jwtUtil.generateToken(user.get().getUsername(), user.get().getRole());
    }


    //게스트 토큰 발행
    public String guestToken() {
        //나중에 qr 닫으면 토큰 발행 정지 하는 코드 고려
        
        //게스트 생성
        Guest guest = new Guest();
        guestRepository.save(guest);

        //토큰발행
        return jwtUtil.generateToken(guest.getId(), ROLE.ROLE_GUEST);
    }
}
