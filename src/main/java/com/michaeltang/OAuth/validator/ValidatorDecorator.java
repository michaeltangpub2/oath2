package com.michaeltang.OAuth.validator;

import java.security.Principal;
import java.util.Map;

import com.michaeltang.OAuth.services.OAuthClientService;
import com.michaeltang.OAuth.services.OAuthTokenService;

public abstract class ValidatorDecorator implements Validator {
    protected OAuthClientService clientService;
    protected OAuthTokenService tokenService;
    private Validator next;
    
    protected ValidatorDecorator(Validator instance, OAuthClientService clientService, OAuthTokenService tokenService) {
        this.next = instance;
        this.clientService = clientService;
        this.tokenService = tokenService;
    }
    
    protected void validateNext(Principal principal, Map<String, String> parameters) throws Exception {
        if (next != null) {
            next.validate(principal, parameters);
        }
    }
    
    public abstract void validate(Principal principal, Map<String, String> parameters) throws Exception;
}
