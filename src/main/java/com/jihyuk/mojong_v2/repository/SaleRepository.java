package com.jihyuk.mojong_v2.repository;

import com.jihyuk.mojong_v2.model.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

    List<Sale> findAllByUserId(Long userId);

}
