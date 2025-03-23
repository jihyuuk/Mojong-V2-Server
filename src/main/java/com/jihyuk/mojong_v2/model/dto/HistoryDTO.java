package com.jihyuk.mojong_v2.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jihyuk.mojong_v2.model.entity.Sale;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class HistoryDTO {

    //나중에 페이징 처리해야함

    /*
    1.제목 (오이 외 n개)
    2.판매번호 (27)
    3.판매자 (홍길동)
    4.판매시간 (2024-04-17 11:18)
    5.판매가격 (2000원)
     */

    private Long id;

    private String title;

    private String username;

    @JsonFormat(pattern = "YYYY.MM.dd HH:mm")
    private LocalDateTime date;

    private int finalAmount;

    public HistoryDTO(Sale sale) {
        this.id = sale.getId();
        this.title = sale.getFirstItemName();
        this.username = sale.getUser().getUsername();
        this.date = sale.getCreatedDateTime();
        this.finalAmount = sale.getFinalAmount();

        if (sale.getItemsCount() > 1){
            title += "외 "+(sale.getItemsCount()-1)+"개";
        }
    }
}
