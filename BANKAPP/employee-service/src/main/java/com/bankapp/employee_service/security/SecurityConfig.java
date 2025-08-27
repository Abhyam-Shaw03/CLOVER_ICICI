package com.bankapp.employee_service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
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
                        // Only ADMIN can create or delete employees
                        .requestMatchers(HttpMethod.POST, "/employee/signup").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/employee/delete/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/employee/all").hasRole("ADMIN")

                        // ADMIN and EMPLOYEE can view their profile
                        .requestMatchers(HttpMethod.GET, "/employee/profile/**").hasAnyRole("ADMIN", "EMPLOYEE")

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
}
