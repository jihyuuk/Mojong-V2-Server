package com.jihyuk.mojong_v2.model.entity;

import com.jihyuk.mojong_v2.model.dto.SaleParam;
import com.jihyuk.mojong_v2.model.enums.PAYMENT;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@RequiredArgsConstructor
public class Sale {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sale_id")
    private Long id;

    //주문자는 user 또는 guest
    //fetch lazy 생각해봐야함
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "guest_id")
    private Guest guest;

    private String firstItemName; //처음 상품

    private int itemsCount; //총 상품 수

    private int totalAmount; //총 금액

    private int discountAmount; //할인 금액

    private int finalAmount; //합계

    @Enumerated(EnumType.STRING)
    private PAYMENT payment; //지불방법

    @OneToMany(mappedBy = "sale")
    private List<SaleItem> saleItems = new ArrayList<>(); //주문 아이템들

    private LocalDateTime createdDateTime; //주문 날짜


    @PrePersist
    protected void prePersist(){
        createdDateTime = LocalDateTime.now();
    }

    // Guest 인지 판단
    public boolean isGuest() {
        return guest != null;
    }

    // User 인지 판단
    public boolean isUser() {
        return user != null;
    }

    public Sale(SaleParam dto, Guest guest) {
        this.guest = guest;
        this.firstItemName = dto.getItems().stream().findFirst().get().getName();
        this.itemsCount = dto.getItems().size();
        this.totalAmount = dto.getTotalAmount();
        this.discountAmount = dto.getDiscountAmount();
        this.finalAmount = dto.getFinalAmount();
        this.payment = dto.getPayment();
    }

    public Sale(SaleParam dto, User user, Guest guest) {

        if(user != null){
            this.user = user;
        } else if (guest != null) {
            this.guest = guest;
        }else{
            throw new IllegalArgumentException("주문자 파라미터 오류입니다.");
        }

        this.firstItemName = dto.getItems().stream().findFirst().get().getName();
        this.itemsCount = dto.getItems().size();
        this.totalAmount = dto.getTotalAmount();
        this.discountAmount = dto.getDiscountAmount();
        this.finalAmount = dto.getFinalAmount();
        this.payment = dto.getPayment();

    }

}
