package com.michaeltang.OAuth.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.michaeltang.OAuth.services.OAuthClientService;
import com.michaeltang.OAuth.services.OAuthTokenService;
import com.michaeltang.OAuth.validator.RedirectUriValidator;
import com.michaeltang.OAuth.validator.Validator;

@Configuration
@AutoConfigureAfter(OAuthCommonConfigurations.class)
public class OAuthClientConfigurations {
    @Autowired
    private OAuthClientService clientService = null;
    
    @Autowired
    private OAuthTokenService tokenService = null;
    
    @Bean(name = "clientValidator")
    public Validator resourceOwnerValidator() {
        Validator redirectUriValidator = new RedirectUriValidator(null, clientService, tokenService);
        return redirectUriValidator;
    }
}
