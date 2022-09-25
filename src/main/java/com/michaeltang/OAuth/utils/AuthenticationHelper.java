package com.michaeltang.OAuth.utils;

import org.springframework.security.core.context.SecurityContextHolder;

public class AuthenticationHelper {
    
    public static void clearContext() {
        if (isAuthenticated()) {
            SecurityContextHolder.clearContext();
        }
    }
    
    private static boolean isAuthenticated() {
        return SecurityContextHolder.getContext().getAuthentication() == null;
    }
}
