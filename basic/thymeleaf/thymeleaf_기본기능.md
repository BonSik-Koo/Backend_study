__텍스트-text, utext__
==========================
- 타임리프틑 기본적으로 HTML 테그의 속성에 기능을 정의해서 동작한다.
- HTML의 콘텐츠(content)에 데이터를 출력할 때는 다음과 같이 "th:text" 를 사용하면 된다 -> ex) <span th:text="${data}">
- HtML 콘텐츠 영역안(테크의 속성에하는것이 아니라)에서 직접 데이터를 출력하고 싶으면 다음과 같이 "[[...]]"를 사용하면 된다 -> ex) [[${data}]]

__HTML엔티티__
-------------------
- 웹 브라우저는 < 를 HTML 테그의 시작으로 인식한다. 따라서 < 를 테그의 시작이 아니라 문자로 표현할 수있는 방법이 필요한데, 이것을 HTML 엔티티라 한다. 그리고 이렇게 HTML에서 사용하는 특수 문자를 HTML 엔티티로 변경하는 것을 "이스케이프(escape)"라 한다. 그리고 타임리프가 제공하는 th:text ,[[...]] 는 기본적으로 이스케이스(escape)를 제공한다. -> 타임리프를 통해서 인위적으로 "<~~~>"로 테그를 지정할때 렌더링 되는 과정에서 이스케이프로 인해 '<' -> &lt, '>' -> &gt 로 바뀌게 된다.
- 이스케이프 기능을 사용하지 않으려면 "th:text -> th:utext" , "[[...]] -> [(...)]" 로 사용하면 된다.    

__변수-SpringEL__
=====================
- 타임리프에서 변수를 사용할때 아래 변수 표현식을 사용한다.                 
 
__(1)Object__         
- user.username : user의 username을 프로퍼티 접근(자동) -> user.getUsername()
- user['username']
- user.getUsername()

__(2)List__             
- users[0].username : List에서 첫 번째 회원을 찾고 username 프로퍼티 접근 -> list.get(0).getUsername()
- users[0]['username']
- users[0].getUsername() :

__(3)Map__                
- userMap['userA'].username : Map에서 userA를 찾고, username 프로퍼티 접근 map.get("userA").getUsername()
- userMap['userA']['username']
- userMap['uesrA'].getUsername()
