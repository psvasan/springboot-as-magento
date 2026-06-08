package com.dualsession.vasan.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StorefrontCatalogController {

    // Completely public by default!
    @GetMapping("/")
    public String homePage() { return "storefront-home"; }

    @GetMapping("/product/{id}")
    public String productDetailPage() { return "storefront-pdp"; }

    @GetMapping("/cart")
    public String cartPage() { return "storefront-cart"; }

    @GetMapping("/new-cms-page-test")
    public String futurePage() { return "cms-template"; } // Instantly accessible!
}