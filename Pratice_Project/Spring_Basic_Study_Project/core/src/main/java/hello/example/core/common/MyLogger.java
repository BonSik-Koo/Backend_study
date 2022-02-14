package hello.example.core.common;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.UUID;

//로그를 출력하기 위한 클래스
@Component
/*
@Scope(value = "request") 를 사용해서 request 스코프로 지정했다. 이제 이 빈은 HTTP 요청 당 하나씩 생성되고, HTTP 요청이 끝나는 시점에 소멸된다
 -> 즉 스프링 컨테이너가 생성될때 요청하여 등록할수 있는 빈이 아니라 스프링 컨테이너가 생성되 후 http 요청이 있을시 생성할수 있는 빈이다!!!!!!!!!!!!!!!
 + 생성되는 시점이 예를 들어 컨트롤러에서 http를 요청받는 메소드에서 해당 클래스를 사용하려고 불렀는경우에 생성된다 -> "myLogger.setRequestURL(requestURL);"여기서 myLogger의 메소드를 호춣하는순간 생성되어 빈으로 등록됨
 */
//@Scope("request") // = @Scope(value = "request") -> "ObjectProvider" 사용
@Scope(value="request" , proxyMode = ScopedProxyMode.TARGET_CLASS) // -> "Proxy" 사용
public class MyLogger {

    private String uuid;
    private String requestURL;

    public void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
    }

    public void log(String message) {
        System.out.println("[" + uuid + "]" + "[" + requestURL + "] " + message);
    }

    @PostConstruct
    public void init() {
        uuid = UUID.randomUUID().toString();
        System.out.println("[" + uuid + "] request scope bean create " + this);
    }

    @PreDestroy
    public void close() {
        System.out.println("[" + uuid + "] request scope bean close " + this );
    }
}
