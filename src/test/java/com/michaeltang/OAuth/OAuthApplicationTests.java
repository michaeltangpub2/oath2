package com.michaeltang.OAuth;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.michaeltang.OAuth.configurations.OAuthClientConfigurations;
import com.michaeltang.OAuth.configurations.OAuthCommonConfigurations;
import com.michaeltang.OAuth.configurations.OAuthResourceOwnerConfigurations;
import com.michaeltang.OAuth.model.OAuthAccessToken;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT,
classes={AuthorizationServerApplication.class, OAuthCommonConfigurations.class, OAuthClientConfigurations.class, OAuthResourceOwnerConfigurations.class})
@TestPropertySource(properties = {
        "redis.port=6379",
        "server.servlet.session.cookie.http-only=false",
        "server.servlet.session.cookie.name=CUSTOMSESSIONID"})
public class OAuthApplicationTests {
    @LocalServerPort
    int randomServerPort;
    
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        final HttpComponentsClientHttpRequestFactory factory = 
                 new HttpComponentsClientHttpRequestFactory();
        CloseableHttpClient build = 
                 HttpClientBuilder.create().disableRedirectHandling().build();
        factory.setHttpClient(build);
        restTemplate.setRequestFactory(factory);
        return restTemplate;
    }

	@AfterClass
	public static void after() {
	}
	
	public static class RestTemplateHeaderModifierInterceptor implements ClientHttpRequestInterceptor {
	    @Override
	    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
	        ClientHttpResponse response = execution.execute(request, body);
	        System.out.println("------------------------------------headers: " + response.getHeaders());
	        return response;
	    }
	}
	
	/**
	 * Test OAuth2.0 Authorization Code mode described in RFC https://datatracker.ietf.org/doc/html/rfc6749#section-4.1
	 * The authorization code grant type is used to obtain both access
       tokens and refresh tokens and is optimized for confidential clients.
       Since this is a redirection-based flow, the client must be capable of
       interacting with the resource owner's user-agent (typically a web
       browser) and capable of receiving incoming requests (via redirection)
       from the authorization server.
    
         +----------+
         | Resource |
         |   Owner  |
         |          |
         +----------+
              ^
              |
             (B)
         +----|-----+          Client Identifier      +---------------+
         |         -+----(A)-- & Redirection URI ---->|               |
         |  User-   |                                 | Authorization |
         |  Agent  -+----(B)-- User authenticates --->|     Server    |
         |          |                                 |               |
         |         -+----(C)-- Authorization Code ---<|               |
         +-|----|---+                                 +---------------+
           |    |                                         ^      v
          (A)  (C)                                        |      |
           |    |                                         |      |
           ^    v                                         |      |
         +---------+                                      |      |
         |         |>---(D)-- Authorization Code ---------'      |
         |  Client |          & Redirection URI                  |
         |         |                                             |
         |         |<---(E)----- Access Token -------------------'
         +---------+       (w/ Optional Refresh Token)
	 * @throws URISyntaxException
	 */
	
	@Test
    public void testOAuth2GrantCodeMode2() throws URISyntaxException {
        // Test 
        RestTemplate restTemplate = new RestTemplate();
        final HttpComponentsClientHttpRequestFactory factory = 
                 new HttpComponentsClientHttpRequestFactory();
        CloseableHttpClient build = 
                 HttpClientBuilder.create().disableRedirectHandling().build();
        factory.setHttpClient(build);
        restTemplate.setRequestFactory(factory);
        //TestRestTemplate restTemplate =
                //new TestRestTemplate(builder, null, null, TestRestTemplate.HttpClientOption.ENABLE_COOKIES);
        /**
         * RFC https://datatracker.ietf.org/doc/html/rfc6749#section-4.1

         (A)  The client initiates the flow by directing the resource owner's
            user-agent to the authorization endpoint.  The client includes
            its client identifier, requested scope, local state, and a
            redirection URI to which the authorization server will send the
            user-agent back once access is granted (or denied).
            
          (B)  The authorization server authenticates the resource owner (via
            the user-agent) and establishes whether the resource owner
            grants or denies the client's access request.
         */
        // Resource owner: GET /oauth/authorize?response_type=code
        String baseUrl = "http://localhost:" + randomServerPort + "/oauth/authorize?response_type=code&client_id=demo_client_id&redirect_uri=http://client.host&scope=openid&state=123";
        URI uri = new URI(baseUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.TEXT_HTML_VALUE);
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<Object>(headers), String.class);
        System.out.println("--------------------------GET " + baseUrl + " response headers: " + response.getHeaders());
        //System.out.println("Response headers: " + response.getHeaders().getValuesAsList("Set-Cookie").get(0));
        Assert.assertEquals(302, response.getStatusCodeValue());
        String location = response.getHeaders().getValuesAsList("Location").get(0);
        Assert.assertEquals("http://localhost:" + randomServerPort + "/login", location);
        String cookie = response.getHeaders().get(HttpHeaders.SET_COOKIE).get(0);
        
        // Resource owner: GET /login
        uri = new URI(location);
        headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, cookie);
        response = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<Object>(headers), String.class);
        System.out.println("--------------------------GET " + location + " response headers: " + response.getHeaders());
        //System.out.println("Response headers: " + response.getHeaders().getValuesAsList("Set-Cookie").get(0));
        Assert.assertEquals(200, response.getStatusCodeValue());
     
        // Resource owner: POST /login
        uri = new URI(location);
        headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, cookie);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> payload = new LinkedMultiValueMap<>();
        payload.add("username", "user1"); 
        payload.add("password", "123456");
        //payload.add("_csrf", "123456");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(payload, headers);
        response = restTemplate.postForEntity(uri, request, String.class);
        System.out.println("--------------------------POST " + location + " response headers: " + response.getHeaders());
        Assert.assertEquals(302, response.getStatusCodeValue());
        location = response.getHeaders().getValuesAsList("Location").get(0);
        cookie = response.getHeaders().get(HttpHeaders.SET_COOKIE).get(0);
        Assert.assertEquals(302, response.getStatusCodeValue());
        location = response.getHeaders().getValuesAsList("Location").get(0);
        Assert.assertEquals("http://localhost:" + randomServerPort
                + "/oauth/authorize?response_type=code&client_id=demo_client_id&redirect_uri=http://client.host&scope=openid&state=123", location);
        cookie = response.getHeaders().get(HttpHeaders.SET_COOKIE).get(0);
        
        /** 
         * (C)  Assuming the resource owner grants access, the authorization
            server redirects the user-agent back to the client using the
            redirection URI provided earlier (in the request or during
            client registration).  The redirection URI includes an
            authorization code and any local state provided by the client
            earlier.
          */
        // Resource owner: GET /oauth/authorize?response_type=code...
        uri = new URI(location);
        headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, cookie);
        response = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<Object>(headers), String.class);
        System.out.println("--------------------------GET " + location + " response headers: " + response.getHeaders());
        Assert.assertEquals(302, response.getStatusCodeValue());
        location = response.getHeaders().getValuesAsList("Location").get(0);
        String grantCode = location.substring(location.indexOf("code=") + 5).split("\\&")[0];
        Assert.assertTrue(location.startsWith("http://client.host?code="));
 
         /**
          * (D)  The client requests an access token from the authorization
            server's token endpoint by including the authorization code
            received in the previous step.  When making the request, the
            client authenticates with the authorization server.  The client
            includes the redirection URI used to obtain the authorization
            code for verification.

            (E)  The authorization server authenticates the client, validates the
            authorization code, and ensures that the redirection URI
            received matches the URI used to redirect the client in
            step (C).  If valid, the authorization server responds back with
            an access token and, optionally, a refresh token.
         */
        // Client: POST /oauth/token 
        baseUrl = "http://localhost:" + randomServerPort + "/oauth/token";
        uri = new URI(baseUrl);
        headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Basic ZGVtb19jbGllbnRfaWQ6ZGVtb19jbGllbnRfc2VjcmV0");
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        payload = new LinkedMultiValueMap<>();
        payload.add("grant_type", "authorization_code"); 
        payload.add("code", grantCode);
        payload.add("redirect_uri", "http://client.host");
        request = new HttpEntity<>(payload, headers);
        ResponseEntity<OAuthAccessToken> accessToken = restTemplate.postForEntity(uri, request, OAuthAccessToken.class);
        System.out.println("--------------------------POST " + baseUrl + " accessToken: " + accessToken.getBody().getAccessToken());
        Assert.assertEquals(200, accessToken.getStatusCodeValue());
        
        // Client: POST /user 
        baseUrl = "http://localhost:" + randomServerPort + "/user";
        uri = new URI(baseUrl);
        headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken.getBody().getAccessToken());
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        payload = new LinkedMultiValueMap<>();
        request = new HttpEntity<>(payload, headers);
        response = restTemplate.postForEntity(uri, request, String.class);
        System.out.println("--------------------------POST " + baseUrl + " response: " + response.getBody());
        System.out.println(response.getBody());
        Assert.assertEquals(200, accessToken.getStatusCodeValue());
    }
}
