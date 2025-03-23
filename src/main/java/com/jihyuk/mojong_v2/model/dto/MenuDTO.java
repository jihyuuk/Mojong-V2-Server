package com.jihyuk.mojong_v2.model.dto;

import com.jihyuk.mojong_v2.model.entity.Category;
import com.jihyuk.mojong_v2.model.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class MenuDTO {

    private Long categoryId;
    private String name;
    private List<ItemDTO> items;

    //카테고리 받아서 dto 생성
    public MenuDTO(Category category) {
        this.categoryId = category.getId();
        this.name = category.getName();

        //아이템 리스트 만드는 과정
        this.items = category.getItems().stream().map(ItemDTO::new).collect(Collectors.toList());
    }
}

@Getter
@Setter
@AllArgsConstructor
class ItemDTO {

    private Long id;
    private String name;
    private String description;
    private String photo;
    private int price;
    private int stock;

    public ItemDTO(Item item) {
        this.id = item.getId();
        this.name = item.getName();
        this.description = item.getDescription();
        this.photo = item.getPhoto();
        this.price = item.getPrice();
        this.stock = item.getStock();
    }
}