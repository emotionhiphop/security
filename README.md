# 의문점들

### /login을 security가 대신 처리해주는데 password를 어떻게 정확하게 인식하는 것인지?
* password라고 동일하게 네이밍되어야 하는지?
* password의 encoding은 ioc에 passwordendcode 타입으로 등록만 하면 그걸 사용하는 것인지?


http://localhost:8080/login/oauth2/code/google

928764277245-tc48j6ptrp9gjqef42srlmelqjiu71j7.apps.googleusercontent.com
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


## Spring Security
1. session안에 Spring Security Session에 존재한다.
2. Spring Security Session에 로그인이 되었다면 Authentication이 존재하고 DI된다.
3. Authentication안에 2가지 타입만 존재한다. (UserDetails = 일반로그인, OAuth2User = OAuth2로그인 )
