package study.springdatajpa.respository.JPA;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.springdatajpa.entity.Member;

import java.util.List;

/**
 * 순수 JPA 테스트
 */
@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    public void 회원테스트() throws Exception {

        //given
        Member member = new Member("memberA");

        //when
        Member save = memberJpaRepository.save(member);

        //then
        Member find = memberJpaRepository.find(save.getId());

        Assertions.assertThat(save.getId()).isEqualTo(find.getId());
        Assertions.assertThat(save.getUsername()).isEqualTo(find.getUsername());
        Assertions.assertThat(save).isEqualTo(find);
    }

    //순수 JPA- 공통인터페이스 사용
    @Test
    public void basicCRUD() throws Exception {

        Member member1 = new Member("memberA");
        Member member2 = new Member("memberB");
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        //단건 조회 검증
        Member findMember1 = memberJpaRepository.findById(member1.getId()).get();
        Member findMember2 = memberJpaRepository.findById(member2.getId()).get();
        Assertions.assertThat(findMember1).isEqualTo(member1);
        Assertions.assertThat(findMember2).isEqualTo(member2);

        //복수 조회 검증
        List<Member> all = memberJpaRepository.findAll();
        Assertions.assertThat(all.size()).isEqualTo(2);

        //카운트 검증
        Long count = memberJpaRepository.count();
        Assertions.assertThat(count).isEqualTo(2);

        //삭제 검증
        memberJpaRepository.delete(member1);
        memberJpaRepository.delete(member2);
        Long deleteCount = memberJpaRepository.count();
        Assertions.assertThat(deleteCount).isEqualTo(0);

    }

    //순수 JPA- 메소드 이름으로 쿼리 생성
    @Test
    public void findByUsernameAndAgeGreaterThan() throws Exception {
        Member member1 = new Member("memberA", 10);
        Member member2 = new Member("memberA", 20);
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        List<Member> result = memberJpaRepository.findByUsernameAndAgeGreaterThan("memberA", 15);

        Assertions.assertThat(result.size()).isEqualTo(1);
        Assertions.assertThat(result.get(0).getUsername()).isEqualTo("memberA");
        Assertions.assertThat(result.get(0).getAge()).isEqualTo(20);
    }

    //순수 JPA - Named 쿼리 호출
    @Test
    public void findByUsername() throws Exception {
        Member member1 = new Member("memberA", 10);
        Member member2 = new Member("memberA", 20);
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        List<Member> result = memberJpaRepository.findByUsername("memberA");

        Assertions.assertThat(result.size()).isEqualTo(2);
        Assertions.assertThat(result.get(0).getUsername()).isEqualTo("memberA");
    }

    //순수 JPA - 벌크 연산
    @Test
    public void 벌크연산테스트() throws Exception {
        //given
        memberJpaRepository.save(new Member("member1", 10));
        memberJpaRepository.save(new Member("member2", 19));
        memberJpaRepository.save(new Member("member3", 20));
        memberJpaRepository.save(new Member("member4", 21));
        memberJpaRepository.save(new Member("member5", 40));

        //when
        int resultCount = memberJpaRepository.bulkAgePlus(20);

        //then
        Assertions.assertThat(resultCount).isEqualTo(3);
    }

}