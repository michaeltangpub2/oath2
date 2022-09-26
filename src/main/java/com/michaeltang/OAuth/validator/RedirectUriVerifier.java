package com.michaeltang.OAuth.validator;

import java.security.Principal;
import java.util.Map;

import com.michaeltang.OAuth.model.OAuthAuthenticationState;

public class RedirectUriVerifier extends VerifierDecorator{

    public RedirectUriVerifier(Verifier next) {
        super(next);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void verify(Principal client, Map<String, String> parameters, OAuthAuthenticationState authState)
            throws Exception {
        if (!parameters.get("redirect_uri").equals(authState.getRedirectUri())) {
            throw new Exception("redirect_uri mismatch");
        }
        super.verifyNext(client, parameters, authState);
    }

}
