package hello.example.core.singleton;

import hello.example.core.appconfig;
import hello.example.core.member.Member;
import hello.example.core.member.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class SingletonTest {

    @Test
    @DisplayName("스프링 없는 순수 DI 컨테이너")
    void pureContainer() {
        appconfig app = new appconfig();

        MemberService memberService1 = app.memberService();
        MemberService memberService2 = app.memberService();

        System.out.println("memberservice1: "+ memberService1);
        System.out.println("memberservice2: "+ memberService2);

        //Assertions.assertThat(memberService1).isNotEqualTo(memberService2);
        Assertions.assertThat(memberService1).isNotSameAs(memberService2);

        //객체가 요청때마다 새롭게 생성한다.
    }

    @Test
    @DisplayName("스프링 없이 싱글톤 패턴을 적용한 객체 사용")
    public void singleServiceTest() {

        SingleTonService singleTonService1 = SingleTonService.getInstance();

        SingleTonService singleTonService2 = SingleTonService.getInstance();

        System.out.println("SingleTonService1: " + singleTonService1);
        System.out.println("SingleTonService2: " + singleTonService2);

        //Assertions.assertThat(singleTonService1).isEqualTo(singleTonService2);
        Assertions.assertThat(singleTonService1).isSameAs(singleTonService2);

    }

    @Test
    @DisplayName("스프링 컨테이너와 싱글톤")
    public void springContanier() {

        ApplicationContext ac = new AnnotationConfigApplicationContext(appconfig.class);

        MemberService memberService1 = ac.getBean("memberService", MemberService.class);
        MemberService memberService2 = ac.getBean("memberService", MemberService.class);

        System.out.println("memberservice1: "+ memberService1);
        System.out.println("memberservice2: "+ memberService2);

        Assertions.assertThat(memberService1).isSameAs(memberService2);
    }

}
