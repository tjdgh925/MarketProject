package com.project.market.global.config.security.jwt;

import com.project.market.global.error.exception.ErrorCode;
import com.project.market.global.error.exception.TokenException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticateFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;

    private String AUTHORIZATION = "Authorization";
    private static final List<String> EXCLUDE_URL = (List<String>) Collections.unmodifiableList(Arrays.asList("/api/renew", "/api/login", "/api/register"));


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = getJwtToken(request);

        try {
            if (validateToken(token)) {
                Authentication authentication = tokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("exception", ErrorCode.NOT_VALID_TOKEN.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private boolean validateToken(String token) {
        return StringUtils.isNotEmpty(token) && tokenProvider.validateToken(token);
    }

    private String getJwtToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION);

        if (StringUtils.isNotEmpty(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring("Bearer ".length());
        }
        return null;
    }


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return EXCLUDE_URL.contains(path);
    }

}
