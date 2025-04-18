package com.jihyuk.mojong_v2.service;

import com.jihyuk.mojong_v2.model.dto.ItemParam;
import com.jihyuk.mojong_v2.model.dto.MenuDTO;
import com.jihyuk.mojong_v2.model.entity.Category;
import com.jihyuk.mojong_v2.repository.CategoryRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final SettingService settingService;

    @Transactional
    public List<MenuDTO> getStaffMenu(){
        //모든 카테고리와 아이템 join 으로 가져오기
        List<Category> categories = categoryRepository.findEnabledCategory();
        return categories.stream().map(MenuDTO::new).collect(Collectors.toList());
    }

    @Transactional
    public List<MenuDTO> getGuestMenu() {
        //모든 카테고리와 아이템 join 으로 가져오기 (조건 isPublic = true)
        List<Category> categories = categoryRepository.findPublicItems();
        return categories.stream().map(MenuDTO::new).collect(Collectors.toList());
    }

    @Transactional
    public void create(String name){

        if(name == null || name.isEmpty()){
            throw new IllegalArgumentException("카테고리명을 정확히 입력해주세요.");
        }

        if(categoryRepository.existsByNameAndEnabledTrue(name)){
            throw new DuplicateKeyException("이미 존재하는 카테고리 입니다.");
        }

        Category category = new Category(name);
        categoryRepository.save(category);
    }

    @Transactional
    public void setDisable(Long id){
        Category category = categoryRepository.findById(id).orElseThrow(()->new IllegalArgumentException("존재하지 않는 카테고리입니다."));
        category.setSeq(-1);
        category.setEnabled(false);
    }

    @Transactional
    public void updateName(Long id, String name){

        if(name == null || name.isEmpty()){
            throw new IllegalArgumentException("카테고리명을 정확히 입력해주세요.");
        }

        if(categoryRepository.existsByNameAndEnabledTrue(name)){
            throw new DuplicateKeyException("이미 존재하는 카테고리 입니다.");
        }

        Category category = categoryRepository.findById(id).orElseThrow(()->new IllegalArgumentException("카테고리 ID를 정확히 입력해주세요."));
        category.setName(name);
    }

    @Transactional
    public void changeSeq(List<Long> categoryIds){
        for (int i = 0 ; i < categoryIds.size(); i++) {
            Long id = categoryIds.get(i);
            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("카테고리를 찾을 수 없습니다. ID: " + id));
            category.setSeq(categoryIds.size()-i);
        }
    }





    //dummy data
    private final ItemService itemService;
    //@PostConstruct
    public void dummyData(){

        List<String> categories = Arrays.asList("피자", "사이드", "음료", "디저트", "파스타", "소스", "핫도그", "햄버거", "치킨", "사진 없는 것");

        for (String category : categories) {
            create(category);
        }

        List<ItemParam> items = Arrays.asList(
                new ItemParam(1L, "어깨 피자", "이 글을 본다면 당장 어깨를 펴보세요.", "https://picsum.photos/id/44/100/100", 200, 300),
                new ItemParam(1L, "페퍼로니 피자", "매콤한 페퍼로니와 치즈가 듬뿍 들어간 피자.", "https://picsum.photos/id/1/100/100", 18000, 50000),
                new ItemParam(1L, "불고기 피자", "고소한 불고기와 신선한 야채가 어우러진 피자.", "https://picsum.photos/id/2/100/100", 22000, 0),
                new ItemParam(1L, "콤비네이션 피자", "여러 가지 토핑이 어우러진 퓨전 피자.", "https://picsum.photos/id/3/100/100", 25000, 40000),
                new ItemParam(1L, "치즈 피자", "고소한 치즈가 듬뿍 들어간 심플 피자.", "https://picsum.photos/id/4/100/100", 17000, 60000),
                new ItemParam(1L, "슈프림 피자", "다양한 토핑이 어우러진 프리미엄 피자.", "https://picsum.photos/id/5/100/100", 27000, 20000),
                new ItemParam(2L, "감자튀김", "바삭하게 튀긴 감자튀김.", "https://picsum.photos/id/6/100/100", 7000, 100000),
                new ItemParam(2L, "치킨윙", "매콤하고 바삭한 치킨윙.", "https://picsum.photos/id/7/100/100", 12000, 80000),
                new ItemParam(2L, "갈릭 브레드", "고소한 마늘 버터로 구운 브레드.", "https://picsum.photos/id/8/100/100", 5000, 120000),
                new ItemParam(2L, "샐러드", "신선한 야채로 만든 건강한 샐러드.", "https://picsum.photos/id/9/100/100", 6000, 70000),
                new ItemParam(2L, "모짜렐라 스틱", "바삭하게 튀긴 모짜렐라 치즈 스틱.", "https://picsum.photos/id/10/100/100", 8000, 90000),
                new ItemParam(3L, "콜라", "시원하고 청량한 콜라.", "https://picsum.photos/id/11/100/100", 3000, 200000),
                new ItemParam(3L, "아이스티", "달콤하고 상큼한 아이스티.", "https://picsum.photos/id/12/100/100", 3500, 150000),
                new ItemParam(3L, "레모네이드", "신선한 레몬으로 만든 레모네이드.", "https://picsum.photos/id/13/100/100", 4000, 100000),
                new ItemParam(3L, "사이다", "깔끔하고 상쾌한 사이다.", "https://picsum.photos/id/14/100/100", 3000, 180000),
                new ItemParam(3L, "맥주", "시원하게 즐기는 맥주.", "https://picsum.photos/id/15/100/100", 6000, 120000),
                new ItemParam(4L, "초코 브라우니", "진한 초콜릿이 가득한 브라우니.", "https://picsum.photos/id/16/100/100", 5000, 100000),
                new ItemParam(4L, "치즈 케이크", "부드럽고 진한 치즈 케이크.", "https://picsum.photos/id/17/100/100", 7000, 80000),
                new ItemParam(4L, "티라미수", "커피 향이 가득한 이탈리안 디저트.", "https://picsum.photos/id/18/100/100", 7500, 60000),
                new ItemParam(4L, "아이스크림", "시원하고 달콤한 아이스크림.", "https://picsum.photos/id/19/100/100", 4000, 150000),
                new ItemParam(4L, "마카롱", "다양한 맛의 달콤한 마카롱.", "https://picsum.photos/id/20/100/100", 9000, 100000),
                new ItemParam(5L, "까르보나라", "크리미한 소스가 어우러진 까르보나라 파스타.", "https://picsum.photos/id/21/100/100", 12000, 100000),
                new ItemParam(5L, "알리오 올리오", "담백하고 깔끔한 올리브오일 파스타.", "https://picsum.photos/id/22/100/100", 11000, 80000),
                new ItemParam(5L, "볼로네제", "진한 미트소스가 들어간 볼로네제 파스타.", "https://picsum.photos/id/23/100/100", 13000, 60000),
                new ItemParam(5L, "새우 로제 파스타", "매콤한 로제소스와 새우가 들어간 파스타.", "https://picsum.photos/id/24/100/100", 14000, 70000),
                new ItemParam(5L, "해산물 파스타", "신선한 해산물이 가득한 토마토소스 파스타.", "https://picsum.photos/id/25/100/100", 15000, 50000),
                new ItemParam(6L, "갈릭 디핑 소스", "고소한 마늘 향이 풍부한 갈릭 디핑 소스.", "https://picsum.photos/id/26/100/100", 1000, 200000),
                new ItemParam(6L, "스위트 칠리 소스", "달콤하고 매콤한 스위트 칠리 소스.", "https://picsum.photos/id/27/100/100", 1500, 150000),
                new ItemParam(6L, "랜치 소스", "부드럽고 크리미한 랜치 소스.", "https://picsum.photos/id/28/100/100", 1200, 18000),
                new ItemParam(6L, "허니 머스타드", "달콤하고 부드러운 허니 머스타드 소스.", "https://picsum.photos/id/29/100/100", 1300, 1200000000),
                new ItemParam(6L, "바베큐 소스", "진한 훈연향이 가득한 바베큐 소스.", "https://picsum.photos/id/30/100/100", 1400, 100000),
                new ItemParam(7L, "클래식 핫도그", "부드러운 번과 소시지가 조화로운 핫도그.", "https://picsum.photos/id/31/100/100", 5000, 100000),
                new ItemParam(7L, "치즈 핫도그", "체다 치즈가 듬뿍 들어간 핫도그.", "https://picsum.photos/id/32/100/100", 6000, 80000),
                new ItemParam(7L, "칠리 핫도그", "매콤한 칠리 소스를 곁들인 핫도그.", "https://picsum.photos/id/33/100/100", 6500, 70000),
                new ItemParam(7L, "베이컨 랩 핫도그", "베이컨을 감싼 풍미 가득한 핫도그.", "https://picsum.photos/id/34/100/100", 7000, 60000),
                new ItemParam(7L, "콘도그", "달콤한 핫도그를 튀긴 콘도그.", "https://picsum.photos/id/35/100/100", 5500, 90000),
                new ItemParam(8L, "불고기 버거", "달콤한 불고기 소스로 맛을 낸 클래식 불고기 버거.", "https://picsum.photos/id/36/100/100", 5500, 100000),
                new ItemParam(8L, "치즈 버거", "고소한 치즈가 들어간 풍미 가득한 치즈 버거.", "https://picsum.photos/id/37/100/100", 6000, 80000),
                new ItemParam(9L, "후라이드 치킨", "바삭하고 촉촉한 전통 후라이드 치킨.", "https://picsum.photos/id/38/100/100", 17000, 50000),
                new ItemParam(9L, "양념 치킨", "달콤하고 매콤한 특제 소스로 버무린 양념 치킨.", "https://picsum.photos/id/39/100/100", 18000, 50000),
                new ItemParam(10L, "노각오이", "", "https://picsum.photos/id/40/100/100", 17000, 50000),
                new ItemParam(10L, "노각오이2", "바삭하고 촉촉한 전통 후라이드 치킨.", "", 17000, 50000),
                new ItemParam(10L, "오이", "달콤하고 매콤한 특제 소스로 버무린 양념 치킨.", "", 18000, 50000),
                new ItemParam(10L, "가지", "", "", 18000, 5)
        );


        for (ItemParam item : items) {
            itemService.create(item);
        }

        settingService.setQrOrderEnabled(true);


    }


}
