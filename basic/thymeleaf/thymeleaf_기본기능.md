__<텍스트-text, utext>__
==========================
- 타임리프틑 기본적으로 HTML 테그의 속성에 기능을 정의해서 동작한다.
- HTML의 콘텐츠(content)에 데이터를 출력할 때는 다음과 같이 "th:text" 를 사용하면 된다 -> ex) <span th:text="${data}">
- HtML 콘텐츠 영역안(테크의 속성에하는것이 아니라)에서 직접 데이터를 출력하고 싶으면 다음과 같이 "[[...]]"를 사용하면 된다 -> ex) [[${data}]]

__HTML엔티티__
-------------------
- 웹 브라우저는 < 를 HTML 테그의 시작으로 인식한다. 따라서 < 를 테그의 시작이 아니라 문자로 표현할 수있는 방법이 필요한데, 이것을 HTML 엔티티라 한다. 그리고 이렇게 HTML에서 사용하는 특수 문자를 HTML 엔티티로 변경하는 것을 "이스케이프(escape)"라 한다. 그리고 타임리프가 제공하는 th:text ,[[...]] 는 기본적으로 이스케이스(escape)를 제공한다. -> 타임리프를 통해서 인위적으로 "<~~~>"로 테그를 지정할때 렌더링 되는 과정에서 이스케이프로 인해 '<' -> &lt, '>' -> &gt 로 바뀌게 된다.
- 이스케이프 기능을 사용하지 않으려면 "th:text -> th:utext" , "[[...]] -> [(...)]" 로 사용하면 된다.    

__<변수-SpringEL>__
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


__<기본 객체들>__
========================
- 타임리프는 기본 객체들을 제공한다.(${#request}, ${#response}, ${#session}, ${#servletContext}, ${#locale})
- 하지만 #request 는 HttpServletRequest객체를 그대로 가져오기 때문에 데이터를 조회하려면 request.getParameter("data") 처럼 불편하게 접근해야함 -> 아래 편의 객체도 제공          

- HTTP 요청 파라미터 접근: param -> 예) ${param.paramData}
- HTTP 세션 접근: session -> 예) ${session.sessionData}
- 스프링 빈 접근: @ -> 예) ${@helloBean.hello('Spring!')} :스프링 빈의 이름이 'helloBean'

__<URL 링크>__
=================================
- 타임리프의 URL을 생성할 때는 "@{}"문법을 사용하면 된다.  

__(1) 단순한 URL__       
- @{/hello} -> /hello

__(2) 쿼리 파라미터__
- @{/hello(param1=${param1}, param2=${param2})} -> /hello?param1=data1&param2=data2
- () 에 있는 부분은 쿼리 파라미터로 처리

__(3) 경로변수(PathVarialbe)__
- @{/hello/{param1}/{param2}(param1=${param1}, param2=${param2})} -> /hello/data1/data2
- URL 경로상에 변수가 있으면 () 부분은 경로 변수로 처리된다

__(4) 경로 변수 + 쿼리 파라미터__
- @{/hello/{param1}(param1=${param1}, param2=${param2})} -> /hello/data1?param2=data2
- ()에 경로변수의 파라미터가 있으면 경로변수로 처리, 없다면 쿼리 파라미터로 처리됨


__<리터럴>__
=====================
- 리터럴은 소스 코드상에 고정된 값을 의미("hell", 10,20 등)
- 타임리프에서 문자 리터럴은 항상 ' (작은 따옴표)로 감싸야 한다. -> <span th:text="'hello'">
- 공백없이 쪽 이어진다면 ''을 생략해도 된다 -> 주의!) <span th:text="hello world!"></span> -> 중간에 공백이 있어서 하나의 의미있는 토큰으로도 인식되지 않는다. --> ''을 씌어야 된다.
- "리터럴 대체(|~|)"를 사용해서 사용하지 않을수 있다 -> <span th:text="|hello ${data}|">

__<속성(Attribute)>__
=======================
- "th:*" 속성을 지정하면 타임리프는 기존 속성을 th:* 로 지정한 속성으로 대체한다.(기존에 정의된 속성을 없애고 타임리프로 지정한 속성이 채택). 기존 속성이 없다면 새로 만든다.    
- ex) input type="text" name="mock" th:name="userA" -> 타임리프를 통해서 뷰템플릿이 랜더링 된 후 -> input type="text" name="userA"

__<반복>__
=============
- 타임리프에서 "th:each"를 사용하여 Model에서 객체를 하나씩 꺼내며 반복할수 있다.
- <tr th:each="user : ${users}"> -> 반복시 오른쪽 컬렉션( ${users} )의 값을 하나씩 꺼내서 왼쪽 변수( user )에 담아준다 


