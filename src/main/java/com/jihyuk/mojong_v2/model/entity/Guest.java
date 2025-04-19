package com.jihyuk.mojong_v2.model.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
public class Guest {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "guest_id")
    private Long id;

    private String name;

    private LocalDateTime createdDate;

    @PrePersist
    protected void prePersist(){
        createdDate = LocalDateTime.now();
        name = "guest_"+UUID.randomUUID();
    }

}
