package com.dualsession.vasan.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.SecurityContextRepository;

@Configuration
@EnableWebSecurity
public class MagentoStyleSecurityConfig {

    public static final String ADMIN_KEY = "MAGENTO_ADMIN_CONTEXT";
    public static final String CUSTOMER_KEY = "MAGENTO_CUSTOMER_CONTEXT";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityContext(context -> context.securityContextRepository(magentoSessionRepository()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/login", "/customer/login", "/error", "/process-login", "/process-logout").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/customer/**").hasRole("USER")
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form.disable())
                // Disable native automated global logout completely
                .logout(logout -> logout.disable());

        return http.build();
    }

    @Bean
    public SecurityContextRepository magentoSessionRepository() {
        return new SecurityContextRepository() {
            @Override
            public SecurityContext loadContext(org.springframework.security.web.context.HttpRequestResponseHolder holder) {
                HttpServletRequest request = holder.getRequest();
                HttpSession session = request.getSession(false);
                if (session != null) {
                    String targetKey = request.getRequestURI().contains("/admin") ? ADMIN_KEY : CUSTOMER_KEY;
                    SecurityContext context = (SecurityContext) session.getAttribute(targetKey);
                    if (context != null) return context;
                }
                return SecurityContextHolder.createEmptyContext();
            }

            @Override
            public void saveContext(SecurityContext context, HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response) {
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

    @Bean
    public UserDetailsService userDetailsService() {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        UserDetails admin = User.builder().username("admin").password(encoder.encode("admin123")).roles("ADMIN").build();
        UserDetails customer = User.builder().username("user").password(encoder.encode("user123")).roles("USER").build();
        return new InMemoryUserDetailsManager(admin, customer);
    }
}