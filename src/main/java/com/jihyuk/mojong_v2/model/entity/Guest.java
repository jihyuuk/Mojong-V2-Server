package com.jihyuk.mojong_v2.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
public class Guest {

    @Id
    @Column(name = "guest_id", columnDefinition = "CHAR(36)")
    private String id;

    private LocalDateTime createdDate;

    @PrePersist
    private void prePersist(){
        createdDate = LocalDateTime.now();
    }

    public Guest() {
        id = UUID.randomUUID().toString();
    }
}
