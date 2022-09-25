package com.michaeltang.OAuth.Controller;

import java.security.Principal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.michaeltang.OAuth.services.OAuthTokenService;
import com.michaeltang.OAuth.validator.Validator;


@RestController
public class OAuthResourceOwnerController {
    @Autowired
    @Qualifier("resourceOwnerValidator")
    private Validator validator;
    
    @Autowired
    private OAuthTokenService tokenService;
    
    @RequestMapping(value = "/oauth/authorize")
    public ModelAndView authorize(Map<String, Object> model, @RequestParam Map<String, String> parameters,
            SessionStatus sessionStatus, Principal principal) throws Exception {
        validator.validate(principal, parameters);
        final String code = tokenService.onResourceOwnerApproved(principal, parameters);
        String redirectUri = parameters.get("redirect_uri");
        if (redirectUri.endsWith("/")) {
            redirectUri = redirectUri.substring(0, redirectUri.length() - 1);
        }
        if (redirectUri.contains("?")) {
            redirectUri = redirectUri + "&code=" + code;
        } else {
            redirectUri = redirectUri + "?code=" + code;
        }
        return new ModelAndView(new RedirectView(redirectUri, false, true, false));
    }
}
