package com.dualsession.vasan.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.security.Principal;

@Controller
@PreAuthorize("hasRole('ADMIN')") // <-- Every endpoint in this controller now requires ROLE_ADMIN
public class AdminDashboardController {

    @GetMapping("/admin/dashboard")
    public String dashboard(Principal principal, Model model) {
        model.addAttribute("username", principal.getName());
        return "admin-dashboard";
    }

    @GetMapping("/admin/reports")
    public String salesReports() {
        return "admin-reports"; // Automatically protected! No config changes needed.
    }
}