package com.jihyuk.mojong_v2.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class ItemParam {

    @NotNull(message = "카테고리를 입력해주세요.")
    private Long categoryId; //카테고리 id

    @NotBlank(message = "상품명을 입력해주세요.")
    private String name; //아이템명

    private String description; //설명

    private String photo; //사진

    @NotNull(message = "가격을 입력해주세요")
    @Min(value = 0, message = "가격은 0원 이상이어야 합니다.")
    private Long price; //가격

    @NotNull(message = "수량을 입력해주세요")
    @Min(value = 0, message = "수량은 0개 이상이어야 합니다.")
    private Long stock; //수량
}
