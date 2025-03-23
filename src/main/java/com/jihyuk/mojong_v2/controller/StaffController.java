package com.jihyuk.mojong_v2.controller;

import com.jihyuk.mojong_v2.model.dto.HistoryDTO;
import com.jihyuk.mojong_v2.model.dto.HistoryDetailDTO;
import com.jihyuk.mojong_v2.model.dto.MenuDTO;
import com.jihyuk.mojong_v2.model.dto.SaleParam;
import com.jihyuk.mojong_v2.service.CategoryService;
import com.jihyuk.mojong_v2.service.SaleService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

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
    public ResponseEntity<String> order(@Valid @RequestBody SaleParam saleParam, Authentication authentication){
        try {
            saleService.sale(saleParam, authentication);
        }catch (EntityNotFoundException e){
            return ResponseEntity.status(BAD_REQUEST).body(e.getMessage());
        }

        return ResponseEntity.ok("주문 완료");
    }

    //팬매 기록들
    @GetMapping("/history")
    public ResponseEntity<?> history(Authentication authentication){
        try {
            List<HistoryDTO> histories = saleService.getHistories(authentication);
            return ResponseEntity.ok(histories);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
        }
    }

    //판매 기록 상세
    @GetMapping ("/history/{id}")
    public ResponseEntity<?> historyDetail(@PathVariable Long id, Authentication authentication){
        try {
            HistoryDetailDTO historyDetailDTO = saleService.getHistoryDetail(id, authentication);
            return ResponseEntity.ok(historyDetailDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
        } catch (AccessDeniedException e){
            return ResponseEntity.status(FORBIDDEN).body(e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
        }
    }

}
