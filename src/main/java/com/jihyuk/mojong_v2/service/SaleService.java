package com.jihyuk.mojong_v2.service;

import com.jihyuk.mojong_v2.model.dto.SaleDTO;
import com.jihyuk.mojong_v2.model.dto.SaleItemDTO;
import com.jihyuk.mojong_v2.model.entity.*;
import com.jihyuk.mojong_v2.model.enums.ROLE;
import com.jihyuk.mojong_v2.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
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

        //로그인 정보 (유저 이름, role)
        String authName = authentication.getName();
        ROLE authRole = getRoleFromAuth(authentication);

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

        //아이템들 조회
        Map<Long, Item> itemMap = findItems(saleDTO);

        //주문 아이템들 생성
        List<SaleItem> saleItems = createSaleItems(saleDTO, sale, itemMap);
        //주문 아이템들 저장 (나중에 bulk 고려)
        saleItemRepository.saveAll(saleItems);
    }




    //아이템들 db에서 조회하기
    private Map<Long, Item> findItems(SaleDTO saleDTO){
        // 아이템 ID 목록 추출(성능 향상을 위한 로직)
        List<Long> itemIds = saleDTO.getItems().stream()
                .map(SaleItemDTO::getId)
                .collect(Collectors.toList());

        // 아이템들을 한 번에 조회
        List<Item> items = itemRepository.findAllById(itemIds);

        // 아이템들을 Map으로 변환 (ID -> Item)
        return items.stream()
                .collect(Collectors.toMap(Item::getId, item -> item));
    }

    //주문 아이템들 생성 로직
    private List<SaleItem> createSaleItems(SaleDTO saleDTO, Sale sale, Map<Long, Item> itemMap) {
        return saleDTO.getItems().stream().map(saleItemDTO -> {
            // 해당 상품 찾기 (Map에서 가져오기)
            Item item = itemMap.get(saleItemDTO.getId());
            if (item == null) {
                throw new EntityNotFoundException("존재하지 않는 상품입니다.");
            }

            //주문 아이템 생성
            return new SaleItem(saleItemDTO, sale, item);
        }).collect(Collectors.toList());
    }

    //로그인 정보에서 role 가져오기
    private ROLE getRoleFromAuth(Authentication authentication) {
        return ROLE.valueOf(
                authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Role not found"))
        );
    }
}
