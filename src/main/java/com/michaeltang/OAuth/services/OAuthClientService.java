package com.michaeltang.OAuth.services;

public interface OAuthClientService {
    /**
     * regieserClient
     * @param clientId
     * @param clientSecret
     * @param redirectUri
     * @param scope
     */
    void regieserClient(String clientId, String clientSecret, String redirectUri, String scope);
    
    /**
     * authenticate
     * @param clientId
     * @param clientSecret
     * @return
     */
    boolean authenticate(String clientId, String clientSecret);
    
    /**
     * validate
     * @param clientId
     * @param redirectUri
     * @param scope
     * @return
     */
    boolean validate(String clientId, String redirectUri, String scope);
    String getClientId();
    String getRedirectUri();
    String getScope();
}
