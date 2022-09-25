package com.michaeltang.OAuth.model;

import java.security.Principal;
import java.util.Map;

public class OAuthAuthenticationState {
    private Principal resourceOwner;
    private Principal client;
    private String clientId;
    private String redirectUri;
    private String code;
    private String accessToken;
    private String username;
    
    public static OAuthAuthenticationState from(Principal resourceOwner, String code, Map<String, String> parameters) {
        OAuthAuthenticationState state = new OAuthAuthenticationState();
        state.code = code;
        state.resourceOwner = resourceOwner;
        state.clientId = parameters.get("client_id");
        state.redirectUri = parameters.get("redirect_uri");
        state.username = resourceOwner.getName();
        return state;
    }
    public Principal getResourceOwner() {
        return resourceOwner;
    }
    public void setResourceOwner(Principal resourceOwner) {
        this.resourceOwner = resourceOwner;
    }
    public Principal getClient() {
        return client;
    }
    public void setClient(Principal client) {
        this.client = client;
    }
    public String getClientId() {
        return clientId;
    }
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
    public String getRedirectUri() {
        return redirectUri;
    }
    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getAccessToken() {
        return accessToken;
    }
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public OAuthAccessToken toOAuthAccessToken() {
        OAuthAccessToken token = new OAuthAccessToken();
        token.setAccessToken(accessToken);
        token.setClientId(clientId);
        token.setTokenType("code"); //hard-coded
        token.setRedirectUri(redirectUri);
        token.setUsername(username);
        return token;
    }
}
