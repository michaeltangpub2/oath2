package com.michaeltang.OAuth.validator;

import java.security.Principal;
import java.util.Map;

import com.michaeltang.OAuth.model.OAuthAuthenticationState;

public abstract class VerifierDecorator implements Verifier {
    protected Verifier next = null;
    
    protected VerifierDecorator(Verifier next) {
        this.next = next;
    }

    protected void verifyNext(Principal client, Map<String, String> parameters, OAuthAuthenticationState authState) throws Exception {
        if (next != null) {
            next.verify(client, parameters, authState);
        }
    }
    
    @Override
    public abstract void verify(Principal client, Map<String, String> parameters, OAuthAuthenticationState authState) throws Exception;
}
