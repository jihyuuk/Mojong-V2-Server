package com.jihyuk.mojong_v2.model.entity;

import com.jihyuk.mojong_v2.model.dto.ItemParam;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false)
    private String name; //아이템명
    
    private String description; //설명
    
    private String photo; //사진

    @Column(nullable = false)
    private int price; //가격

    @Column(nullable = false)
    private int stock; //수량

    private int seq; //순서
    
    private boolean isPublic = true; //공개 여부

    public Item(Category category, ItemParam param) {
        this.category = category;
        this.name = param.getName().trim();
        this.description = param.getDescription().trim();
        this.photo = param.getPhoto().trim();
        this.price = param.getPrice();
        this.stock = param.getStock();
    }

    public void update(ItemParam param){
        this.name = param.getName().trim();
        this.description = param.getDescription().trim();
        this.photo = param.getPhoto().trim();
        this.price = param.getPrice();
        this.stock = param.getStock();
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }
}
