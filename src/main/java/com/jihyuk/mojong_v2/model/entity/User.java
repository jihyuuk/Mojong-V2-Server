package com.jihyuk.mojong_v2.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jihyuk.mojong_v2.model.enums.ROLE;
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

    @JsonIgnore
    @Column(nullable = false)
    private String password; //비밀번호

    @Enumerated(EnumType.STRING)
    private ROLE role; //권한

    private boolean enabled; //활성화 여부

    private boolean approved; //가입신청 승인 여부

    @JsonFormat(pattern = "YYYY.MM.dd HH:mm")
    private LocalDateTime createdDate; //가입일

    //가입일 자동 등록
    @PrePersist
    protected void prePersist(){
        createdDate = LocalDateTime.now();
    }

    public User(String username, String password, ROLE role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}
