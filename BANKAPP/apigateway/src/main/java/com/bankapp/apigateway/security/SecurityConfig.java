package com.bankapp.apigateway.security;

//import jakarta.ws.rs.HttpMethod; // THIS ONE IS NOT TO BE USED FOR HttpMethod

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
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
                        // Public endpoint
                        .requestMatchers(HttpMethod.POST, "/user/login").permitAll()

                        // Role-based access for specific endpoints
                        .requestMatchers(HttpMethod.POST, "/customer/signup").hasAnyRole("ADMIN", "EMPLOYEE")
                        .requestMatchers(HttpMethod.POST, "/employee/signup").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/admin/signup").hasRole("ADMIN")

                        // Fine-grained authorization for employee details
                        .requestMatchers(HttpMethod.GET, "/employee/profile/**").hasAnyRole("ADMIN", "EMPLOYEE")
                        .requestMatchers(HttpMethod.GET, "/employee/all").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/employee/updatePassword/**").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.PUT, "/employee/updateProfile/**").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.DELETE, "/employee/delete/**").hasRole("ADMIN")

                        // Fine-grained authorization for customer details
                        .requestMatchers(HttpMethod.GET, "/customer/profile/**").hasAnyRole("ADMIN", "EMPLOYEE", "CUSTOMER")
                        .requestMatchers(HttpMethod.GET, "/customer/all").hasAnyRole("ADMIN", "EMPLOYEE")
                        .requestMatchers(HttpMethod.PUT, "/customer/updatePassword/**").hasRole("CUSTOMER")
                        .requestMatchers(HttpMethod.PUT, "/customer/updateProfile/**").hasRole("CUSTOMER")
                        .requestMatchers(HttpMethod.DELETE, "/customer/delete/**").hasAnyRole("ADMIN", "EMPLOYEE")

                        // Fine-grained authorization for admin details
                        .requestMatchers(HttpMethod.GET, "/admin/profile/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/admin/all").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/admin/updatePassword/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/admin/updateProfile/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/admin/delete/**").hasRole("ADMIN")

                        // Fine-grained authorization for account-service details
                        .requestMatchers(HttpMethod.GET, "/accounts/accountIdDetails/**").hasAnyRole("ADMIN", "EMPLOYEE")
                        .requestMatchers(HttpMethod.GET, "/accounts/number/**").hasAnyRole("ADMIN", "EMPLOYEE", "CUSTOMER")
                        .requestMatchers(HttpMethod.GET, "/accounts/customer/**").hasAnyRole("ADMIN", "EMPLOYEE", "CUSTOMER")
                        .requestMatchers(HttpMethod.POST, "/accounts/create").hasAnyRole("ADMIN", "EMPLOYEE")


                        // All other requests require a valid token
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAndLoggingFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
