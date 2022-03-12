__HttpSession__
====================

- request.getSession() -> 해당 쿠키의 value인 id를 가지는 세션을 반환(서버에 있는), 없으면 서버에서 생성

- sesson.setAttribute(name, value) -> 위에서 하나뿐인 id를 가지고 있는 세션내에 map형태로 key, value로 지정한 값들을 저장

- session.invalidate() -> id를 가지는 세션내에 모든 key,value를 삭제 / 위에서 지정한 key의 해당하는 value만 삭제하는 것도 찾아보기

- 그래서 homeController에서 getSession했을때 해당 id를 가진 세션(안에는 여러개의 key,value)이 없으면 그냥 리턴한것 , 
  그리고 getAttribute를 통해 해당 id를 가진 세션안에 지정한 key(사용자가 정의한 상수) 의 value(member)를 가져온다
