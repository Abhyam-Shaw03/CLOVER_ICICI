package com.bankapp.apigateway.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
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

//    @Autowired
//    private JWTService jwtService;
//
//    public JWTAndLoggingFilter() {
//    }

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

        logger.info(">>> JWT Filter is starting for request: {}", httpRequest.getRequestURI());

        String requestId = UUID.randomUUID().toString();
        httpResponse.setHeader("X-Request-ID", requestId);

        long startTime = Instant.now().toEpochMilli();
        logger.info("[{}] Incoming Request - Method: {}, URL: {}", requestId,
                httpRequest.getMethod(), httpRequest.getRequestURI());

        try {
            String authHeader = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                logger.info(">>> JWT Filter found a token. Validating...");

                if (jwtService.validateToken(token)) {
                    String role = jwtService.extractRole(token);
                    logger.info(">>> Extracted role from token: {}", role);
                    logger.info(">>> Token is valid. Populating Security Context.");
                    String userId = jwtService.extractUserId(token);

                    // Create an Authentication object
                    SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);
                    logger.info(">>> FINAL Authority set in context: {}", authority.getAuthority()); // <-- ADD THIS LINE
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

































//package com.bankapp.apigateway.security;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpHeaders;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.time.Instant;
//import java.util.UUID;
//
//@Component
//public class JWTAndLoggingFilter extends OncePerRequestFilter {
//
//    private static final Logger logger = LoggerFactory.getLogger(JWTAndLoggingFilter.class);
//
//    @Autowired
//    private JWTService jwtService;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest httpRequest, HttpServletResponse httpResponse, FilterChain filterChain)
//            throws ServletException, IOException {
//
//        // Generate a unique request ID for tracing across logs
//        String requestId = UUID.randomUUID().toString();
//        httpResponse.setHeader("X-Request-ID", requestId);
//
//        long startTime = Instant.now().toEpochMilli();
//        logger.info("[{}] Incoming Request - Method: {}, URL: {}", requestId,
//                httpRequest.getMethod(), httpRequest.getRequestURI());
//
//        String path = httpRequest.getRequestURI();
//        String method = httpRequest.getMethod();
//
//        try {
//            // Allow unauthenticated access to these endpoints
//            if (isPublicEndpoint(path)) {
//                filterChain.doFilter(httpRequest, httpResponse);
//                return;
//            }
//
//            // Extract and validate JWT
//            String authHeader = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);
//            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//                logger.warn("[{}] Unauthorized access attempt to {}", requestId, path);
//                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                return;
//            }
//
//            String token = authHeader.substring(7);
//            if (!jwtService.validateToken(token)) {
//                logger.warn("[{}] Invalid token for request {}", requestId, path);
//                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                return;
//            }
//
//            // Role-based access control
//            String role = jwtService.extractRole(token);
//            if (isAdminEndpoint(path, role)) {
//                // Admin has full access
//                filterChain.doFilter(httpRequest, httpResponse);
//            } else if (isUserEndpoint(path, method, role)) {
//                // User has restricted access
//                filterChain.doFilter(httpRequest, httpResponse);
//            } else {
//                // Deny access if not a public endpoint and roles don't match
//                logger.warn("[{}] Forbidden access for role '{}' to {}", requestId, role, path);
//                httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
//            }
//
//        } finally {
//            // This logging is now guaranteed to execute after the chain has processed the request
//            logResponse(requestId, httpRequest, httpResponse, startTime);
//        }
//    }
//
//    private boolean isPublicEndpoint(String path) {
//        return path.startsWith("/admin/register") || path.startsWith("/admin/login") ||
//                path.startsWith("/user/register") || path.startsWith("/user/login");
//    }
//
//    private boolean isAdminEndpoint(String path, String role) {
//        return "ADMIN".equals(role);
//    }
//
//    private boolean isUserEndpoint(String path, String method, String role) {
//        if (!"USER".equals(role)) {
//            return false;
//        }
//        return path.startsWith("/user/") ||
//                (path.startsWith("/quiz/getQuestions") && "POST".equalsIgnoreCase(method)) ||
//                (path.startsWith("/quiz/submit") && "POST".equalsIgnoreCase(method));
//    }
//
//    private void logResponse(String requestId, HttpServletRequest request, HttpServletResponse response, long startTime) {
//        long duration = Instant.now().toEpochMilli() - startTime;
//        logger.info("[{}] Response - URL: {} - Status: {} - Processing Time: {}ms",
//                requestId, request.getRequestURI(), response.getStatus(), duration);
//    }
//}