package com.jihyuk.mojong_v2.controller;

import com.jihyuk.mojong_v2.model.dto.MenuDTO;
import com.jihyuk.mojong_v2.service.CategoryService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StaffController {

    private final CategoryService categoryService;

    @GetMapping("/menu")
    public List<MenuDTO> menu(){
        return categoryService.getMenu();
    }

    @PostMapping("/order")
    public String order(){
        return "/order 접근 완료";
    }

}
