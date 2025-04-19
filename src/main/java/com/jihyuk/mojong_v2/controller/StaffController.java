package com.jihyuk.mojong_v2.controller;

import com.jihyuk.mojong_v2.model.dto.HistoryDTO;
import com.jihyuk.mojong_v2.model.dto.HistoryDetailDTO;
import com.jihyuk.mojong_v2.model.dto.MenuDTO;
import com.jihyuk.mojong_v2.model.dto.SaleParam;
import com.jihyuk.mojong_v2.service.CategoryService;
import com.jihyuk.mojong_v2.service.PrinterService;
import com.jihyuk.mojong_v2.service.SaleService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
public class StaffController {

    private final CategoryService categoryService;
    private final SaleService saleService;
    private final PrinterService printerService;

    //메뉴 조회
    @GetMapping("/menu")
    public ResponseEntity<?> menu(Authentication authentication) {
        try {
            List<MenuDTO> menu = categoryService.getStaffMenu(authentication);
            return ResponseEntity.ok(menu);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(UNAUTHORIZED).body(e.getMessage());
        }
    }

    //주문하기
    @PostMapping("/order")
    public ResponseEntity<Map<String, Object>> order(@Valid @RequestBody SaleParam saleParam, Authentication authentication) {
        try {
            long saleId = saleService.staffSale(saleParam, authentication);
            boolean printOK = true;

            //영수증 출력
            if (!saleParam.isSkipReceipt()) {
                //판매 기록 상세 재활용
                HistoryDetailDTO historyDetailDTO = saleService.getHistoryDetail(saleId, authentication);
                printOK = printerService.printReceipt(historyDetailDTO);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("message", "주문 완료");
            response.put("saleId", saleId);
            response.put("printOK", printOK);

            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(UNAUTHORIZED)
                    .body(Map.of("error", e.getMessage()));
        }
    }


    //팬매 기록들
    @GetMapping("/history")
    public ResponseEntity<?> history(
            Authentication authentication,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        try {
            Page<HistoryDTO> histories = saleService.getHistories(authentication, pageable);
            return ResponseEntity.ok(histories);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
        }
    }

    //판매 기록 상세
    @GetMapping("/history/{id}")
    public ResponseEntity<?> historyDetail(@PathVariable Long id, Authentication authentication) {
        try {
            HistoryDetailDTO historyDetailDTO = saleService.getHistoryDetail(id, authentication);
            return ResponseEntity.ok(historyDetailDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
        }
    }

    //영수증 프린트하기
    @PostMapping("/print/{id}")
    public ResponseEntity<?> printReceipt(@PathVariable Long id, Authentication authentication) {
        try {
            //위의 판매 기록 상세 재활용
            HistoryDetailDTO historyDetailDTO = saleService.getHistoryDetail(id, authentication);
            boolean printOK = printerService.printReceipt(historyDetailDTO);

            Map<String, Object> response = new HashMap<>();
            response.put("printOK", printOK);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
        }
    }

}
