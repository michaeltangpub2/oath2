package com.michaeltang.OAuth.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.michaeltang.OAuth.services.OAuthClientService;
import com.michaeltang.OAuth.services.OAuthTokenService;
import com.michaeltang.OAuth.validator.ClientIdValidator;
import com.michaeltang.OAuth.validator.ClientIdVerifier;
import com.michaeltang.OAuth.validator.RedirectUriValidator;
import com.michaeltang.OAuth.validator.RedirectUriVerifier;
import com.michaeltang.OAuth.validator.ScopeValidator;
import com.michaeltang.OAuth.validator.Validator;
import com.michaeltang.OAuth.validator.Verifier;

@Configuration
@AutoConfigureAfter(OAuthCommonConfigurations.class)
public class OAuthClientConfigurations {
    @Autowired
    private OAuthClientService clientService = null;
    
    @Autowired
    private OAuthTokenService tokenService = null;
    
    @Bean(name = "clientValidator")
    public Validator clientValidator() {
        Validator redirectUriValidator = new RedirectUriValidator(null, clientService, tokenService);
        return redirectUriValidator;
    }
    
    @Bean(name = "clientVerifier")
    public Verifier clientVerifier() {
        Verifier redirectUriVerifier = new RedirectUriVerifier(null);
        Verifier clientIdVerifier = new ClientIdVerifier(redirectUriVerifier);
        return clientIdVerifier;
    }
}
