package com.myproject.journalApp.filter;

import com.myproject.journalApp.utilis.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

@Slf4j
@Component
public class JwtFilter  extends OncePerRequestFilter {
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {


                log.info("Incoming request: {}", request.getRequestURI());

        String authorizationHeader = request.getHeader("Authorization");

        log.debug("Authorization header: {}", authorizationHeader);

        String username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer")) {
            jwt = authorizationHeader.substring(7);

            log.debug("Extracted JWT: {}", jwt);

            try {
                username = jwtUtil.extractUsername(jwt);
                log.info("Extracted username from JWT: {}", username);
            } catch (Exception e) {
                log.error("Failed to extract username from JWT", e);
            }
        } else {
            log.warn("No valid Authorization header found");
        }

        if (username != null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            log.debug("Loaded user details for: {}", username);
            if (jwtUtil.validateToken(jwt)) {

                log.info("JWT is valid for user: {}", username);

               // UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                Collection<? extends GrantedAuthority> authorities =
                        userDetails.getAuthorities() != null ? userDetails.getAuthorities() : Collections.emptyList();

                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(userDetails, null, authorities);


                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
            else {
                log.warn("Invalid JWT for user: {}", username);
            }
        } else {
            log.warn("Username is null — skipping authentication");
        }

        //response.addHeader("admin", "vikas");
        chain.doFilter(request, response);
    }
}


