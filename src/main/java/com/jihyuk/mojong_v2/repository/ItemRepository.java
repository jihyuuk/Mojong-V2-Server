package com.jihyuk.mojong_v2.repository;

import com.jihyuk.mojong_v2.model.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
