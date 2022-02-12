package hello.example.core.beanfind;

import hello.example.core.appconfig;
import hello.example.core.member.MemberService;
import hello.example.core.member.MemberServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ApplicationContextBasicFindTest {

    ApplicationContext ac = new AnnotationConfigApplicationContext(appconfig.class);


    @Test
    @DisplayName("빈 이름으로 조회")
    void findBeanName() {
        MemberService memberService = ac.getBean("memberService", MemberService.class);
        Assertions.assertThat(memberService).isInstanceOf(MemberServiceImpl.class); //스프링으로 등록한 memberService 의 빈이 해당 클래스의 인스턴스이면 정상
    }

    @Test
    @DisplayName("이름 없이 타칩만으로 조회")
    void findBeantype() {
        MemberService memberService = ac.getBean( MemberService.class);
        Assertions.assertThat(memberService).isInstanceOf(MemberServiceImpl.class); //스프링으로 등록한 memberService 의 빈이 해당 클래스의 인스턴스이면 정상
    }

    @Test
    @DisplayName("구체 타입으로 조회")
    void findBeanName2() {
        //구체화 타입으로 조회하는것은 좋지 않은 방법이다. ->역할의 의존해야된다.
        MemberService memberService = ac.getBean("memberService", MemberServiceImpl.class);
        Assertions.assertThat(memberService).isInstanceOf(MemberServiceImpl.class); //스프링으로 등록한 memberService 의 빈이 해당 클래스의 인스턴스이면 정상
    }

    @Test
    @DisplayName("빈 이름으로 조회X -> 실패 테스트")
    void findBeanNameX() {
       // MemberService memberService = ac.getBean( "xxxX", MemberService.class);
        org.junit.jupiter.api.Assertions.assertThrows(NoSuchBeanDefinitionException.class,
                () -> ac.getBean( "xxxX", MemberService.class));
    }

}
