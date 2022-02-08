package hello.hellospring; //참고) 스프링 컨테이너에 등록하는 컨포넌트들은 아무런 파일에 있는 컴포넌트 스캔을 등록하는 것이 아니라 해당 어플리케이션의 "hello.hellospring"의 하위폴더로 가면서 실행된다.

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HelloSpringApplication {
	public static void main(String[] args) {

		SpringApplication.run(HelloSpringApplication.class, args);
	}

}
