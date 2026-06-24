package com.dualsession.vasan.controller;

import com.dualsession.vasan.config.JwtTokenUtil;
import com.dualsession.vasan.config.MagentoStyleSecurityConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Map;

@Controller
public class AppController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private AuthenticationManager authenticationManager;


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
                // Save context exclusively to the Admin session partition
                session.setAttribute(MagentoStyleSecurityConfig.ADMIN_KEY, context);
                return "redirect:/admin/dashboard";
            } else {
                // Save context exclusively to the Customer session partition
                session.setAttribute(MagentoStyleSecurityConfig.CUSTOMER_KEY, context);
                return "redirect:/customer/home";
            }
        } catch (Exception e) {
            return "admin".equalsIgnoreCase(loginType) ? "redirect:/admin/login?error" : "redirect:/customer/login?error";
        }
    }

    @GetMapping("/process-logout")
    public String processLogout(@RequestParam("type") String type, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            if ("admin".equalsIgnoreCase(type)) {
                session.removeAttribute(MagentoStyleSecurityConfig.ADMIN_KEY);
                return "redirect:/admin/login?logout";
            } else {
                session.removeAttribute(MagentoStyleSecurityConfig.CUSTOMER_KEY);
                return "redirect:/customer/login?logout";
            }
        }
        return "redirect:/customer/login";
    }

    // Dedicated Token Generation Endpoint
    @PostMapping("/api/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody Map<String, String> authenticationRequest) {
        try {
            String username = authenticationRequest.get("username");
            String password = authenticationRequest.get("password");

            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            // Fetch the role to verify if the user is an Admin
            String role = auth.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .findFirst().orElse("");

            if (!"ROLE_ADMIN".equals(role)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "Access Denied: Only Admins can access API tokens."));
            }

            // Generate and return the stateless JWT token string
            String token = jwtTokenUtil.generateToken(username, role);
            return ResponseEntity.ok(Map.of("token", token));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid login credentials supplied"));
        }
    }


}