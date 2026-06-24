package com.dualsession.vasan.catalog.repository;

import com.dualsession.vasan.catalog.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository   extends JpaRepository<Category, Long> {
}
