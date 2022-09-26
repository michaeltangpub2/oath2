package com.michaeltang.OAuth.security;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@Component
public class OAuthUserDetailsService implements UserDetailsService {
	private static final Log logger = LogFactory.getLog(OAuthUserDetailsService.class);
	private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public UserDetails loadUserByUsername(String username) {
        if (username.isEmpty()) {
            throw new UsernameNotFoundException("Empty user");
        }
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("USER"));
        logger.debug("loadUserByUsername, username:" + username);

        if (username.equals("demo_client_id")){
            return new org.springframework.security.core.userdetails.User(
                    username, encoder.encode("demo_client_secret"),
                    true,//enabled
                    true,//accountNonExpired
                    true,//credentialsNonExpired
                    true,//accountNonLocked
                    authorities);
        } else if (username.equals("user1")){
	        return new org.springframework.security.core.userdetails.User(
	                username, encoder.encode("123456"),
	                true,//enabled
	                true,//accountNonExpired
	                true,//credentialsNonExpired
	                true,//accountNonLocked
	                authorities);
        } else {
        	return new org.springframework.security.core.userdetails.User(
                username, encoder.encode("654321"),
                true,//enabled
                true,//accountNonExpired
                true,//credentialsNonExpired
                true,//accountNonLocked
                authorities);
        }
    }
}