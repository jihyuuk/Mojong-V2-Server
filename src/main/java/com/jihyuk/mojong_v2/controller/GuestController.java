package com.jihyuk.mojong_v2.controller;

import com.jihyuk.mojong_v2.model.dto.MenuDTO;
import com.jihyuk.mojong_v2.model.dto.SaleParam;
import com.jihyuk.mojong_v2.service.CategoryService;
import com.jihyuk.mojong_v2.service.SaleService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestController
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class GuestController {

    private final CategoryService categoryService;
    private final SaleService saleService;

    //게스트 상품목록
    @GetMapping("/guest-menu")
    public List<MenuDTO> guestMenu(){
        return categoryService.getGuestMenu();
    }

    //게스트 주문
    @PostMapping("/guest-order")
    public ResponseEntity<String> guestOrder(@Valid @RequestBody SaleParam saleParam, Authentication authentication){
        try {
            saleService.guestSale(saleParam, authentication);
        }catch (EntityNotFoundException e){
            return ResponseEntity.status(UNAUTHORIZED).body(e.getMessage());
        }
        return ResponseEntity.ok("주문 완료");
    }
}
