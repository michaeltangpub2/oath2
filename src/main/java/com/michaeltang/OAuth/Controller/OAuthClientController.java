package com.michaeltang.OAuth.Controller;

import java.security.Principal;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.michaeltang.OAuth.model.OAuthAccessToken;
import com.michaeltang.OAuth.model.OAuthAuthenticationState;
import com.michaeltang.OAuth.services.OAuthClientService;
import com.michaeltang.OAuth.services.OAuthTokenService;
import com.michaeltang.OAuth.validator.Validator;
import com.michaeltang.OAuth.validator.Verifier;

@RestController
public class OAuthClientController {
    
    @Autowired
    @Qualifier("clientValidator")
    private Validator validator;
    
    @Autowired
    private Verifier verifier;
    
    @Autowired
    private OAuthClientService clientService;
    
    @Autowired
    private OAuthTokenService tokenService;
    
    
    @RequestMapping(value = "/oauth/token", method=RequestMethod.POST)
    public ResponseEntity<OAuthAccessToken> postAccessToken(
            HttpServletRequest request,
            Principal principal,
            @RequestParam
            Map<String, String> parameters) throws Exception {
        if (!(principal instanceof Authentication)) {
            //throw new InsufficientAuthenticationException("Invalid client");
        }
        validator.validate(principal, parameters);
        final OAuthAuthenticationState authState = tokenService.queryByGrantCode(principal, parameters);
        verifier.verify(principal, parameters, authState);
        return buildResponse(authState);
    }
    
    private ResponseEntity<OAuthAccessToken> buildResponse(OAuthAuthenticationState authState) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cache-Control", "no-store");
        headers.set("Pragma", "no-cache");
        headers.set("Content-Type", "application/json;charset=UTF-8");
        return new ResponseEntity<OAuthAccessToken>(authState.toOAuthAccessToken(), headers, HttpStatus.OK);
    }

	@RequestMapping(value = "/user",  produces = "application/json")
    public Principal user(Principal user){
        return user;
    }
	
	@RequestMapping(value = "/introspect",  produces = "application/json")
    public String introspect(Principal user){
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String token = (String) authentication.getPrincipal();
	    //OAuth2Authentication auth = tokenServices.loadAuthentication(token);
        return token;
    }
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	@Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        clientService.regieserClient("demo_client_id", "demo_client_secret", "http://client.host", "openid");
        auth.inMemoryAuthentication()
            .withUser("demo_client_id")
            .password(passwordEncoder.encode("demo_client_secret"))
            .authorities("ROLE_USER");
    }
}
