package com.dualsession.vasan.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.DeferredSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Configuration
@EnableWebSecurity
public class MagentoStyleSecurityConfig {

    public static final String ADMIN_KEY = "MAGENTO_ADMIN_CONTEXT";
    public static final String CUSTOMER_KEY = "MAGENTO_CUSTOMER_CONTEXT";

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    // CHAIN 1: STATELESS REST API (Uses JWT)
    @Bean
    @Order(1)
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/**")
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/products/login").permitAll()
                        .requestMatchers("/api/products/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // CHAIN 2: STATEFUL WEB UI (Uses Magento Splitter)
    @Bean
    @Order(2)
    public SecurityFilterChain webSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityContext(context -> context.securityContextRepository(magentoSessionRepository()))
                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/admin/login", "/customer/login", "/error", "/process-login", "/process-logout").permitAll()
//                        .requestMatchers("/admin/**").hasRole("ADMIN")
//                        .requestMatchers("/customer/**").hasRole("USER")
//                        .anyRequest().authenticated()
                                .anyRequest().permitAll()
                )
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form.disable())
                .logout(logout -> logout.disable());

        return http.build();
    }

    @Bean
    public SecurityContextRepository magentoSessionRepository() {
        return new SecurityContextRepository() {
            @Override
            public DeferredSecurityContext loadDeferredContext(HttpServletRequest request) {
                return new DeferredSecurityContext() {
                    private SecurityContext context;

                    @Override
                    public SecurityContext get() {
                        if (this.context == null) {
                            this.context = fetchContext(request);
                        }
                        return this.context;
                    }

                    @Override
                    public boolean isGenerated() {
                        HttpSession session = request.getSession(false);
                        if (session == null) return true;
                        String targetKey = request.getRequestURI().contains("/admin") ? ADMIN_KEY : CUSTOMER_KEY;
                        return session.getAttribute(targetKey) == null;
                    }
                };
            }

            @SuppressWarnings("deprecation")
            @Override
            public SecurityContext loadContext(org.springframework.security.web.context.HttpRequestResponseHolder holder) {
                return fetchContext(holder.getRequest());
            }

            private SecurityContext fetchContext(HttpServletRequest request) {
                HttpSession session = request.getSession(false);
                if (session != null) {
                    String targetKey = request.getRequestURI().contains("/admin") ? ADMIN_KEY : CUSTOMER_KEY;
                    SecurityContext context = (SecurityContext) session.getAttribute(targetKey);
                    if (context != null) return context;
                }
                return SecurityContextHolder.createEmptyContext();
            }

            @Override
            public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
                HttpSession session = request.getSession(true);
                String targetKey = request.getRequestURI().contains("/admin") ? ADMIN_KEY : CUSTOMER_KEY;
                session.setAttribute(targetKey, context);
            }

            @Override
            public boolean containsContext(HttpServletRequest request) {
                HttpSession session = request.getSession(false);
                if (session == null) return false;
                String targetKey = request.getRequestURI().contains("/admin") ? ADMIN_KEY : CUSTOMER_KEY;
                return session.getAttribute(targetKey) != null;
            }
        };
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}