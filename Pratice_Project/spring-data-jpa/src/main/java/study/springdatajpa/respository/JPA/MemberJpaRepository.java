package study.springdatajpa.respository.JPA;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import study.springdatajpa.entity.Member;

import java.util.List;
import java.util.Optional;

/**
 * 순수 JPA 기반 레포지토리
 */
@Repository
public class MemberJpaRepository {

    @PersistenceContext
    private EntityManager em;

    //저장
    public Member save(Member member){
        em.persist(member);
        return member;
    }

    //단건 조회
    public Member find(Long id){
        return em.find(Member.class, id);
    }
    public Optional<Member> findById(Long id){
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    //복수건 조회
    //JPQL 사용
    public List<Member> findAll() {
        return em.createQuery("select m from Member m",Member.class)
                .getResultList();
    }

    //개수 조회
    //JPQL 사용, count 는 Long 타입 반환!
    public Long count() {
        return em.createQuery("select count(m) from Member m", Long.class)
                .getSingleResult();
    }

    //삭제
    public void delete(Member member){
        em.remove(member);
    }

    //순수 JPA- 메소드 이름으로 쿼리 생성
    public List<Member> findByUsernameAndAgeGreaterThan(String username, int age){
        return em.createQuery("select m from Member m where m.username=:username and m.age>:age")
                .setParameter("username", username)
                .setParameter("age",age)
                .getResultList();
    }

    //순수 JPA - Named 쿼리 호출
    public List<Member> findByUsername(String username){
        return em.createNamedQuery("Member.findByUsername")
                .setParameter("username", username)
                .getResultList();
    }

    //순수 JPA - 벌크 연산
    public int bulkAgePlus(int age) {
        return em.createQuery("update Member m set m.age=m.age+1 where m.age>=:age")
                .setParameter("age", age)
                .executeUpdate();
    }

}
