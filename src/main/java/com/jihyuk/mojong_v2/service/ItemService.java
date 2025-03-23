package com.jihyuk.mojong_v2.service;

import com.jihyuk.mojong_v2.model.dto.ItemParam;
import com.jihyuk.mojong_v2.model.entity.Category;
import com.jihyuk.mojong_v2.model.entity.Item;
import com.jihyuk.mojong_v2.repository.CategoryRepository;
import com.jihyuk.mojong_v2.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public void create(ItemParam param) {
        //카테고리 없으면 예외 발생
        Category category = categoryRepository.findById(param.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("카테고리를 찾을 수 없습니다. (id=" + param.getCategoryId() + ")"));

        //아이템 생성
        Item item = new Item(category, param);
        itemRepository.save(item);
    }
}
