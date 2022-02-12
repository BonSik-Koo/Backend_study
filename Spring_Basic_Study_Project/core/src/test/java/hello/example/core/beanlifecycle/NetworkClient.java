package hello.example.core.beanlifecycle;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class NetworkClient {

    private String url;

    public NetworkClient() {
        System.out.println("생성자 호출 url = " + url);
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void connect() {
        System.out.println("connect: " + url);
    }

    public void call(String message) {
        System.out.println("call: " + url + "message: " + message);
    }

    public void disConnect() {
        System.out.println("close: " + url);
    }

    @PostConstruct //스프링 빈 생성, 의존관계주입이 끝이 나면 호출되는 콜백 애노테이션
    public void init() {
        System.out.println("NetworkClient.init");
        connect();
        call("초기화 연결 메세지");
    }

    @PreDestroy // 스프링 컨테이너 소멸전(ac.close()) 스프링빈을 반환하고 호출되는 콜백 애노테이션
    public void close() {
        System.out.println("NetworkClient.close");
        disConnect();
    }
}
