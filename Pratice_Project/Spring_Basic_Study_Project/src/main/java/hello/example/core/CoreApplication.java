package hello.example.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication //스프링 부트어플리케이션 어노테이션
						// -> 실행하게 되면여기에서 @componentscan이 있기 때문에 우리가 따로 Componentscan하지 않아도 component로 선언되있는 것을 빈으로 등록한다.!!!
                       // 자동빈, 수동빈와 관련하여 같은 이름의 빈이 있으면 오류를 발생시킨다 -> 스프링 부트의 기능
public class CoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoreApplication.class, args);
	}

}
