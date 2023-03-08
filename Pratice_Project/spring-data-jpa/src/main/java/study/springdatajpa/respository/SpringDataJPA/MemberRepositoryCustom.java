package study.springdatajpa.respository.SpringDataJPA;

import study.springdatajpa.entity.Member;

import java.util.List;

public interface MemberRepositoryCustom {

    List<Member> findMemberCustom();
}
