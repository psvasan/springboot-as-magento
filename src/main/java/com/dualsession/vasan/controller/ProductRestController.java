package com.dualsession.vasan.controller;

import com.dualsession.vasan.config.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductRestController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    // Dedicated Token Generation Endpoint
    @PostMapping("/login")
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

    // Role-Protected API Resource Data Endpoint
    @GetMapping
    public ResponseEntity<?> getProducts(Principal principal) {
        List<Map<String, Object>> products = List.of(
                Map.of("id", 501, "sku", "JWT-TOK", "name", "Stateless API Package Data", "price", 0.00)
        );
        return ResponseEntity.ok(Map.of(
                "authenticatedApiPrincipal", principal != null ? principal.getName() : "Anonymous",
                "products", products
        ));
    }
}