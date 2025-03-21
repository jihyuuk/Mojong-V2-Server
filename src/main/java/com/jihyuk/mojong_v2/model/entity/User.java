package com.jihyuk.mojong_v2.model.entity;

import com.jihyuk.mojong_v2.model.ROLE;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id; //pk

    @Column(nullable = false, unique = true)
    private String username; //유저 아이디

    @Column(nullable = false)
    private String password; //비밀번호

    @Enumerated(EnumType.STRING)
    private ROLE role; //권한

    private boolean enabled; //활성화 여부

    private boolean approved; //가입신청 승인 여부

    private LocalDateTime createdDate; //가입일

    //가입일 자동 등록
    @PrePersist
    protected void prePersist(){
        createdDate = LocalDateTime.now();
    }

}
