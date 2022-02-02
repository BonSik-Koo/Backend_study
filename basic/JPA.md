__JPA(Java Persistence API)란__
================================
- 자바에서 ORM(Object-Relational Mappint)기술 표준으로 사용되는 인터페이스의 모음이다.         
__※ORM(객체 관계 매핑)이란__     
-> 객체는 객체대로 생성하고, 관게형 데이터베이스는 관계형 데이터베이스로 설계한후 두개를 매핑하는 것 -->ORM프레임워크가 중간에서 서로 매핑해줌
- JPA는 인터페이스이고 이를 구현한 대표적인 오픈소스가 Hibernate,EclipseLink,DataNucleus들이 있다. -> 주로 Hibernate를 사용한다. 

__사용하는 이유?__
----------------------
- 기존의 반복코드(객체생성, 쿼리실행객체등)를 줄여주고, 기본적인 SQL문도 JPA가 직접 만들어서 실행해준다.
- SQL 중심적인 설계 개발에서 객체 중심의 설계 개발로 바꿀수 있다.
- 유지보수와 생산성을 높일 수 있다. -> DB 필드 변경시 모든 SQL을 수정해야지만 JPA를 사용하면 JPA의 Entity의 필드만 추가하면 된다.

__JPA 사용방법__
---------------------
① 설정
- build.gradle파일에서 JPA관련 라이브러리 추가
![22222](https://user-images.githubusercontent.com/96917871/152133079-5a3edda0-4449-44a2-b1c9-d4919254e88e.PNG)

- application.properties에서 JPA설정 변경
```
spring.jpa.show-sql=true ->만들어진 쿼리문을 볼수 있다.
spring.jpa.hibernate.ddl-auto=none  -> 엔티티로 설정한 객체를 보고 자동으로 "테이블"까지 생성하는 옵션 --> create로 바꾸면 만들어준다.
```

② Entity 매핑하기!!
- 어노에티션을 사용하여 설정할 객체를 "Entity"로 설정하기        

__Entity(엔티티)란?__
------------------------------
-JPA에서 엔티티는 테이블에 대응하는 하나의 클래스를 만드는 것이다.
ex)   
![333](https://user-images.githubusercontent.com/96917871/152138146-1540185f-7ce1-4aeb-a01d-646c25033cf7.PNG)

__"@Id", "@Column" 설정하기__
------------------------------------
1) "@Id"   

2) "@Column"   








