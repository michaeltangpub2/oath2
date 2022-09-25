package com.michaeltang.OAuth.validator;

import java.security.Principal;
import java.util.Map;

import com.michaeltang.OAuth.services.OAuthClientService;
import com.michaeltang.OAuth.services.OAuthTokenService;

public class RedirectUriValidator extends ValidatorDecorator {

    public RedirectUriValidator(Validator instance, OAuthClientService clientService,
            OAuthTokenService tokenService) {
        super(instance, clientService, tokenService);
    }

    @Override
    public void validate(Principal principal, Map<String, String> parameters) throws Exception {
        if (!clientService.getRedirectUri().equals(parameters.get("redirect_uri"))) {
            throw new Exception("Invalid redirect_uri");
        }
        super.validateNext(principal, parameters);;
    }

}