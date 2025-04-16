package com.jihyuk.mojong_v2.model.dto;

import com.jihyuk.mojong_v2.model.enums.PAYMENT;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter @Setter
@AllArgsConstructor
public class SaleParam {

    private int totalAmount; //총 금액
    private int discountAmount; //할인 금액
    private int finalAmount; //합계
    @NotNull(message = "지불 방식을 입력해주세요.")
    private PAYMENT payment; //지불 방법

    @NotNull(message = "주문 아이템을 입력해주세요")
    private List<SaleItemDTO> items;

}