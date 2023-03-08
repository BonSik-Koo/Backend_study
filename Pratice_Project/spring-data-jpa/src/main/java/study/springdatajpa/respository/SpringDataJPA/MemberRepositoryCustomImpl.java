package study.springdatajpa.respository.SpringDataJPA;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import study.springdatajpa.entity.Member;

import java.util.List;

@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

    private final EntityManager em;

    @Override
    public List<Member> findMemberCustom() {
        return em.createQuery("select m from Member m")
                .getResultList();
    }
}
