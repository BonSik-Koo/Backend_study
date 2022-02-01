__JdbcTemplate이란?__
===========================
- "DB에 접근할수 있도록 java에서 제공하는 API"인 __JDBC__  를 사용할때 DB연동에 필요한 Connection객체, 쿼리 실행을 위한 PreparedStatement객체를 생성하여야 한다. 그리고 퀴리 실행후에는 ResultSet(쿼리 결과를 담는 객체), PreparedStatement, Connection객체들을 close하여야 한다. 이러한 코드들을 데이터 처리와는 관계없지만 JDBC 프로그래밍을 할때 구조적으로 계속 반복적으로 시행하여야한다. 이러한 구조적인 반복을 줄인 기술이 __JDBC Template__ 클래스이다.

__Jdbc Template생성__
--------------------------
- Jdbc Template객체를 생성하려면 DataSource의 생성자가 전달되어야 된다.
__DataSource란__
- DB와 관계된 커넥션 정보를 담고 있으며 스프링이 시작되면 빈으로 등록된다. 
- "property file"을 통해 DateSource를 정의해준다. ->스프링이 시작될때 자동으로 스프링 컨테이너에 빈으로 등록.
![111](https://user-images.githubusercontent.com/96917871/151832304-03ca178f-8737-4166-a693-c6e9a5b471ab.PNG)






