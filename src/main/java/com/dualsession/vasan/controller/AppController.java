package com.dualsession.vasan.controller;

import com.dualsession.vasan.config.MagentoStyleSecurityConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class AppController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/admin/login")
    public String adminLogin() { return "admin-login"; }

    @GetMapping("/customer/login")
    public String customerLogin() { return "customer-login"; }

    @PostMapping("/process-login")
    public String processLogin(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("loginType") String loginType,
            HttpServletRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);

            HttpSession session = request.getSession(true);
            if ("admin".equalsIgnoreCase(loginType)) {
                session.setAttribute(MagentoStyleSecurityConfig.ADMIN_KEY, context);
                return "redirect:/admin/dashboard";
            } else {
                session.setAttribute(MagentoStyleSecurityConfig.CUSTOMER_KEY, context);
                return "redirect:/customer/home";
            }
        } catch (Exception e) {
            return "admin".equalsIgnoreCase(loginType) ? "redirect:/admin/login?error" : "redirect:/customer/login?error";
        }
    }

    // NEW MAGENTO-STYLE AREA-SPECIFIC LOGOUT METHOD
    @GetMapping("/process-logout")
    public String processLogout(@RequestParam("type") String type, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            if ("admin".equalsIgnoreCase(type)) {
                // Remove ONLY admin key context
                session.removeAttribute(MagentoStyleSecurityConfig.ADMIN_KEY);
                return "redirect:/admin/login?logout";
            } else {
                // Remove ONLY customer key context
                session.removeAttribute(MagentoStyleSecurityConfig.CUSTOMER_KEY);
                return "redirect:/customer/login?logout";
            }
        }
        return "redirect:/customer/login";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(Principal principal, Model model) {
        if (principal != null) {
            model.addAttribute("username", principal.getName());
            model.addAttribute("area", "Magento-Style Admin Panel");
        }
        return "admin-dashboard";
    }

    @GetMapping("/customer/home")
    public String customerHome(Principal principal, Model model) {
        if (principal != null) {
            model.addAttribute("username", principal.getName());
            model.addAttribute("area", "Customer Frontend Store");
        }
        return "customer-home";
    }
}