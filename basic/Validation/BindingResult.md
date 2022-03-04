__< BindlingResult >__
=====================================================
- 스프링이 제공하는 검증 오류를 보관하는 객체이다. 검증 오류가 발생하면 여기에 보관하게 된다 
- "BindlingResult"는 검증할 대상 바로 다음에 위치 해야된다. 순서가 중요하다!!! -> 예를 들어 "@ModelAttribute Item item" 바로 뒤에 "BindlingResult bin~"이 위치해야된다.!!
- BindingResult 가 없으면 : "@ModelAttribute"의 필드타입과 다른 타입의 값이 들어오게 되면 웹 브라우저에 "400오류"가 발생하게 된다.         
-> 즉 요청한 URL과 전달하는 파라미터의 정보가 맞지 않아 컨트롤러가 호출되지 않는것이다!!!           
- BindingResult 가 있으면 :  다른 필드 타입의 오류 정보들을 스프링이 자동으로 "FieldError"을 생성해서(ObjectName,field등 필요한 파라미터 정보까지) "BindlingResult"에 넣어준다.

__FieldError__

-------------------------

__ObjectError__

------------------------




