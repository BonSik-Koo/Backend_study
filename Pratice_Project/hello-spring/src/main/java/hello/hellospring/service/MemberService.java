package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
import hello.hellospring.repository.MemoryMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

//@Service //스프링이 시작될때 스프링 컨테이너가 생성되는데 "@Service"로 지정한 객체의 생성자를 호출하고 생성한 객체를 스프링 컨테이너에 등록하게 된다. ->컴포넌트 스캔방식
@Transactional //JPA를 사용할때 항상 DB에 데이터가 변경되는 곳에는 "@Transactional" 어노테이션이 되어있어야된다.!!!!!!!!! -> 아래 join 메소드에서만 데이터가 변경되니깐 join 메소드에만 걸어줘도 된다.
public class MemberService {

    private final MemberRepository memberRepository;

    //@Autowired //스프링 컨테이너에 있는 "memberRepository"을 넣어주는다.->컴포넌트 스캔 방식
    public MemberService(MemberRepository memberRepository) { //생성자주입("DI"방식 중)
        this.memberRepository = memberRepository;
    }

    /*회원가입*/
    public Long join(Member member) {

        validateDuplicateMember(member); //중복검사
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        //Optional 메소드중 값이 있는지 판별하는 "ifPresent"메소드 사용 -> 람다식으로 구현 가능하다.
        memberRepository.findByName(member.getName())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다"); //예외와 메세지 터트림
                });
    }

    /*전체 회원 조회*/
    public List<Member> findMember() {
        return memberRepository.findAll();
    }

    public Optional<Member> findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }

}
