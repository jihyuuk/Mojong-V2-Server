package com.jihyuk.mojong_v2.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class SaleItemDTO{

    @NotNull(message = "상품 id를 입력해주세요.")
    private Long id; //상품 id
    @NotBlank(message = "상품명을 입력해주세요.")
    private String name;//상품 이름
    private int price; //가격
    private int quantity; //수량
    private int totalAmount; //합계

}