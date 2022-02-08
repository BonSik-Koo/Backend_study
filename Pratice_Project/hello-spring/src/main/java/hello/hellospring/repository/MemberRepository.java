package hello.hellospring.repository;

import hello.hellospring.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    public Member save(Member member);
    public Optional<Member> findById(Long id); //Optional기능 -> null이 나오더라도 Member로 감싸서 반환해준다.
    public Optional<Member> findByName(String name);
    public List<Member> findAll();
}
