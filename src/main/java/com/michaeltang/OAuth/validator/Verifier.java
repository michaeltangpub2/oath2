package com.michaeltang.OAuth.validator;

import java.security.Principal;
import java.util.Map;

import com.michaeltang.OAuth.model.OAuthAuthenticationState;

/**
 * Verify the access token request made by client
 * @author tangyh
 *
 */
public interface Verifier {
    /**
     * Verify request
     * @param principal
     * @param parameters
     * @param authState
     * @throws Exception
     */
    void verify(Principal client, Map<String, String> parameters, OAuthAuthenticationState authState) throws Exception;
}
