package com.michaeltang.OAuth.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.michaeltang.OAuth.services.OAuthClientService;
import com.michaeltang.OAuth.services.OAuthTokenService;
import com.michaeltang.OAuth.validator.ClientIdValidator;
import com.michaeltang.OAuth.validator.RedirectUriValidator;
import com.michaeltang.OAuth.validator.ScopeValidator;
import com.michaeltang.OAuth.validator.Validator;

/**
 * 
 * @author tangyh
 *
 */
@Configuration
@AutoConfigureAfter(OAuthCommonConfigurations.class)
public class OAuthResourceOwnerConfigurations {
    
    @Autowired
    private OAuthClientService clientService;
    
    @Autowired
    private OAuthTokenService tokenService;
    
    @Bean(name = "resourceOwnerValidator")
    public Validator resourceOwnerValidator() {
        Validator redirectUriValidator = new RedirectUriValidator(null, clientService, tokenService);
        Validator scopeValidator = new ScopeValidator(redirectUriValidator, clientService, tokenService);
        Validator clientIdValidator = new ClientIdValidator(scopeValidator, clientService, tokenService);
        return clientIdValidator;
    }
}
