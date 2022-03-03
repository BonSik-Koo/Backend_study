__<타임리프와 스프링 통합>__
=============================
- 타임리프는 스프링 없이도 동작하지만, 스프링과 통합이 되면서 추가 기능도 제공
- 타임리프를 사용하려면 뷰를 랜더림하기 위한 "타임리프 템플릿 엔진"을 스프링 빈에 등록해야하고, 상대경로를 절대경로로 바꾸어주는 "타임리프용 뷰 리졸버"를 스프링 빈에 등록해야 된다 -> 스프링 부트에서는 이러한 것을 자동화 해준다!!

__<입력 폼 처리>__
========================
- 타임리프가 제공해주는 추가 기능인 입력 폼의 추가 기능    
- < form action="item.html" th:action th:Object="${item}" method="post" > -> "th:Object"로 Model에서 전달받은 객체를 가져온다.         
- < input type="text" th:field="*{itemName}" /> -> *{...} : 선택 변수 식이라고 한다. th:object 에서 선택한 객체에 접근한다.(=${item.itemName}) --> th:field :HTML 태그의 id , name , value 속성을 자동으로 생성해준다!!!!!    
- id, name, value를 하나하나 지정해주는 방식을 사용하지 않아도 된다.

__<폼 컴포넌트 기능(checkbox, radio button, List등을 편리하게 사용하는 기능)>__
========================================================

__체크 박스(checkbox>-단일__
------------------------
- < input type="checkbox" id="open" name="open" class="form-check-input" > -> checkbox 예시
- 체크 박스기능으로 인해 체크하게 되면 체크 박스를 체크하면 HTML Form에서 open=on 이라는 값이 넘어간다. 스프링은 on 이라는 문자를
true 타입으로 변환해준다. -> 즉 "true"라는 데이터를 받을수 있다
- 하지만 HTML에서 체크 박스를 선택하지 않고 폼을 전송하면 open 이라는 필드 자체가 서버로 전송되지 않는다. -> 해당 변수 자체가 전송되지 않는다!
- 서버쪽으로 값이 전달되지 않으면 상황에 따라 문제가 발생할 수 있다.!!
- 이를 해결 하기 위해 __히든 필드__ 를 사용한다.

__[히든필드]__     
- < input type="hidden" name="_open" value="on" />  -> 체크박스 이름(open)에 '_'붙히면 히든 필드 생성
- 체크 박스를 체크하면 스프링 MVC가 open 에 값이 있는 것을 확인하고 사용한다. 이때 _open 은 무시한다.
- 체크 박스를 체크하지 않으면 스프링 MVC가 _open 만 있는 것을 확인하고, open 의 값이 체크되지 않았다고 인식한다. 이 경우 서버에서 Boolean 타입을 찍어보면 결과가 null 이 아니라 false 인 것을 확인할 수 있다.


__[타임리프는 "th:field"를 사용해서 자동 히든필드 적용]__     
- < input type="checkbox" id="open" th:field="${item.open}" disabled > -> 히든 필드 자동 적용
- 앞서 폼 type=text 에서 "th:field"를 사용할때 name, id, value를 자동 생성해주었는데 type=checkbox 일때는 name, id, value, checked, 히든 필드 까지 자동 생성해준다. ->여기서 id는 "th:field=${item.open}"이라면 "open1"로 된다. -> 반복문(<th:each>)이면 open2,open3...
- 폼으로 보낼때는 th:field 를 사용해서 체크를 한다면 "name"의 값에 true를 넣어 전송 -> 타임리프가 히든 필드를 만들어 null값은 안보내지게 만든다.(false를 보내게)
- 폼으로 보내는 경우가 아닌 경우에 th:field를 사용해서 "value"의 값이 "name"의 값에 있으면 타임리프가 "checked"를 자동으로 만들어서 체크를 해준다.!!!!!!!!             

__체크 박스_다중__
---------------------------
-
-
-



__ridio 버튼__
-------------------------
- 라디오 버튼은 멀티 체크박스와 다르게 하나만 선택이 가능하다.
- < input type="radio" th:field="*{itemType}" th:value="${type.name()}" >             
-> type은 열거형으로 정의됨                  
--> 반복되는 경우 "field"로 인해서 "id"는 itemType1,itemType2..로 생성되고 "name"은 itemType으로 생성된다.        
---> 하나를 체크하게 되면 "item.itemType"에 그떄의 th:value의 값이 들어가게 된다.          

- 체크 박스와 다르게 입력 폼에서 사용될때는 "히든필드"는 생성되지 않는다. -> 체크박스에서는 하나이상을 선택하고 수정시에 모두 체크를 해제하면 아무런 값이 넘거가지 않는데 --> 라디오 버튼은 하나를 체크하고 수정시에 반드시 체크를 하도록 되어있기 때문에 별도의 히든 필드는 필요없다!(체크후 수정시 null값이 넘어갈수 없다!!!)
- 체크박스와 마찬가지로 폼으로 보내는 경우가 아닌경우 th:value의 값이 th:field로 만들어진 "id" 값에 있으면 타임리프가 "checked"를 만들어 자동 체크해준다.!!


__셀렉트(select) 박스__
-----------------------------------------
- 셀렉트 박스는 여러 선택지 중에 하나를 선택할 때 사용할 수 있다.
```
<select th:field="*{deliveryCode}" class="form-select">
    <option value="">==배송 방식 선택==</option>
    <option th:each="deliveryCode : ${deliveryCodes}" th:value="${deliveryCode.code}" th:text="${deliveryCode.displayName}">FAST</option>
 </select>
```
- 입력 폼에서 th:field로 인해 생경난 "id"에 th:value로 생성한 값을 넣어서 폼으로 보내주게 된다.
- 입력 하는 폼이 아닌 경우에 th:value 값들 중 "id"와 같은 값이 있는 곳에 타임리프가 "selectd"를 만들어 자동 선택되게 만들어준다.


