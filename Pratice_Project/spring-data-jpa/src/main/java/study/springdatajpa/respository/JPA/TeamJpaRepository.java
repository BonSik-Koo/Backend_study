package study.springdatajpa.respository.JPA;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import study.springdatajpa.entity.Member;
import study.springdatajpa.entity.Team;

import java.util.List;
import java.util.Optional;

/**
 * 순수 JPA 기반 레포지토리
 */
@Repository
public class TeamJpaRepository {

    @PersistenceContext
    private EntityManager em;

    //저장
    public Team save(Team team){
        em.persist(team);
        return team;
    }

    //단건 조회
    public Optional<Team> findById(Long id){
        Team team = em.find(Team.class, id);
        return Optional.ofNullable(team);
    }

    //복수건 조회
    //JPQL 사용
    public List<Team> findAll() {
        return em.createQuery("select t from Team t",Team.class)
                .getResultList();
    }

    //개수 조회
    //JPQL 사용, count 는 Long 타입 반환!
    public Long count() {
        return em.createQuery("select count(t) from Team t", Long.class)
                .getSingleResult();
    }

}

