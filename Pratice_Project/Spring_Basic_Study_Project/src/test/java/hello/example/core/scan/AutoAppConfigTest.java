package hello.example.core.scan;

import hello.example.core.AutoAppConfig;
import hello.example.core.member.MemberService;
import hello.example.core.member.MemberServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AutoAppConfigTest {

    @Test
    public void basicScan() {

        ApplicationContext ac = new AnnotationConfigApplicationContext(AutoAppConfig.class); //자바코드 빈을 등록할때와 같은 기능 ->AutoAppconfig은 컴포넌트 스캔 방식을 사용해서 진행됨

        MemberService memberService = ac.getBean(MemberService.class);
        Assertions.assertThat(memberService).isInstanceOf(MemberService.class);

    }
}
