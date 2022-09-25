package com.michaeltang.OAuth.validator;

import java.security.Principal;
import java.util.Map;

import com.michaeltang.OAuth.services.OAuthClientService;
import com.michaeltang.OAuth.services.OAuthTokenService;

public class ClientIdValidator extends ValidatorDecorator {

    public ClientIdValidator(Validator instance, OAuthClientService clientService,
            OAuthTokenService tokenService) {
        super(instance, clientService, tokenService);
    }

    @Override
    public void validate(Principal principal, Map<String, String> parameters) throws Exception {
        if (!clientService.getClientId().equals(parameters.get("client_id"))) {
            throw new Exception("Invalid client id");
        }
        super.validateNext(principal, parameters);;
    }

}