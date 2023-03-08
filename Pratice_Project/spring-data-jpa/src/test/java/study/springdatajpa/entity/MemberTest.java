package study.springdatajpa.entity;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberTest {

    @Autowired
    EntityManager em;

    @Test
    public void 회원과팀테스트() throws Exception {

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("memberA", 10, teamA);
        Member member2 = new Member("memberB", 20, teamA);
        Member member3 = new Member("memberC", 30, teamB);
        Member member4 = new Member("memberD", 40, teamB);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        //영속성 컨텍스트 초기화
        em.flush(); //DB에 commit
        em.clear(); //캐시 지움

        //JPQL 테스트
        List<Member> members = em.createQuery("select m from Member m", Member.class)
                .getResultList();

        for(Member m : members){
            System.out.println("member=" + m);
            System.out.println("-> member.team"+ m.getTeam());
        }
    }
}