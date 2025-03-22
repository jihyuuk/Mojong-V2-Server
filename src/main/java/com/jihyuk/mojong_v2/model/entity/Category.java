package com.jihyuk.mojong_v2.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    private String name;

    private boolean enabled = true;

    @OneToMany(mappedBy = "category")
    private List<Item> items = new ArrayList<>();

    public Category(String name) {
        this.name = name;
    }
}
