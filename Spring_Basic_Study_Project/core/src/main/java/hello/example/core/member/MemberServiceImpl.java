package hello.example.core.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component//컴포넌트 스캔 방식
public class MemberServiceImpl implements MemberService{

    private MemberRepository memberRepository;

    @Autowired //컴포넌트 스캔 방식, 스프링 컨테이너가 자동으로 해당 스프링 빈을 찾아서 주입한다 ->이때 기본 조회 전략은 타입이 같은 빈을 찾아서 주입한다 (ex. MemberService와 MemberServiceImpl은 같은 타입이다 MemberService가 더 상의 타입이다)
    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Autowired
    public void setMemberRepository(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void join(Member member) {
        memberRepository.save(member);
    }

    @Override
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId);
    }

    //테스트 용도
    public MemberRepository getMemberRepository() {
        return this.memberRepository;
    }
}
