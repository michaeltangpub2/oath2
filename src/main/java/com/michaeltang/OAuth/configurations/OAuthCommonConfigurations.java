package com.michaeltang.OAuth.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.michaeltang.OAuth.services.OAuthClientService;
import com.michaeltang.OAuth.services.OAuthTokenService;
import com.michaeltang.OAuth.services.impl.DefaultOAuthClientService;
import com.michaeltang.OAuth.services.impl.DefaultOAuthTokenService;

@Configuration
public class OAuthCommonConfigurations {
    private OAuthClientService clientService = null;
    private OAuthTokenService tokenService = null;
    
    @Bean
    public OAuthClientService clientService() {
        return getOAuthClientService();
    }
    
    @Bean
    public OAuthTokenService tokenService() {
        return getOAuthTokenService();
    }
    
    private OAuthClientService getOAuthClientService() {
        if (clientService == null) {
            synchronized (this) {
                if (clientService == null) {
                    clientService = new DefaultOAuthClientService();
                }
            }
        }
        return clientService;
    }
    
    private OAuthTokenService getOAuthTokenService() {
        if (tokenService == null) {
            synchronized (this) {
                if (tokenService == null) {
                    tokenService = new DefaultOAuthTokenService();
                }
            }
        }
        return tokenService;
    }
}
