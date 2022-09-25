package com.michaeltang.OAuth.services;

import java.security.Principal;
import java.util.Map;

import com.michaeltang.OAuth.model.OAuthAuthenticationState;

/**
 * OAuth2 token service contract
 * @author tangyh
 *
 */
public interface OAuthTokenService {
    /**
     * Invoked after approved by resource owner
     * @param resourceOwner
     * @param parameters
     * @return Generated grant code
     */
    String onResourceOwnerApproved(Principal resourceOwner, Map<String, String> parameters);

    /**
     * Query by GrantCode from client
     * @param client
     * @param parameters
     * @return
     */
    OAuthAuthenticationState queryByGrantCode(Principal client, Map<String, String> parameters);
    
    /**
     * Query by token from client
     * @param client
     * @param parameters
     * @return
     */
    OAuthAuthenticationState queryByToken(String token);
    
    /**
     * load Authentication
     * @param accessToken
     * @return
     * @throws Exception
     */
    OAuthAuthenticationState loadAuthentication(String accessToken) throws Exception;

    /**
     * 
     * @param accessToken
     * @return
     */
    OAuthAuthenticationState loadAccessToken(String accessToken);
}
