package com.michaeltang.OAuth.validator;

import java.security.Principal;
import java.util.Map;

import com.michaeltang.OAuth.model.OAuthAuthenticationState;

public class ClientIdVerifier extends VerifierDecorator {

    public ClientIdVerifier(Verifier next) {
        super(next);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void verify(Principal client, Map<String, String> parameters, OAuthAuthenticationState authState) throws Exception {
        if (!client.getName().equals(authState.getClientId())) {
            throw new Exception("Invalid client id");
        }
        super.verifyNext(client, parameters, authState);
    }

}
