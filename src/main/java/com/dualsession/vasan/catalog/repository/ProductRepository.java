package com.dualsession.vasan.catalog.repository;

import com.dualsession.vasan.catalog.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
