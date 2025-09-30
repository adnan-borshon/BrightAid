package com.example.Bright_Aid.Security;

import org.springframework.security.core.userdetails.UserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

   private final JwtUtil jwtUtil;
   private final UserDetailsService userDetailsService;

   // Paths that should skip JWT authentication
   private static final List<String> PUBLIC_PATHS = Arrays.asList(
           "/api/auth/",
           "/swagger-ui",
           "/v3/api-docs",
           "/swagger-resources",
           "/webjars/",
           "/h2-console",
           "/error"
   );

   @Override
   protected void doFilterInternal(
           @NonNull HttpServletRequest request,
           @NonNull HttpServletResponse response,
           @NonNull FilterChain filterChain) throws ServletException, IOException {

       // Skip JWT validation for public paths
       String requestPath = request.getRequestURI();
       if (isPublicPath(requestPath)) {
           filterChain.doFilter(request, response);
           return;
       }

       final String authHeader = request.getHeader("Authorization");

       // If no auth header or doesn't start with Bearer, continue without authentication
       if (authHeader == null || !authHeader.startsWith("Bearer ")) {
           filterChain.doFilter(request, response);
           return;
       }

       try {
           final String jwt = authHeader.substring(7);
           final String userEmail = jwtUtil.extractUsername(jwt);

           // If we have a username and no authentication set yet
           if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
               UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

               // Validate token
               if (jwtUtil.validateToken(jwt, userDetails)) {
                   UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                           userDetails,
                           null,
                           userDetails.getAuthorities()
                   );
                   authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                   SecurityContextHolder.getContext().setAuthentication(authToken);
                   log.debug("Successfully authenticated user: {}", userEmail);
               }
           }
       } catch (Exception e) {
           log.error("Cannot set user authentication: {}", e.getMessage());
           // Don't throw exception, just continue without authentication
           // This allows the security filter chain to handle the unauthorized access
       }

       filterChain.doFilter(request, response);
   }

   private boolean isPublicPath(String requestPath) {
       return PUBLIC_PATHS.stream().anyMatch(requestPath::startsWith);
   }
}