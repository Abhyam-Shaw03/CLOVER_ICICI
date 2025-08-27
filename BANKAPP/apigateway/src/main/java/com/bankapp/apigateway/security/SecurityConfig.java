package com.bankapp.apigateway.security;

//import jakarta.ws.rs.HttpMethod;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SecurityConfig {

//    @Autowired
//    private JWTAndLoggingFilter jwtAndLoggingFilter;

    // Use a final field for constructor injection
    private final JWTAndLoggingFilter jwtAndLoggingFilter;

    // Use constructor injection
    public SecurityConfig(JWTAndLoggingFilter jwtAndLoggingFilter) {
        this.jwtAndLoggingFilter = jwtAndLoggingFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        // Public endpoints
                        .requestMatchers(HttpMethod.POST, "/user/login").permitAll()

                        .requestMatchers(HttpMethod.POST, "/customer/signup").hasAnyRole("ADMIN", "EMPLOYEE")
                        .requestMatchers(HttpMethod.POST, "/employee/signup").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/admin/signup").hasRole("ADMIN")

                        // All other requests require authentication
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAndLoggingFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
