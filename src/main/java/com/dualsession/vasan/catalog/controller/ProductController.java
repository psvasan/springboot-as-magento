package com.dualsession.vasan.catalog.controller;

import com.dualsession.vasan.catalog.model.Product;
import com.dualsession.vasan.catalog.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/catalog/product")
public class ProductController {

    private ProductService productService;

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public String getProduct(@PathVariable Long id, Principal principal, Model model) {
        model.addAttribute("username", null);
        if (principal != null) {
            model.addAttribute("username", principal.getName());
        }
        Product product = this.productService.findById(id);
        model.addAttribute("product", product);

        return "frontend/catalog/product/view";
    }
}
