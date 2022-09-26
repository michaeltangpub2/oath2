package com.michaeltang.OAuth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.michaeltang.OAuth.filter.OAuthTokenFilter;
import com.michaeltang.OAuth.services.OAuthTokenService;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

@SpringBootApplication
@EnableWebSecurity
public class AuthorizationServerApplication extends WebMvcConfigurerAdapter {

	public static void main(String[] args) {
		SpringApplication.run(AuthorizationServerApplication.class, args);
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}
	
	@Bean
	public ServletContextInitializer servletContextInitializer() {
	    return new ServletContextInitializer() {
	        @Override
	        public void onStartup(ServletContext servletContext) throws ServletException {
	            servletContext.getSessionCookieConfig().setName("JSESSIONID");
	        }
	    };

	}

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        //registry.addViewController("/oauth/confirm_access").setViewName("authorize");
    }

    @Order(Ordered.LOWEST_PRECEDENCE)
    @Configuration
    protected static class LoginConfiguration extends WebSecurityConfigurerAdapter {
    	
    	@Autowired
        UserDetailsService detailsService;
    	
    	@Autowired
        private OAuthTokenService tokenService;

        @Override
        @Bean
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
        	http
        	//.sessionManagement().enableSessionUrlRewriting(true)
        	//.and()
            .formLogin().loginPage("/login").permitAll()
            .and()
            .requestMatchers().antMatchers("/login", "/user", "/oauth/authorize", "/oauth/confirm_access")
            .and()
            .authorizeRequests().anyRequest().authenticated()
            .and()
            .csrf().disable()
            .requestMatchers().antMatchers("/oauth/token")
            .and()
            .authorizeRequests().anyRequest().authenticated()
            .and()
            .httpBasic();
        	http.addFilterBefore(new OAuthTokenFilter(detailsService, tokenService), UsernamePasswordAuthenticationFilter.class);
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        	auth.userDetailsService(detailsService).passwordEncoder(new BCryptPasswordEncoder());
            /*auth.inMemoryAuthentication()
                    .withUser("user").password("password").roles("USER")
                    .and()
                    .withUser("admin").password("admin").roles("ADMIN");*/
//            auth.parentAuthenticationManager(authenticationManager);
        }
    }
}
