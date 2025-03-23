package com.jihyuk.mojong_v2.model.dto;

import com.jihyuk.mojong_v2.model.entity.SaleItem;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class SaleItemDTO {

    @NotNull(message = "상품 id를 입력해주세요.")
    private Long id; //상품 id
    @NotBlank(message = "상품명을 입력해주세요.")
    private String name;//상품 이름
    private int price; //가격
    private int quantity; //수량
    private int totalAmount; //합계

    public SaleItemDTO(SaleItem saleItem) {
        this.id = saleItem.getId();
        this.name = saleItem.getItem().getName();
        this.price = saleItem.getPrice();
        this.quantity = saleItem.getQuantity();
        this.totalAmount = saleItem.getTotalAmount();
    }
}