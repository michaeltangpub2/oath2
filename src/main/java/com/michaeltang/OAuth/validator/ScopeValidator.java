package com.michaeltang.OAuth.validator;

import java.security.Principal;
import java.util.Map;

import com.michaeltang.OAuth.services.OAuthClientService;
import com.michaeltang.OAuth.services.OAuthTokenService;

public class ScopeValidator extends ValidatorDecorator {

    public ScopeValidator(Validator instance, OAuthClientService clientService,
            OAuthTokenService tokenService) {
        super(instance, clientService, tokenService);
    }

    @Override
    public void validate(Principal principal, Map<String, String> parameters) throws Exception {
        if (!clientService.getScope().equals(parameters.get("scope"))) {
            throw new Exception("Invalid scope");
        }
        super.validateNext(principal, parameters);;
    }

}
