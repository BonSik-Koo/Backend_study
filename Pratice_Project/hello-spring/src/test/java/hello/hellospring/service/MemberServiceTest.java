package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemoryMemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class MemberServiceTest {

    MemberService memberService;
    MemoryMemberRepository memberRepository;

    @BeforeEach
    public void beforeEach() {
        memberRepository = new MemoryMemberRepository();
        memberService = new MemberService(memberRepository);
    }

    @AfterEach //@Test인 메소드를 하나 실행할때마다 해당 메소드를 실행한다.
    public void afterEach() {
        memberRepository.clearStore();
    }

    @Test
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

    @Test
    void findMember() {
    }

    @Test
    void findOne() {
    }
}