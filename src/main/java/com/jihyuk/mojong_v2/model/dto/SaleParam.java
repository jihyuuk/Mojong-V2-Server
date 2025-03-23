package com.jihyuk.mojong_v2.model.dto;

import com.jihyuk.mojong_v2.model.enums.Payment;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter @Setter
@AllArgsConstructor
public class SaleParam {

    private String firstItemName; //첫 아이템
    private int totalAmount; //총 금액
    private int discountAmount; //할인 금액
    private int finalAmount; //합계
    private Payment payment; //지불 방법

    @NotNull(message = "주문 아이템을 입력해주세요")
    private List<SaleItemDTO> items;

}