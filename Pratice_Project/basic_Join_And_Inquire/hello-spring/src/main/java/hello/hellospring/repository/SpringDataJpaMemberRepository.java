package hello.hellospring.repository;

import hello.hellospring.AOP.TimeTraceAop;
import org.springframework.data.jpa.repository.JpaRepository;
import hello.hellospring.domain.Member;
import java.util.Optional;

//스프링 데이터가 "JpaRepository<엔티티 객체자료형, PI 자료형>" 상속받은 인터페이스를 자동으로 구현체를 만들어 스프링 빈에 등록한다!!!!!!
public interface SpringDataJpaMemberRepository extends JpaRepository<Member, Long> ,MemberRepository {

    //Jpql -> select m from Member m where m.name = ? 작성된다.
    // 예) findByUser_name -> select m from Member m where m.User_name = ?
    @Override
    Optional<Member> findByName(String name);
}
