package com.michaeltang.OAuth.services.impl;

import java.security.Principal;
import java.security.SecureRandom;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.michaeltang.OAuth.model.OAuthAuthenticationState;
import com.michaeltang.OAuth.services.OAuthTokenService;

public class DefaultOAuthTokenService implements OAuthTokenService {
    /**
     * Grant code-->OAuthAuthenticationState mapping
     */
    private Map<String, OAuthAuthenticationState> code2authentications = new ConcurrentHashMap<>();
    private Map<String, OAuthAuthenticationState> token2authentications = new ConcurrentHashMap<>();

    @Override
    public OAuthAuthenticationState loadAuthentication(String accessToken) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public OAuthAuthenticationState loadAccessToken(String accessToken) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String onResourceOwnerApproved(Principal resourceOwner, Map<String, String> parameters) {
        final String code = generate(6);
        final OAuthAuthenticationState auth = OAuthAuthenticationState.from(resourceOwner, code, parameters);
        code2authentications.put(code, auth);
        return code;
    }
    
    @Override
    public OAuthAuthenticationState queryByGrantCode(Principal client, Map<String, String> parameters) {
        final String code = parameters.get("code");
        if (!code2authentications.containsKey(code)) {
            return null;
        }
        final OAuthAuthenticationState authState = code2authentications.remove(code);
        final String token = UUID.randomUUID().toString();
        authState.setAccessToken(token);
        authState.setClient(client);
        token2authentications.put(token, authState);
        return authState;
    }

    @Override
    public OAuthAuthenticationState queryByToken(String token) {
        return token2authentications.getOrDefault(token, null);
    }

    private static final char[] CODE_CHARS = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
            .toCharArray();

    private Random random = new SecureRandom();

    protected String generate(int length) {
        byte[] seed = new byte[length];
        random.nextBytes(seed);
        char[] chars = new char[length];
        for (int i = 0; i < length; i++) {
            chars[i] = CODE_CHARS[((seed[i] & 0xFF) % CODE_CHARS.length)];
        }
        return new String(chars);
    }
}
