package com.jihyuk.mojong_v2.service;

import com.jihyuk.mojong_v2.model.dto.ItemParam;
import com.jihyuk.mojong_v2.model.entity.Category;
import com.jihyuk.mojong_v2.model.entity.Item;
import com.jihyuk.mojong_v2.repository.CategoryRepository;
import com.jihyuk.mojong_v2.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public void create(ItemParam param) {

        //이름 중복이면 예외 발생
        if(itemRepository.existsByName(param.getName().trim())){
            throw new DuplicateKeyException("중복된 상품명 입니다.");
        }

        //카테고리 검증
        if(param.getCategoryId() == null){
            throw new IllegalArgumentException("카테고리를 입력해주세요.");
        }

        //카테고리 없으면 예외 발생
        Category category = categoryRepository.findById(param.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("카테고리를 찾을 수 없습니다. (id=" + param.getCategoryId() + ")"));

        //아이템 생성
        Item item = new Item(category, param);
        itemRepository.save(item);
    }

    @Transactional
    public void delete(Long id){
        itemRepository.deleteById(id);
    }

    @Transactional
    public void update(Long id, ItemParam param){

        Item item = itemRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다. ID: " + id));

        //이름 중복이면 예외 발생
        if(itemRepository.existsByName(param.getName().trim())){
            throw new DuplicateKeyException("중복된 상품명 입니다.");
        }

        item.update(param);
    }

    @Transactional
    public void changeSeq(List<Long> itemIds){
        for (int i = 0 ; i < itemIds.size(); i++) {
            Long id = itemIds.get(i);
            Item item = itemRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다. ID: " + id));
            item.setSeq(itemIds.size()-i);
        }
    }
}
