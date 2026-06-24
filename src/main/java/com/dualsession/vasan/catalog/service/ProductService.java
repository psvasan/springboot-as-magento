package com.dualsession.vasan.catalog.service;

import com.dualsession.vasan.catalog.model.Product;
import com.dualsession.vasan.catalog.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    public boolean delete(Long id) {
        try {
            Product product = findById(id);
            productRepository.delete(product);
            return true;
        } catch (RuntimeException exception) {
            return false;
        }
    }

    
}

