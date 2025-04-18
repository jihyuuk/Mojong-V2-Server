package com.jihyuk.mojong_v2.repository;

import com.jihyuk.mojong_v2.model.entity.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

    Page<Sale> findAllByUserId(Long userId, Pageable pageable);

}
