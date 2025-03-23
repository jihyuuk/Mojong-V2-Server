package com.jihyuk.mojong_v2.controller;

import com.jihyuk.mojong_v2.model.dto.MenuDTO;
import com.jihyuk.mojong_v2.model.dto.SaleDTO;
import com.jihyuk.mojong_v2.service.CategoryService;
import com.jihyuk.mojong_v2.service.SaleService;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@RequiredArgsConstructor
public class StaffController {

    private final CategoryService categoryService;
    private final SaleService saleService;

    //메뉴 조회
    @GetMapping("/menu")
    public List<MenuDTO> menu(){
        return categoryService.getMenu();
    }

    //주문하기
    @PostMapping("/order")
    public ResponseEntity<String> order(@Valid @RequestBody SaleDTO saleDTO, Authentication authentication){
        try {
            saleService.sale(saleDTO, authentication);
        }catch (EntityNotFoundException e){
            return ResponseEntity.status(BAD_REQUEST).body(e.getMessage());
        }

        return ResponseEntity.ok("주문 완료");
    }

}
