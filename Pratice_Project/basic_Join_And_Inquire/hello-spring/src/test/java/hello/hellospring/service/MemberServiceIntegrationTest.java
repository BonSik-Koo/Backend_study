package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
import hello.hellospring.repository.MemoryMemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest  //스프링 컨테이너와 테스트를 함께 실행한다 ->실제로 컨트롤러등 빈으로 등록한것이 전부다 컨테이너에 등록된다.
@Transactional //: 테스트 케이스에 이 애노테이션이 있으면, 테스트 시작 전에 트랜잭션을 시작하고, 테스트 완료 후에 항상 롤백한다. 이렇게 하면 DB에 데이터가 남지 않으므로 다음 테스트에 영향을 주지않는다
class MemberServiceIntegrationTest {

    @Autowired MemberService memberService;

    @Test
    //@Commit -> "Transactional" 되지 않고 DB에 들어간다.!
    void join() {
        //given
        Member member = new Member();
        member.setName("hello");

        //when
        Long saveId = memberService.join(member);

        //then
        Member findMember = memberService.findOne(saveId).get();
        assertThat(member.getName()).isEqualTo(findMember.getName());
    }

    @Test
    void 중복_회원_예외() {
        //given
        Member member1 = new Member();
        member1.setName("spring");

        Member member2 = new Member();
        member2.setName("spring");

        //when
        memberService.join(member1);

        /* 방법1 -> 중복됬을때 예외 던지는걸 잡는방법*/
        IllegalStateException e = assertThrows(IllegalStateException.class , () -> memberService.join(member2));
        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다");

        /* 방법2 -> 중복됬을때 예외 던지는걸 잡는방법
        try {
            memberService.join(member2);
            fail(); //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!의미 보기
        }catch (IllegalStateException e ) {
            assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다"); //터진 예외의 메세지 매핑
        }
        */
    }

}