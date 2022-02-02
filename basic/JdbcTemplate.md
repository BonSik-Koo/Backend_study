__JdbcTemplate이란?__
===========================
- "DB에 접근할수 있도록 java에서 제공하는 API"인 __JDBC__  를 사용할때 DB연동에 필요한 Connection객체, 쿼리 실행을 위한 PreparedStatement객체를 생성하여야 한다. 그리고 퀴리 실행후에는 ResultSet(쿼리 결과를 담는 객체), PreparedStatement, Connection객체들을 close하여야 한다. 이러한 코드들을 데이터 처리와는 관계없지만 JDBC 프로그래밍을 할때 구조적으로 계속 반복적으로 시행하여야한다. 이러한 구조적인 반복을 줄인 기술이 __JDBC Template__ 라이브러리이다.

__Jdbc Template생성__
--------------------------
- Jdbc Template객체를 생성하려면 DataSource의 생성자가 전달되어야 된다.    

__DataSource란__
- DB와 관계된 커넥션 정보를 담고 있으며 스프링이 시작되면 빈으로 등록된다. 
- "property file"을 통해 DateSource를 정의해준다. ->스프링이 시작될때 자동으로 스프링 컨테이너에 빈으로 등록.
![111](https://user-images.githubusercontent.com/96917871/151832304-03ca178f-8737-4166-a693-c6e9a5b471ab.PNG)      

__query() 메소드__
-----------------------
- 여러개의 값을 조회할 때 주로 사용된다. 
- 문법: "List<T> list = jdbcTemplate.query("insert * from 테이블명 where colum명=?", RowMapper<T> rowMapper, 인자);" -> SQL문에서 해당 colum명에서 "?"에 인자로 받은 값을 대입하여 DB에서 찾게된다. 
- 여러개의 반환값을 받기 위해 "RowMapper"의 인터페이스를 사용한다. ->원하는 자료형으로 변환후 "query"메소드의 반환값인 List형태로 반환    

__RowMapper란__
- 검색하는 쿼리문("SELECT ~")에서 원하는 객체의 타입으로 반환받기 위한 인터페이스이다
- 쿼리문에 결과가 "ResultSet"에 담기고 원하는 자료형으로 변환하는 기능인 해당 인터페이스의 "mapRow"메소드를 Override한다.! -> RowMapper객체가 생성되면 해당 메소드를 실행시켜 원하는 자료형으로 변환후 반환
```
List<Member> result = jdbcTemplate.query("select * from MEMBER where id=?", memberRowMapper());
    private RowMapper<Member> memberRowMapper(){
        return new RowMapper<Member>() { //해당 인터페이스는 "mapRow"메소드 하나만 가지고 있다 ->람다식으로 변환이 가능하다.
            @Override
            public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
                Member member = new Member();
                member.setId(rs.getLong("id"));
                member.setName(rs.getString("name"));
                return member;
            }
        };
        /* 람다식
         return (rs, rowNum) -> {
            Member member = new Member();
            member.setId(rs.getLong("id"));
            member.setName(rs.getString("name"));
            return member;
        };
         */
    }
```












