package com.jihyuk.mojong_v2.controller;

import com.jihyuk.mojong_v2.model.dto.HistoryDTO;
import com.jihyuk.mojong_v2.model.entity.User;
import com.jihyuk.mojong_v2.service.SaleService;
import com.jihyuk.mojong_v2.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final SaleService saleService;
    private final UserService userService;

    //직원 관리=====================================================================

    //직원 조회
    @GetMapping("/members")
    public List<User> members(){
        return userService.findAllMembers();
    }

    //직원 차단
    @PutMapping("/members/{id}/block")
    public ResponseEntity<String> memberBlock(@PathVariable Long id){
        userService.setUserEnabled(id, false);
        return ResponseEntity.ok("해당 직원을 차단했습니다.");
    }

    //직원 차단 해제
    @PutMapping("/members/{id}/unBlock")
    public ResponseEntity<String> memberUnBlock(@PathVariable Long id){
        userService.setUserEnabled(id, true);
        return ResponseEntity.ok("해당 직원을 활성화했습니다.");
    }

    //가입 승인
    @PutMapping("/members/{id}/approval")
    public ResponseEntity<String> memberApproval(@PathVariable Long id){
        userService.approval(id);
        return ResponseEntity.ok("해당 직원의 가입을 승인하였습니다.");
    }

    //가입 거절
    @PutMapping("/members/{id}/disApproval")
    public ResponseEntity<String> memberDisApproval(@PathVariable Long id){
        userService.disApproval(id);
        return ResponseEntity.ok("해당 직원의 가입을 거부하였습니다.");
    }


    //카테고리 관리=====================================================================
  

    //상품 관리=====================================================================





    //==========================================================================

    //전체 판매 기록 조회
    @GetMapping("/all-history")
    public ResponseEntity<?> history(Authentication authentication){
        try {
            List<HistoryDTO> histories = saleService.getAllHistories(authentication);
            return ResponseEntity.ok(histories);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
        }
    }

}
