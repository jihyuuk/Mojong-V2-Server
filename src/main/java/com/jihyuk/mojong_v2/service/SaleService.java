package com.jihyuk.mojong_v2.service;

import com.jihyuk.mojong_v2.model.dto.SaleDTO;
import com.jihyuk.mojong_v2.model.entity.*;
import com.jihyuk.mojong_v2.model.enums.ROLE;
import com.jihyuk.mojong_v2.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SaleService {

    private final SaleRepository saleRepository;
    private final SaleItemRepository saleItemRepository;
    private final UserRepository userRepository;
    private final GuestRepository guestRepository;
    private final ItemRepository itemRepository;


    @Transactional
    public void sale(SaleDTO saleDTO, Authentication authentication) {

        //로그인 정보
        String authName = authentication.getName();
        ROLE authRole = ROLE.valueOf(
                authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Role not found"))
        );

        User user = null;
        Guest guest = null;

        //주문자 찾기
        if (authRole == ROLE.ROLE_GUEST) {
            guest = guestRepository.findByGuestname(authName)
                    .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게스트입니다."));
        }
        if (authRole == ROLE.ROLE_STAFF || authRole == ROLE.ROLE_ADMIN) {
            user = userRepository.findByUsername(authName)
                    .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 유저입니다."));
        }

        //주문 저장
        Sale sale = new Sale(saleDTO, user, guest);
        saleRepository.save(sale);

        //주문 아이템들
        List<SaleItem> saleItems = saleDTO.getItems().stream().map(saleItemDTO -> {
            //해당 상품 찾기
            Item item = itemRepository.findById(saleItemDTO.getId())
                    .orElseThrow(() -> new EntityNotFoundException("존재하지 상품입니다."));
            //주문 아이템 생성
            return new SaleItem(saleItemDTO, sale, item);
        }).collect(Collectors.toList());

        // 한 번에 저장 (배치 처리)
        saleItemRepository.saveAll(saleItems);
    }
}
