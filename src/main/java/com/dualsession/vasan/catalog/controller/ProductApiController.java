package com.dualsession.vasan.catalog.controller;

import com.dualsession.vasan.catalog.model.Product;
import com.dualsession.vasan.catalog.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalog/product")
public class ProductApiController {
    private ProductService productService;

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/add")
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        Product savedProduct = productService.save(product);
        return ResponseEntity.ok(savedProduct);
    }

    @GetMapping()
    public List<Product> getProducts() {
        return productService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        Product fetchedProduct = productService.findById(id);
        return ResponseEntity.ok(fetchedProduct);
    }

    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        boolean result = productService.delete(id);
        if (result) {
            return "Product deleted";
        }
        return "Product is not available";
    }
}
