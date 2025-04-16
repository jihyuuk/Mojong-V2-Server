package com.jihyuk.mojong_v2.controller;

import com.jihyuk.mojong_v2.model.dto.HistoryDTO;
import com.jihyuk.mojong_v2.model.dto.ItemParam;
import com.jihyuk.mojong_v2.model.entity.User;
import com.jihyuk.mojong_v2.service.CategoryService;
import com.jihyuk.mojong_v2.service.ItemService;
import com.jihyuk.mojong_v2.service.SaleService;
import com.jihyuk.mojong_v2.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final SaleService saleService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final ItemService itemService;

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

    //카테고리 추가
    @PostMapping("/category/new")
    public ResponseEntity<String> newCategory(@RequestBody String name){
        try{
            categoryService.create(name.trim());
            return  ResponseEntity.ok("카테고리 추가 성공!");
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(BAD_REQUEST).body(e.getMessage());
        }catch (DuplicateKeyException e){
            return ResponseEntity.status(CONFLICT).body(e.getMessage());
        }
    }

    //카테고리 삭제
    @DeleteMapping("/category/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id){

        try{
            categoryService.setDisable(id);
            return  ResponseEntity.ok("카테고리 삭제 성공!");
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(BAD_REQUEST).body(e.getMessage());
        }

    }

    //카테고리 수정
    @PutMapping("/category/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable Long id, @RequestBody String newName){
        try{
            categoryService.updateName(id, newName.trim());
            return  ResponseEntity.ok("카테고리 수정 성공!");
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(BAD_REQUEST).body(e.getMessage());
        }catch (DuplicateKeyException e){
            return ResponseEntity.status(CONFLICT).body(e.getMessage());
        }
    }

    //카테고리 순서변경
    @PutMapping("/category/seq")
    public ResponseEntity<String> seqCategory(@RequestBody List<Long> categoryIds){
        categoryService.changeSeq(categoryIds);
        return ResponseEntity.ok("카테고리 순서 변경 성공!");
    }

    
    //상품 관리=====================================================================

    //상품 추가
    @PostMapping("/item/new")
    public ResponseEntity<String> newItem(@Valid @RequestBody ItemParam itemParam){
        try{
            itemService.create(itemParam);
            return  ResponseEntity.ok("아이템 추가 성공!");
        }catch (EntityNotFoundException e){
            return ResponseEntity.status(BAD_REQUEST).body(e.getMessage());
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(BAD_REQUEST).body(e.getMessage());
        }catch (DuplicateKeyException e){
            return ResponseEntity.status(CONFLICT).body(e.getMessage());
        }
    }

    //상품 삭제
    @DeleteMapping("/item/{id}")
    public ResponseEntity<String> deleteItem(@PathVariable Long id){
        itemService.delete(id);
        return  ResponseEntity.ok("카테고리 삭제 성공!");
    }

    //상품 수정
    @PutMapping("/item/{id}")
    public ResponseEntity<String> updateItem(@PathVariable Long id, @Valid @RequestBody ItemParam itemParam){
        try{
            itemService.update(id, itemParam);
            return  ResponseEntity.ok("아이템 수정 성공!");
        }catch (EntityNotFoundException e){
            return ResponseEntity.status(BAD_REQUEST).body(e.getMessage());
        }catch (DuplicateKeyException e){
            return ResponseEntity.status(CONFLICT).body(e.getMessage());
        }
    }


    //상품 순서변경
    @PutMapping("/item/seq")
    public ResponseEntity<String> seqItems(@RequestBody List<Long> itemIds){
        itemService.changeSeq(itemIds);
        return ResponseEntity.ok("아이템 순서 변경 성공!");
    }




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
