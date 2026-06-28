package com.dualsession.vasan.catalog.controller;

import com.dualsession.vasan.catalog.model.Product;
import com.dualsession.vasan.catalog.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin/catalog/product")
public class ProductAdminController {
    private ProductService productService;

    @Autowired
    public ProductAdminController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String getProducts(@RequestParam(defaultValue = "1") int pageNo,
                              @RequestParam(defaultValue = "10") int pageSize,
                              @RequestParam(defaultValue = "id") String sortField,
                              @RequestParam(defaultValue = "asc") String sortDirection,
                              @RequestParam(required = false) String keyword,
                              Model model) {
        Page<Product> page;

        if (keyword != null && !keyword.isEmpty()) {
            page = productService.searchProducts(keyword, pageNo, pageSize, sortField, sortDirection);
            model.addAttribute("keyword", keyword);
        } else {
            page = productService.getProductsPaginated(pageNo, pageSize, sortField, sortDirection);
        }

        List<Product> products = page.getContent();

        model.addAttribute("products", products);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
        return "admin/catalog/products";
    }

    @PostMapping("/add")
    public String addProduct(@RequestBody Product product) {
        Product savedProduct = productService.save(product);
        return "admin/catalog/products";
    }

    @GetMapping("/{id}")
    public String getProduct(@PathVariable Long id, Principal principal, Model model) {
        model.addAttribute("username", null);
        if (principal != null) {
            model.addAttribute("username", principal.getName());
        }
        Product product = this.productService.findById(id);
        model.addAttribute("product", product);

        return "admin/catalog/product-edit";
    }
}
