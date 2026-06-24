package com.dualsession.vasan.user.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller

public class CustomerController {

    @GetMapping("/customer/login")
    public String customerLogin() {
        return "frontend/customer/login";
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/customer/home")
    public String customerHome(Principal principal, Model model) {
        model.addAttribute("username", principal.getName());
        return "frontend/customer/home";
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/customer/orders")
    public String orderHistory() {
        return "frontend/customer/orders"; // Automatically protected! No config changes needed.
    }
}
