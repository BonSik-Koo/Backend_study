package hello.example.core.autowired;

import hello.example.core.member.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.lang.Nullable;

import java.util.Optional;

//"Autowired" 의 옵션 테스트
public class AutowiredTest {

    @Test
    public void AutowiredOptin() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(TestBean.class);
    }

    static class TestBean {

        @Autowired(required = false) //기본값은 true인데 false로 하면 자동의존관계주입시 해당 Member타입의 빈이 없으면 해당 메소드를 호출하지 않게된다.!!!
        public void setNoBean1(Member member) {
            System.out.println("noBean1: " + member);
        }

        @Autowired
        public void setNoBean2(@Nullable Member member) { //위와 다르게 메소드가 호출은되는데 member에 "null"값이 들어오게 된다.
            System.out.println("noBean2: " + member);
        }

        @Autowired
        public void setNoBean3(Optional<Member> member) { //java8에서 지원하는 "Optional"은 마찬가지로 스프링빈에 없으면 Optional에 null 값을 담아준다.
            System.out.println("noBean3: " + member);
        }
    }
}
