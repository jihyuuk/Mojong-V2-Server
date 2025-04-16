package com.jihyuk.mojong_v2.service;

import com.jihyuk.mojong_v2.model.dto.HistoryDTO;
import com.jihyuk.mojong_v2.model.dto.HistoryDetailDTO;
import com.jihyuk.mojong_v2.model.dto.SaleParam;
import com.jihyuk.mojong_v2.model.dto.SaleItemDTO;
import com.jihyuk.mojong_v2.model.entity.*;
import com.jihyuk.mojong_v2.model.enums.ROLE;
import com.jihyuk.mojong_v2.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
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

    //게스트 주문 서비스
    @Transactional
    public Long staffSale(SaleParam saleParam, Authentication authentication) {

        //로그인 정보 (유저 이름, role)
        String name = authentication.getName();

        //게스트 찾기
        User user = userRepository.findByUsernameAndEnabledTrue(name).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 직원입니다."));

        //주문 생성
        Sale sale = new Sale(saleParam, user);
        //주문 저장
        saleRepository.save(sale);

        //주문 아이템들 생성
        List<SaleItem> saleItems = saleParam.getItems().stream()
                .map(dto -> new SaleItem(dto, sale))
                .toList();
        //주문 아이템들 저장 (나중에 bulk 고려)
        saleItemRepository.saveAll(saleItems);

        return sale.getId();
    }


    //게스트 주문 서비스
    @Transactional
    public Long guestSale(SaleParam saleParam, Authentication authentication) {

        //로그인 정보 (유저 이름, role)
        String name = authentication.getName();

        //게스트 찾기
        Guest guest = guestRepository.findByName(name).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게스트입니다."));

        //주문 생성
        Sale sale = new Sale(saleParam, guest);
        //주문 저장
        saleRepository.save(sale);

        //주문 아이템들 생성
        List<SaleItem> saleItems = saleParam.getItems().stream()
                                                        .map(dto -> new SaleItem(dto, sale))
                                                        .toList();
        //주문 아이템들 저장 (나중에 bulk 고려)
        saleItemRepository.saveAll(saleItems);

        return sale.getId();
    }

    //주문기록들 조회
    @Transactional
    public List<HistoryDTO> getHistories(Authentication authentication){
        //로그인 정보 (유저 이름, role)
        String username = authentication.getName();

        //user 가져오기
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사용자입니다."));

        //해당 user 의 주문 내역들 조회
        return saleRepository.findAllByUserId(user.getId()).stream().map(HistoryDTO::new).collect(Collectors.toList());
    }

    //주문 기록 상세 조회
    @Transactional
    public HistoryDetailDTO getHistoryDetail(Long id, Authentication authentication){
        //로그인 정보 (유저 이름, role)
        String username = authentication.getName();

        //user 가져오기
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사용자입니다."));

        //sale 가져오기
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 판매번호 입니다."));

        //본인의 주문이 아니면 거절(관리자 예외)
        if(user.getRole() != ROLE.ROLE_ADMIN && sale.getUser() != user){
            throw new AccessDeniedException("주문자 본인만 열람 할 수 있습니다.");
        }

        //해당 user 의 주문 내역들 조회
        return new HistoryDetailDTO(sale);
    }
    
}
