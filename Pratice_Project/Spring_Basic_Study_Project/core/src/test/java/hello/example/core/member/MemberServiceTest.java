package hello.example.core.member;

import hello.example.core.appconfig;
import hello.example.core.order.OrderService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MemberServiceTest {

    MemberService memberService;

    @BeforeEach
    public void beforeEach() {
        appconfig app = new appconfig();
       memberService = app.memberService();
    }

    @Test
    void join() {
        //When
        Member member = new Member(1L, "memberA", Grade.VIP);

        //given
        memberService.join(member);
        Member findMember = memberService.findMember(1L);

        //then
        Assertions.assertThat(member).isEqualTo(findMember);
    }
}
