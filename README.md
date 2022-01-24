Servlet Container 에서의 Http Digest 인증 구현 예제입니다. Servlet 으로 요청에 대한 처리가 구현되어 있습니다.

Http Digest 인증에 대한 설명은 Blog 에 정리해두었습니다.

*[dpTablo Blog - Http Digest](https://dptablo.github.io/servlet%20container/authorization/http/Http-Digest-implementation/)*

# 요청 URL

### /digest
직접 구현한 Http Digest 인증 절차를 수행하는 servlet 입니다.

인증에 성공하지 않은 경우 Http status 401 response 되며, 인증에 성공한 경우에는 "인증 성공" 내용의 html 이 response 됩니다.

### /tomcat/test1
tomcat 에서의 인증 테스트를 위한 servlet 입니다.
해당 URL 에 대해 role 권한 설정이 `web.xml` 에 적용되어 있습니다.

```xml
<login-config>
    <auth-method>DIGEST</auth-method>
    <realm-name>mRealm1</realm-name>
</login-config>

<security-role>
    <role-name>administrators</role-name>
    <role-name>users1</role-name>
    <role-name>users2</role-name>
</security-role>

<security-constraint>
    <web-resource-collection>
        <web-resource-name>Memory Realm Sample</web-resource-name>
        <url-pattern>/tomcat/test1</url-pattern>
    </web-resource-collection>
    <auth-constraint>
        <role-name>administrators</role-name>
        <role-name>users1</role-name>
        <role-name>users2</role-name>
    </auth-constraint>
</security-constraint>
```
tomcat 에서의 realm, role 설정은 별도로 하셔야 합니다.