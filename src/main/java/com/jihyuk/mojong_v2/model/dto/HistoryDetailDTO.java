package com.jihyuk.mojong_v2.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jihyuk.mojong_v2.model.entity.Sale;
import com.jihyuk.mojong_v2.model.enums.PAYMENT;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
public class HistoryDetailDTO {

        /*
    1.제목 (오이 외 n개)
    2.판매번호 (27)
    3.판매자 (홍길동)
    4.판매시간 (2024-04-17 11:18)
    5.총 금액
    6.할인금액
    7.합계
    8.지불방법
    9. 상품리스트
     */

    private Long id;

    private String title;

    private String username;

    @JsonFormat(pattern = "YYYY.MM.dd HH:mm")
    private LocalDateTime date;

    private int totalAmount;

    private int discountAmount;

    private int finalAmount;

    private PAYMENT PAYMENT;

    private List<SaleItemDTO> items;

    public HistoryDetailDTO(Sale sale) {
        this.id = sale.getId();
        this.title = sale.getFirstItemName();
        this.username = sale.getUser().getUsername();
        this.date = sale.getCreatedDateTime();
        this.totalAmount = sale.getTotalAmount();
        this.discountAmount = sale.getDiscountAmount();
        this.finalAmount = sale.getFinalAmount();
        this.PAYMENT = sale.getPayment();

        items = sale.getSaleItems().stream().map(SaleItemDTO::new).collect(Collectors.toList());

        if (sale.getItemsCount() > 1){
            title += "외 "+(sale.getItemsCount()-1)+"개";
        }
    }
}
