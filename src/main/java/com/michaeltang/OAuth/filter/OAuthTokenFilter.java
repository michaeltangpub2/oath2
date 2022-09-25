package com.michaeltang.OAuth.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.michaeltang.OAuth.model.OAuthAuthenticationState;
import com.michaeltang.OAuth.security.OAuthAuthenticationToken;
import com.michaeltang.OAuth.services.OAuthTokenService;

public class OAuthTokenFilter extends OncePerRequestFilter {
    private UserDetailsService userDetailsService;
    private OAuthTokenService tokenService;
    
    public OAuthTokenFilter(UserDetailsService userDetailsService, OAuthTokenService tokenService) {
        this.userDetailsService = userDetailsService;
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            final String token = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (token == null || !token.contains("Bearer ".intern())) {
                filterChain.doFilter(request, response);
                return;
            }
            final OAuthAuthenticationState authState = tokenService.queryByToken(token.substring("Bearer ".intern().length()));
            UserDetails userDetails = userDetailsService.loadUserByUsername(authState.getUsername());
            OAuthAuthenticationToken authentication = new OAuthAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            authentication.setAuthenticated(true);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
        }
        filterChain.doFilter(request, response);
    }

}
