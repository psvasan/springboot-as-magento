package com.dualsession.vasan.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.security.Principal;

@Controller
@PreAuthorize("hasRole('USER')") // <-- Every endpoint in this controller now requires ROLE_USER
public class CustomerAccountController {

    @GetMapping("/customer/home")
    public String customerHome(Principal principal, Model model) {
        model.addAttribute("username", principal.getName());
        return "customer-home";
    }

    @GetMapping("/customer/orders")
    public String orderHistory() {
        return "customer-orders"; // Automatically protected! No config changes needed.
    }
}