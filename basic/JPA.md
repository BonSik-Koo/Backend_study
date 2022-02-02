__JPA(Java Persistence API)란__
================================
- 자바에서 ORM(Object-Relational Mappint)기술 표준으로 사용되는 인터페이스의 모음이다.         
__※ORM(객체 관계 매핑)이란__     
-> 객체는 객체대로 생성하고, 관게형 데이터베이스는 관계형 데이터베이스로 설계한후 두개를 매핑하는 것      
-->ORM프레임워크가 중간에서 서로 매핑해줌
- JPA는 인터페이스이고 이를 구현한 대표적인 오픈소스가 Hibernate,EclipseLink,DataNucleus들이 있다. -> 주로 Hibernate를 사용한다. 

__사용하는 이유?__
============================
- 기존의 반복코드(객체생성, 쿼리실행객체등)를 줄여주고, 기본적인 SQL문도 JPA가 직접 만들어서 실행해준다.
- SQL 중심적인 설계 개발에서 객체 중심의 설계 개발로 바꿀수 있다.
- 유지보수와 생산성을 높일 수 있다. -> DB 필드 변경시 모든 SQL을 수정해야지만 JPA를 사용하면 JPA의 Entity의 필드만 추가하면 된다.

__JPA 관련 어노테이션__
===========================
① 설정
-------------
- build.gradle파일에서 JPA관련 라이브러리 추가
![22222](https://user-images.githubusercontent.com/96917871/152133079-5a3edda0-4449-44a2-b1c9-d4919254e88e.PNG)

- application.properties에서 JPA설정 변경
```
spring.jpa.show-sql=true ->만들어진 쿼리문을 볼수 있다.
spring.jpa.hibernate.ddl-auto=none  -> 엔티티로 설정한 객체를 보고 자동으로 "테이블"까지 생성하는 옵션 --> create로 바꾸면 만들어준다.
```
==> 위의 설정을 마치면 스프링을 시작하면 스프링 부트에서 자동으로 "EntityManager"를 생성한다. ->DB와 다 연결시켜서

__② Entity 매핑하기!!__
-----------------------------
- 어노에티션을 사용하여 설정할 객체를 "Entity"로 설정하기        

__※ Entity(엔티티)란?__ ->JPA에서 엔티티는 테이블에 대응하는 하나의 클래스를 만드는 것이다.          
ex)         
![333](https://user-images.githubusercontent.com/96917871/152138146-1540185f-7ce1-4aeb-a01d-646c25033cf7.PNG)

__"@Id", "@Column" 설정하기__
------------------------------------
```
import javax.persistence.*;
@Entity //JPA과 관리하는 entity
public class Member { //회원들의 정보

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@Column(name="name")
    private String name;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id=id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name){
        this.name= name;
    }

}
```
1) "@Id"   
- DB에서의 Primary Key를 의미한다. 
- 모든 엔티티에 반드시 @Id를 지정해주어야 한다.
- "@GeneratedValue(strategy = GenerationType.IDENTITY)" : PK인 id의 값을 생성하는 방식을 "IDENTITY"방식으로 설정      
※ DB에서 값이 들어올때마다 자동으로 값을 증가시켜 생성하는 방식 -> "IDENTITY"방식이라고 한다.   

2) "@Column"   
- DB의 column의 해당하는 열을 대입하는 것
- DB의 열의 이름과 entity객체의 열의 변수의 이름이 같은 경우 "@Column" 어노테이션을 생략할수 있다. 하지만 이름이 다르다면 entity객체의 변수에 '@Column(name="열의이름")'을 어노테이션으로 정의해두어야 한다.

__JPA 쿼리문 예시__
========================================
```
public class JpaMemberRepository implements MemberRepository{

    private final EntityManager em;

    @Autowired
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
```






