# oath2
OAuth2 server demo

Steps to build on local:

# Build

# Run the application
java -jar target/OAuthDemo-0.1.jar --server.port=8781

# Test 
## Initiate the access flow
Request http://localhost:8781/oauth/authorize?response_type=code&client_id=demo_client_id&redirect_uri=http://client.host&scope=openid&state=123 from browser,
## Resource owner login and grant the access
Then you will be redirected to http://localhost:8781/login 
Login with credential user1/123456
Then redirect to http://client.host/?code={grant_code}

## The client request the access token by grant code
Example use curl to request the access token:
curl -v -X POST demo_client_id:demo_client_secret@localhost:8781/oauth/token -d client_id=demo_client_id -d grant_type=authorization_code -d redirect_uri=http://client.host -d code={grant_code} -d state=123

## Obtain user info by access token:
curl -v -X POST -H "Authorization: Bearer {access_token}" http://localhost:8781/user

