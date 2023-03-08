package study.springdatajpa.respository.SpringDataJPA;

import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.springdatajpa.dto.MemberDto;
import study.springdatajpa.entity.Member;
import study.springdatajpa.entity.projections.MemberProjection;
import study.springdatajpa.entity.projections.UsernameOnly;
import study.springdatajpa.entity.projections.UsernameOnlyDto;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * 스프링 데이터 JPA 기반 레포지토리
 */
public interface MemberRepository extends JpaRepository<Member, Long> , MemberRepositoryCustom {

    //Spring Data JPA- 메소드 이름으로 쿼리 생성
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    //Spring Data JPA- 메소드 이름으로 쿼리 생성
    List<Member> findTop3TestBy();

    //Spring Data JPA - Named 쿼리 호출
    @Query(name = "Member.findByUsername") //생략 가능
    List<Member> findByUsername(@Param("username")String username);

    //Spring Data JPA - 리포지토리 메소드에 쿼리 정의하기
    @Query("select m from Member m where m.username= :username and m.age= :age")
    List<Member> findMember(@Param("username") String username, @Param("age") int age);

    //Spring Data JPA - 리포지토리 메소드에 쿼리 정의하기 - 특정 값만 조회
    @Query("select m.username from Member m")
    List<String> findUserNameList();

    //Spring Data JPA - 리포지토리 메소드에 쿼리 정의하기 - Dto 조회 -> QueryDSL 쓰면 더 편하다!
    @Query("select new study.springdatajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    //Spring Data JPA - 리포지토리 메소드에 쿼리 정의하기 - 컬렉션 파라미터 바인딩
    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names")Collection<String> names);

    //Spring Data JPA - 유연한 반환 타입 지원
    List<Member> findListByUsername(String username);
    Member findMemberByUsername(String username);
    Optional<Member> findOptionalByUsername(String username);


    //Spring Data JPA - 페이징과 정렬
    Page<Member> findPageByAge(int age, Pageable pageable);

    //Spring Data JPA - 페이징과 정렬 -> @Query 레포지토리에 쿼리 작성
    @Query("select m from Member m where m.username=:username and m.age>:age")
    Page<Member> findMemberPage(@Param("username") String username, @Param("age") int age, Pageable pageable);

    //Spring Data JPA - 페이징과 정렬 -> count 쿼리를 다음과 같이 분리
    @Query(value = "select new study.springdatajpa.dto.MemberDto(m.id, m.username, t.name) from Member m left join m.team t where m.username=:username",
            countQuery = "select count(m) from Member m")
    Page<MemberDto> findMemberAndTeamPage(@Param("username") String username, Pageable pageable);

    //Spring Data JPA - 페이징과 정렬 -> slice 페이징과 정렬
    Slice<Member> findSliceByAge(int age, Pageable pageable);


    //Spring Data JPA - 벌크 연산
    @Modifying(clearAutomatically = true) //순수 JPA 에서 "executeUpdate()" 역할
    @Query("update Member m set m.age=m.age+1 where m.age>= :age")
    int bulkAgePlus(@Param("age")int age);


    //Spring Data JPA - 엔티티그래프(fetch join 해주는것) - JPQL 방식
    @Query("select m from Member m left join fetch m.team t")
    List<Member> findMemberFetchJoin();

    //Spring Data JPA - 엔티티그래프(fetch join 해주는것) - 메서드 이름 + 엔티티 그래프
    @EntityGraph(attributePaths = {"team"})
    List<Member> findMemberEGByUsername(String username);

    //Spring Data JPA - 엔티티그래프(fetch join 해주는것) - JPQL + 엔티티 그래프
    @Query("select m from Member m")
    @EntityGraph(attributePaths = {"team"})
    List<Member> findMemberJPQL_EG();

    //Spring Data JPA - 엔티티그래프(fetch join 해주는것) - "공통 인터페이스 방식"을 오버라이드 (이건별로..)
    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();


    //Spring Data JPA - ReadOnly
    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String username);


    //Spring Data JPA Projections
//    List<UsernameOnly> findProjectsByUsername(String username);                   //인터페이스 기반 close projections
//    @Query("select m.username as username, t.name as teamName " +
//            "from Member m inner join m.team t " +
//            "where m.username=:username")
//    List<UsernameOnly> findProjectsByUsername(@Param("username") String username);  //JPQL + 인터페이스 기반 close projections
//    List<UsernameOnly> findProjectsByUsername(String username);                     //인터페이스 기반 open projections

//    List<UsernameOnlyDto> findProjectsDtoByUsername(String username);               //클래스 기반 projections
    @Query("select new study.springdatajpa.entity.projections.UsernameOnlyDto(m.username, t.name)" +
            "from Member m left join m.team t " +
            "where m.username=:username")
    List<UsernameOnlyDto> findProjectsDtoByUsername(@Param("username") String username); //JPQL + 클래스 기반 projections


    //네이티브 쿼리 + 인터페이스 기반 Projections
    @Query(value = "select m.member_id as id, m.username, t.name as teamName " +
            "from member m left join team t on m.team_id = t.team_id",
            countQuery = "select count(*) from member",
            nativeQuery = true)
    Page<MemberProjection> findByNativeProjections(Pageable pageable);

}
