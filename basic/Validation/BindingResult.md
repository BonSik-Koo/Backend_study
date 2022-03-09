__< BindlingResult >__
=====================================================
- 스프링이 제공하는 검증 오류를 보관하는 객체이다. 바인딩 오류, 검증 오류가 발생하면 여기에 보관하게 된다 
- "BindlingResult"는 검증할 대상 바로 다음에 위치 해야된다. 순서가 중요하다!!! -> 예를 들어 "@ModelAttribute Item item" 바로 뒤에 "BindlingResult bin~"이 위치해야된다.!!
- BindingResult 가 없으면 : "@ModelAttribute"의 필드타입과 다른 타입의 값이 들어오게 되면 웹 브라우저에 "400오류"가 발생하게 된다. 
-> 바인딩 오류!!             
--> 즉 요청한 URL과 전달하는 파라미터의 정보가 맞지 않아 컨트롤러가 호출되지 않는것이다!!!      

- BindingResult 가 있으면 :  다른 필드 타입의 오류 정보들을 스프링이 자동으로 "FieldError"을 생성해서(ObjectName,field, 오류메시지 등을 포함해서) "BindlingResult"에 넣어준다.            
-> 타입에러일 경우에 스프링이 자동적으로 FieldError를 생성해서 BindlingResult에 담아준다. 여기서 에러코드는 "typeMismatch", "TypeMIsmatch.Item.price"등 자세한 에러코드는 뒤쪽에서, 즉 스프링이 타입에러가 발생하면 자동으로 담아준다.!!!!!!



__FieldError_
-------------------------
- BindlingResult에 담는 객체중 하나로써 ObjectError의 자식 인터페이스 이다.
- 특정 필드의 오류의 관하여 사용하는 인터페이스
- ObjectError는 바인딩 될때 발생하는 기본적인 오류를 담을수 있지만 해당 인터페이스는 사용자가 추가적인 오류를 담을수 있다.    

__생성자 1) new FieldError(String objectName, String field, String defaultMessage)__
- objectName : 오류가 발생한 객체 이름 /field : 오류 필드/ defaultMessage : 기본 오류 메시지
- ObjectName으로 지정한 이름으로 Model에 자동적으로 저장된다.     
-> 타임리프에서 "th:error"를 사용하여 Model에 있는 필드의 오류 메시지를 사용할수 있다.
--> th:error는 해당 모델의 필드에 오류가 있는 경우에 메시지를 태크로 출력시키는 기능.   
- 해당 생성자를 사용하여 사용자가 폼에 잘못된 타입의 필드를 입력하여 오류가 발생했을 경우 폼에 사용자가 입력한 값이 유지 되지 않는다.    
-> "th:field의 또다른 기능에 해당하며 이로인해 "fieldError"에 보관한 값을 사용하여 출력하는데 해당 생성자는 그 값을 가지고 있지 않기 때문이다.       

__#th:field"의 또다른 기능__      
- th:field="*{price}"타임리프의 th:field 는 매우 똑똑하게 동작하는데, 정상 상황에는 모델 객체의 값을 사용하지만, 오류가 발생하면 FieldError 에서 보관한 값을 사용해서 값을 출력한다.

__생성자 2) new FieldError(String objectName, String field, @Nullable Object rejectedValue, boolean bindingFailure, @Nullable String[] codes, @Nullable Object[] arguments, @Nullable String defaultMessage)__
- rejectedValue : 사용자가 입력한 값(거절된 값)/ bindingFailure : 타입 오류 같은 바인딩 실패인지, 검증 실패인지 구분 값/ codes : 메시지 코드/ arguments : 메시지에서 사용하는 인자/ defaultMessage : 기본 오류 메시지
- 위의 "생성자1"와 대부분 동일한 기능을 하지만 "rejectedValue"의 파라미터로 인해 사용자가 폼을 통해 전달한 값을 보관할 수 있다.      
-> 사용자가 폼에 입력한 값을 유지핟도록 할 수 있다.


__ObjectError__
------------------------
- 특정 필드를 넘어선 오류가 있을 때 사용하는 인터페이스 -> 글로벌 오류 처리
- 타임리프에서 예) th:if="${#fields.hasGlobalErrors()}" / th:each="err : ${#fields.globalErrors()}" 처럼 접근하여 글로벌 오류 메시지를 출력한다.


