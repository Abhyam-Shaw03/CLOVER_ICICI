package com.bankapp.account_service.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.Collections;
import java.util.UUID;

@Component
public class JWTAndLoggingFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JWTAndLoggingFilter.class);

    private final JWTService jwtService;

    // Use constructor injection
    public JWTAndLoggingFilter(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpRequest, HttpServletResponse httpResponse, FilterChain filterChain)
            throws ServletException, IOException {

        // THIS IS FOR DEBUGGING ONLY - REMOVE LATER
        if (this.jwtService == null) {
            logger.error("FATAL: JWTService is null! Dependency injection failed.");
        }

        String requestId = UUID.randomUUID().toString();
        httpResponse.setHeader("X-Request-ID", requestId);

        long startTime = Instant.now().toEpochMilli();
        logger.info("[{}] Incoming Request - Method: {}, URL: {}", requestId,
                httpRequest.getMethod(), httpRequest.getRequestURI());

        try {
            String authHeader = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);

                if (jwtService.validateToken(token)) {
                    String role = jwtService.extractRole(token);
                    String userId = jwtService.extractUserId(token);

                    // Create an Authentication object
                    SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userId, token, Collections.singleton(authority)); // the token is added here instead of nul as it is needed to pass for internal secure validate communication

                    // Set the Authentication object in the SecurityContext
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    logger.warn("[{}] Invalid token for request {}", requestId, httpRequest.getRequestURI());
                }
            } else {
                // For public endpoints, we simply log and proceed.
                logger.debug("[{}] No token found for request {}", requestId, httpRequest.getRequestURI());
            }

            filterChain.doFilter(httpRequest, httpResponse);

        } finally {
            logResponse(requestId, httpRequest, httpResponse, startTime);
        }
    }

    private void logResponse(String requestId, HttpServletRequest request, HttpServletResponse response, long startTime) {
        long duration = Instant.now().toEpochMilli() - startTime;
        logger.info("[{}] Response - URL: {} - Status: {} - Processing Time: {}ms",
                requestId, request.getRequestURI(), response.getStatus(), duration);
    }
}

