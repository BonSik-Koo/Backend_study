package hello.example.core;

import hello.example.core.member.MemberRepository;
import hello.example.core.member.MemoryMemberRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

//컴포넌트 스캔 방식
@Configuration
@ComponentScan ( //@ComponentScan 은 @Component 가 붙은 모든 클래스를 스프링 빈으로 등록한다
        //basePackages = "hello.example.core.member",  //"@Component"로 붙은 걸 찾을 파일을 선택할수 있다. 해당 경로의 하위 파일을 모두 찾게 된다.

        /*
        만약 지정하지 않으면 @ComponentScan 이 붙은 설정 정보 클래스의 패키지가 시작 위치가 된다 -> AutoAppConfig의 패키지인 "hello.example.core"부터 시작
        <권장방법>
        예를 들어서 프로젝트가 다음과 같이 구조가 되어 있으면
        com.hello
        com.hello.serivce
        com.hello.repository

        com.hello 프로젝트 시작 루트, 여기에 AppConfig 같은 메인 설정 정보를 두고,
        @ComponentScan 애노테이션을 붙이고, basePackages 지정은 생략한다
        */

        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION , classes = Configuration.class)
)
public class AutoAppConfig {

    //자동 빈과 아래의 수동빈이 충돌하게 되면 수동빈 등록이 우선순위를 가지게 된다. / 자동빈 vs 자동빈은 예외 발생!
    //@Bean
    //public MemberRepository memoryMemberRepository() {
    //   return new MemoryMemberRepository();
    //}
}
