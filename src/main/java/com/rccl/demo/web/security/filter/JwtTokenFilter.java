package com.rccl.demo.web.security.filter;

import com.rccl.demo.web.security.util.JwtUtil;
import com.rccl.demo.web.service.CredentialIDAuthenticationService;
import com.rccl.demo.web.service.UsernamePasswordAuthenticationService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CredentialIDAuthenticationService fidelioAuthenticationService;
    private final UsernamePasswordAuthenticationService ldapAuthenticationService;

    public JwtTokenFilter(JwtUtil jwtUtil, CredentialIDAuthenticationService fidelioAuthenticationService, UsernamePasswordAuthenticationService ldapAuthenticationService) {
        this.jwtUtil = jwtUtil;
        this.fidelioAuthenticationService = fidelioAuthenticationService;
        this.ldapAuthenticationService = ldapAuthenticationService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        try {
            String headerAuth = httpServletRequest.getHeader("Authorization");

            if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
                String token = headerAuth.substring(7);

                Authentication authentication = this.jwtUtil.parseJwtToken(token);

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
