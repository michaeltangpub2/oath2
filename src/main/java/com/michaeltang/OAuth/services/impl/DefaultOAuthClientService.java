package com.michaeltang.OAuth.services.impl;

import com.michaeltang.OAuth.services.OAuthClientService;

public class DefaultOAuthClientService implements OAuthClientService {
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String scope;

    @Override
    public void regieserClient(String clientId, String clientSecret, String redirectUri, String scope) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.scope = scope;
    }

    @Override
    public boolean validate(String clientId, String redirectUri, String scope) {
        return !clientId.isEmpty() && clientId.equals(this.clientId)
                && !redirectUri.isEmpty() && redirectUri.equals(this.redirectUri)
                && !scope.isEmpty() && scope.equals(this.scope);
    }

    @Override
    public boolean authenticate(String clientId, String clientSecret) {
        return !clientId.isEmpty() && clientId.equals(this.clientId) && !clientSecret.isEmpty() && clientSecret.equals(this.clientSecret);
    }

    @Override
    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    @Override
    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

}
