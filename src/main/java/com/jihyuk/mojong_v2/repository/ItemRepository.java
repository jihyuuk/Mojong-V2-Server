package com.jihyuk.mojong_v2.repository;

import com.jihyuk.mojong_v2.model.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    
    //생성시
    boolean existsByName(String name);

    //업데이트시
    boolean existsByNameAndIdNot(String name, Long id);

}
