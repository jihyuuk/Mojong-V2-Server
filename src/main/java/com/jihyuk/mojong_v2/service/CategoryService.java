package com.jihyuk.mojong_v2.service;

import com.jihyuk.mojong_v2.model.dto.ItemParam;
import com.jihyuk.mojong_v2.model.dto.MenuDTO;
import com.jihyuk.mojong_v2.model.entity.Category;
import com.jihyuk.mojong_v2.repository.CategoryRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public List<MenuDTO> getMenu(){
        //모든 카테고리와 아이템 join 으로 가져오기 (조건 enabled = true)
        List<Category> categories = categoryRepository.findEnabledCategoryWithEnabledItems();
        return categories.stream().map(MenuDTO::new).collect(Collectors.toList());
    }

    @Transactional
    public void create(String name){
        Category category = new Category(name);
        Category save = categoryRepository.save(category);
    }


    //dummy data
    private final ItemService itemService;
    @PostConstruct
    public void dummyData(){

        for (int i = 1; i <= 3; i++) {
            create("카테고리 " + i);
        }

        int k = 1;
        for (long i = 1; i <= 3; i++) {
            for (int j = 0; j <= 5; j++) {
                ItemParam param = new ItemParam(
                        i,
                        "아이템 " + (k++),
                        "아이템 설명칸입니다",
                        "http://img.com",
                        1000L,
                        200L
                );
                itemService.create(param);
            }
        }

    }

}
