package com.bankapp.customer_service.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

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
                        // Only ADMIN and EMPLOYEE can create a customer account
                        .requestMatchers(HttpMethod.POST, "/customer/signup").hasAnyRole("ADMIN", "EMPLOYEE")

                        // Only ADMIN can delete customers
                        .requestMatchers(HttpMethod.DELETE, "/customer/delete/**").hasRole("ADMIN")

                        // ADMIN and CUSTOMER can view their profile
                        .requestMatchers(HttpMethod.GET, "/customer/profile/**").hasAnyRole("ADMIN", "CUSTOMER")

                        // All other requests require authentication
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAndLoggingFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12); // Define PasswordEncoder Bean
    }

    // REQUIRED FOR METHOD LEVEL SECURITY AUTHORIZATION HANDLED DIRECTLTY ABOVE THE CONTROLLER ENDPOINT METHOD
    @Bean
    static MethodSecurityExpressionHandler methodSecurityExpressionHandler() {
        return new DefaultMethodSecurityExpressionHandler();
    }
}