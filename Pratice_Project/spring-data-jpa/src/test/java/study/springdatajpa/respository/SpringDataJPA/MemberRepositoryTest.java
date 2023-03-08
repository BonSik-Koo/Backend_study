package study.springdatajpa.respository.SpringDataJPA;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import study.springdatajpa.dto.MemberDto;
import study.springdatajpa.entity.Member;
import study.springdatajpa.entity.Team;
import study.springdatajpa.entity.projections.MemberProjection;
import study.springdatajpa.entity.projections.UsernameOnly;
import study.springdatajpa.entity.projections.UsernameOnlyDto;
import study.springdatajpa.respository.JPA.MemberJpaRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * spring Data JPA 테스트
 */
@SpringBootTest
@Transactional
public class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @Autowired MemberJpaRepository memberJpaRepository;
    @Autowired TeamRepository teamRepository;
    @PersistenceContext EntityManager em;

    @Test
    public void 회원테스트() throws Exception {
        //given
        Member member = new Member("memberA");

        //when
        Member saveMember = memberRepository.save(member);
        Optional<Member> find = memberRepository.findById(saveMember.getId()); //spring data Jpa 는 Optional 로 제공!

        Member findMember = find.get();

        //then
        Assertions.assertThat(saveMember.getId()).isEqualTo(findMember.getId());
        Assertions.assertThat(saveMember.getUsername()).isEqualTo(findMember.getUsername());
        Assertions.assertThat(saveMember).isEqualTo(findMember);
    }

    @Test
    public void basicCRUD() throws Exception {

        Member member1 = new Member("memberA");
        Member member2 = new Member("memberB");
        memberRepository.save(member1);
        memberRepository.save(member2);

        //단건 조회 검증
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        Assertions.assertThat(findMember1).isEqualTo(member1);
        Assertions.assertThat(findMember2).isEqualTo(member2);

        //복수 조회 검증
        List<Member> all = memberRepository.findAll();
        Assertions.assertThat(all.size()).isEqualTo(2);

        //카운트 검증
        Long count = memberRepository.count();
        Assertions.assertThat(count).isEqualTo(2);

        //삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);
        Long deleteCount = memberRepository.count();
        Assertions.assertThat(deleteCount).isEqualTo(0);

    }

    //Spring Data JPA- 메소드 이름으로 쿼리 생성
    @Test
    public void findByUsernameAndAgeGreaterThan() throws Exception {
        Member member1 = new Member("memberA", 10);
        Member member2 = new Member("memberA", 20);

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("memberA", 15);

        Assertions.assertThat(result.size()).isEqualTo(1);
        Assertions.assertThat(result.get(0).getUsername()).isEqualTo("memberA");
        Assertions.assertThat(result.get(0).getAge()).isEqualTo(20);
    }

    //Spring Data JPA- 메소드 이름으로 쿼리 생성
    @Test
    public void findTop3TestBy() throws Exception {

        Member member1 = new Member("memberA", 10);
        Member member2 = new Member("memberA", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> result = memberRepository.findTop3TestBy();

        Assertions.assertThat(result.size()).isEqualTo(2);
    }

    //Spring Data JPA - Named 쿼리 호출
    @Test
    public void findByUsername() throws Exception {
        Member member1 = new Member("memberA", 10);
        Member member2 = new Member("memberA", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> result = memberRepository.findByUsername("memberA");

        Assertions.assertThat(result.size()).isEqualTo(2);
        Assertions.assertThat(result.get(0).getUsername()).isEqualTo("memberA");
    }

    //Spring Data JPA - 리포지토리 메소드에 쿼리 정의하기
    @Test
    public void findMember() throws Exception {

        Member member1 = new Member("memberA", 10);
        Member member2 = new Member("memberB", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> result = memberRepository.findMember("memberA", 10);
        Assertions.assertThat(result.size()).isEqualTo(1);
    }

    //Spring Data JPA - 리포지토리 메소드에 쿼리 정의하기 - 특정 값만 조회
    @Test
    public void findUserNameList() throws Exception {
        Member member1 = new Member("memberA", 10);
        Member member2 = new Member("memberB", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<String> result = memberRepository.findUserNameList();
        for(String name : result){
            System.out.println("username = " + name);
        }
    }

    //Spring Data JPA - 리포지토리 메소드에 쿼리 정의하기 - Dto로 조회
    @Test
    public void findMemberDto() throws Exception {
        Team team = new Team("teamA");
        teamRepository.save(team);

        Member member = new Member("memberA", 10, team);
        memberRepository.save(member);

        List<MemberDto> result = memberRepository.findMemberDto();
        for(MemberDto dto : result){
            System.out.println("dto = " + dto);
        }
    }

    //Spring Data JPA - 리포지토리 메소드에 쿼리 정의하기 - 컬렉션 파라미터 바인딩
    @Test
    public void findByNames() throws Exception {
        Member member1 = new Member("memberA", 10);
        Member member2 = new Member("memberB", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> result = memberRepository.findByNames(Arrays.asList("memberA", "memberB"));

        for(Member m : result){
            System.out.println("member = " + m);
        }
    }

    //Spring Data JPA - 유연한 반환 타입 지원
    @Test
    public void returnType() throws Exception {
        Member member1 = new Member("memberA", 10);
        Member member2 = new Member("memberB", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        Member result1 = memberRepository.findMemberByUsername("memberA");
        Optional<Member> result2 = memberRepository.findOptionalByUsername("memberC");
        List<Member> result3 = memberRepository.findListByUsername("memberB");

        Assertions.assertThat(result1.getUsername()).isEqualTo("memberA");
        Assertions.assertThat(result2.isPresent()).isFalse();
        Assertions.assertThat(result3.size()).isEqualTo(1);
    }

    //Spring Data JPA - 기본 페이지과 정렬 테스트
    @Test
    public void page() throws Exception {
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        //when
        PageRequest pageRequest = PageRequest.of(0,3, Sort.by(Sort.Direction.DESC, "username"));
        Page<Member> page = memberRepository.findPageByAge(10, pageRequest);

        //then
        List<Member> content = page.getContent();
        Assertions.assertThat(content.size()).isEqualTo(3);
        Assertions.assertThat(page.getTotalElements()).isEqualTo(5); //전체 데이터 수
        Assertions.assertThat(page.getNumber()).isEqualTo(0); //페이지 번호
        Assertions.assertThat(page.getTotalPages()).isEqualTo(2); //전페 페이지 번호
        Assertions.assertThat(page.isFirst()).isTrue(); //첫번째 페이지 맞는지
        Assertions.assertThat(page.hasNext()).isTrue(); //다음 페이지 있는지
    }

    //Spring Data JPA - 기본 페이지과 정렬 테스트 + 레포지토리에 쿼리 작성 사용
    @Test
    public void pageMember() throws Exception {
        //given
        memberRepository.save(new Member("member", 10));
        memberRepository.save(new Member("member", 15));
        memberRepository.save(new Member("member", 15));
        memberRepository.save(new Member("member", 15));
        memberRepository.save(new Member("member", 15));

        //when
        PageRequest pageRequest = PageRequest.of(0,3, Sort.by(Sort.Direction.DESC, "username"));
        Page<Member> page = memberRepository.findMemberPage("member", 10, pageRequest);

        //then
        List<Member> content = page.getContent();
        Assertions.assertThat(content.size()).isEqualTo(3);
        Assertions.assertThat(page.getTotalElements()).isEqualTo(4); //전체 데이터 수
        Assertions.assertThat(page.getNumber()).isEqualTo(0); //페이지 번호
        Assertions.assertThat(page.getTotalPages()).isEqualTo(2); //전체 페이지 번호
        Assertions.assertThat(page.isFirst()).isTrue(); //첫번째 페이지 맞는지
        Assertions.assertThat(page.hasNext()).isTrue(); //다음 페이지 있는지
    }

    //Spring Data JPA - 기본 페이지과 정렬 테스트 + count query 분리
    @Test
    public void pageMemberAndTeam() throws Exception {
        //given
        Team team1 = teamRepository.save(new Team("teamA"));
        Team team2 = teamRepository.save(new Team("teamB"));

        memberRepository.save(new Member("member", 10, team1));
        memberRepository.save(new Member("member", 15, team1));
        memberRepository.save(new Member("member", 15, team2));
        memberRepository.save(new Member("member", 15));
        memberRepository.save(new Member("member", 15));

        //when
        PageRequest pageRequest = PageRequest.of(0,3, Sort.by(Sort.Direction.DESC, "username"));
        Page<MemberDto> page = memberRepository.findMemberAndTeamPage("member", pageRequest);

        //then
        List<MemberDto> content = page.getContent();
        Assertions.assertThat(content.size()).isEqualTo(3);
        Assertions.assertThat(page.getTotalElements()).isEqualTo(5); //전체 데이터 수
        Assertions.assertThat(page.getNumber()).isEqualTo(0); //페이지 번호
        Assertions.assertThat(page.getTotalPages()).isEqualTo(2); //전페 페이지 번호
        Assertions.assertThat(page.isFirst()).isTrue(); //첫번째 페이지 맞는지
        Assertions.assertThat(page.hasNext()).isTrue(); //다음 페이지 있는지
    }

    //Spring Data JPA - 기본 페이지과 정렬 테스트 + slice
    @Test
    public void slice() throws Exception {
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        //when
        PageRequest pageRequest = PageRequest.of(0,3, Sort.by(Sort.Direction.DESC, "username"));
        Slice<Member> page = memberRepository.findSliceByAge(10, pageRequest);

        //then
        List<Member> content = page.getContent();
        Assertions.assertThat(content.size()).isEqualTo(3);
        //Assertions.assertThat(page.getTotalElements()).isEqualTo(5); //전체 데이터 수 -> 지원안됨!!!
        Assertions.assertThat(page.getNumber()).isEqualTo(0); //페이지 번호
        //Assertions.assertThat(page.getTotalPages()).isEqualTo(2); //전페 페이지 번호
        Assertions.assertThat(page.isFirst()).isTrue(); //첫번째 페이지 맞는지 -> 지원안됨!!!
        Assertions.assertThat(page.hasNext()).isTrue(); //다음 페이지 있는지
    }

    //Spring Data JPA - 벌크 연산
    @Test
    public void 벌크연산테스트() throws Exception {
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 21));
        memberRepository.save(new Member("member5", 40));

        //when
        int resultCount = memberRepository.bulkAgePlus(20);

        //then
        Assertions.assertThat(resultCount).isEqualTo(3);

        //벌크연산 주위사항!
        Member member = memberRepository.findByUsername("member5").get(0);
        Assertions.assertThat(member.getAge()).isEqualTo(41);
    }

    //Spring Data JPA - 엔티티그래프(fetch join 해주는것)
    @Test
    public void EntityGrapth() throws Exception {

        //given
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        memberRepository.save(new Member("member1", 10, teamA));
        memberRepository.save(new Member("member1", 20, teamB));

        em.flush();
        em.clear();

        //when
        //List<Member> members = memberRepository.findMemberFetchJoin();
        //List<Member> members = memberRepository.findMemberEGByUsername("member1");
        //List<Member> members = memberRepository.findMemberJPQL_EG();
        List<Member> members = memberRepository.findAll();

        //then
        for (Member member : members) {
            System.out.println("member = " + member.getUsername());
            System.out.println("member.team = " + member.getTeam().getName());
        }
    }

    //Spring Data JPA - ReadOnly
    @Test
    public void ReadOnly() throws Exception {
        //given
        memberRepository.save(new Member("member1",10));
        em.flush();
        em.clear();

        //when
        Member member = memberRepository.findReadOnlyByUsername("member1");
        member.setUsername("member2");

        em.flush(); //업데이트 쿼리 발생 X
    }


    //Spring Data JPA - 사용자 정의 리포지토리 구현
    @Test
    public void CustomRepository() throws Exception {

        List<Member> result = memberRepository.findMemberCustom();
        Assertions.assertThat(result.size()).isEqualTo(0);
    }


    //순수 JPA, Spring Dat JPA - Auditing
    @Test
    public void JpaEventBaseEntity() throws Exception {

        Member member = new Member("member1");
        memberJpaRepository.save(member); //@PrePersist 실행

        Thread.sleep(100);
        member.setUsername("member2");

        em.flush(); //@PreUpdate 실행
        em.clear();

        Member findMember = memberJpaRepository.findById(member.getId()).get();

        System.out.println("findMember.createdDate = " + findMember.getCreatedDate());
        //System.out.println("findMember.updatedDate = " + findMember.getUpdatedDate());
        System.out.println("findMember.lastModifiedDate = " + findMember.getLastModifiedDate());
        System.out.println("findMember.createdBy = " + findMember.getCreatedBy());
        System.out.println("findMember.lastModifiedBy = " + findMember.getLastModifiedBy());
    }

    //Projects
    @Test
    public void projections() throws Exception {
        //given
        Team teamA = new Team("teamA");
        em.persist(teamA);

        //when
        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 0, teamA);
        em.persist(m1);
        em.persist(m2);
        em.flush();
        em.clear();

        //List<UsernameOnly> result = memberRepository.findProjectsByUsername("m1");
        List<UsernameOnlyDto> result = memberRepository.findProjectsDtoByUsername("m1");

        //then
        Assertions.assertThat(result.size()).isEqualTo(1);
    }
    
    @Test
    public void NativeQuery() throws Exception {
        Team teamA = new Team("teamA");
        em.persist(teamA);

        //when
        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 0, teamA);
        em.persist(m1);
        em.persist(m2);
        em.flush();
        em.clear();

        Page<MemberProjection> result = memberRepository.findByNativeProjections(PageRequest.of(0, 10));
        List<MemberProjection> content = result.getContent();
        
        //then
        for (MemberProjection memberProjection : content) {
            System.out.println("memberProjection.getId() = " + memberProjection.getId());
            System.out.println("memberProjection.getUsername() = " + memberProjection.getUsername());
            System.out.println("memberProjection.getTeamName() = " + memberProjection.getTeamName());
        }
    }
}
