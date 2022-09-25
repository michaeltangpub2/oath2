
OAuth server authorize(Authorization code grant type):
http://localhost:8781/oauth/authorize?response_type=code&client_id=demo_client_id&redirect_uri=http://client.host&scope=openid&state=123

OAuth server authorize(Implicit grant type):
http://localhost:8781/oauth/authorize?response_type=token&client_id=demo_client_id&redirect_uri=http://localhost&scope=openid&state=123


curl -v demo_client_id:demo_client_secret@localhost:8781/oauth/token -d client_id=demo_client_id -d grant_type=authorization_code -d redirect_uri=http://client.host -d code=q3giY7 -d state=123


curl -v "http://localhost:8781/oauth/authorize?response_type=code&client_id=demo_client_id&redirect_uri=http://client.host&scope=openid&state=123"

curl -d "username=user1&password=123456" -H "Cookie: JSESSIONID=DB1B12A9CD3D54D9ECCFDB865BF2A998; Path=/" -H "Referer: http://localhost:8781/login" -H "Content-Type: application/x-www-form-urlencoded" -v -X POST http://127.0.0.1:8781/login

http://localhost:8781/login
curl -d "username=user1&password=123456" -H "Content-Type: application/x-www-form-urlencoded" -v -X POST http://127.0.0.1:8781/login

