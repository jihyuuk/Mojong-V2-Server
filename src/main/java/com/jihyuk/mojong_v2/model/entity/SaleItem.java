package com.jihyuk.mojong_v2.model.entity;

import com.jihyuk.mojong_v2.model.dto.SaleItemDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Entity
@RequiredArgsConstructor
public class SaleItem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sale_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_id")
    private Sale sale;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    private int price; //가격
    private int quantity; //수량
    private int totalAmount; //합계

    public SaleItem(SaleItemDTO dto, Sale sale, Item item) {
        this.sale = sale;
        this.item = item;
        this.price = dto.getPrice();
        this.quantity = dto.getQuantity();
        this.totalAmount = dto.getTotalAmount();
    }
}
