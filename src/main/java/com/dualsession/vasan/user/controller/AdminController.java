package com.dualsession.vasan.user.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller

public class AdminController {

    @GetMapping("/admin/login")
    public String adminLogin() {
        return "admin/login";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/admin/dashboard")
    public String dashboard(Principal principal, Model model) {
        model.addAttribute("username", principal.getName());
        return "admin/dashboard";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/admin/reports")
    public String salesReports() {
        return "admin/reports"; // Automatically protected! No config changes needed.
    }
}
