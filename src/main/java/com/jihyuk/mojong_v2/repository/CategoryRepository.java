package com.jihyuk.mojong_v2.repository;

import com.jihyuk.mojong_v2.model.entity.Category;
import org.hibernate.annotations.processing.Find;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsByName(String name);

    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.items i WHERE c.enabled = true")
    List<Category> findEnabledCategory();

    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.items i WHERE c.enabled = true AND i.isPublic = true")
    List<Category> findPublicItems();

}
