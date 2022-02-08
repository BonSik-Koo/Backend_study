package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class JpaMemberRepository implements MemberRepository{

    private final EntityManager em;

    //@Autowired
    public JpaMemberRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Member save(Member member) {
        em.persist(member); //JPA해당 멤버를 DB에 쿼리도 자동으로 작성하여 넣어준다. ,entity의 변수들이 private 이면 정의한 setter,getter메소드를 호출하여 넣어준다.
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
       Member member = em.find(Member.class, id); //"PK"일 경우에는 이러한 방식으로 쉽게 조회 가능!!!!

       return Optional.ofNullable(member);
    }

    //"PK"로 아닌 걸로 검색하러면 "jpql"를 작성해야 된다.
    @Override
    public Optional<Member> findByName(String name) {
        List<Member> result = em.createQuery("select m from Member m where m.name = :name", Member.class)  //"PK"가 아닌 다른 열의 이름으로 조회 할때 !!
                .setParameter("name", name)
                .getResultList();

        return result.stream().findAny();
    }

    @Override
    public List<Member> findAll() {
        return em.createQuery("select m from Member m" , Member.class) //"PK"가 아닌 다른 열의 이름으로 조회 할때 !!
                .getResultList();
    }
}
