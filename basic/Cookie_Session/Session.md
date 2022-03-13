__Session 이란__
==========================
- 사이트의 여러 페이지에 걸쳐 사용되는 사용자 정보를 저장하는 방법을 의미합니다.      
-> 로그인 유지 등의 사용된다.       
- 쿠키와 다르게 클라이언트의 요청에 따른 정보를 클라이언트 메모리에 저장하는 것이 아닌 웹 서버가 세션 아이디 파일을 만들어 서비스가 돌아가고 있는 서버에 저장을 하는것을 말한다.    
-> 웹 서버의 메모리를 사용하여 정보를 저장하므로 많은 세션은 많은 메모리 사용을 초래한다.       
- 또한 세션을 통해 만든 쿠키는 사용자의 정보와 관련 없는 임의의 값(UUID)을 가지는 쿠기를 웹 브라우저로 전달한다.

__HttpSession__
====================
- 서블릿에서 "HttpSession"를 지원해준다.
- 서블릿을 통해 HttpSession을 생성하면 쿠키의 이름은 "JEESSIONID"이고 값을 추정 불가능한 랜던 값(UUID를 사용한 값)이다.   
-> ex) Cookie: JSESSIONID=5B78E23B513F50164D6FDD8C97B0AD05     
- 기본적으로 "HttpServletRequest"에 아래 해당 메서드를 사용할수 수 있다.

__세션 생성과 조회__
-------------------------
__" HttpSession session = request.getSession(true) "__  
- http request의 쿠키의 value(랜덤값)의 값의 세션을 찾게 된다. 만약 해당 세션을 찾게 되면 해당 세션을 반환하고 찾지 못하게 되면 해당 value의 세션을 생성해서 반환하게 된다.         
- 기본적으로 request.getSession()의 default값은 "true"이다.

__" HttpSession session = request.getSession(false) "__            
- 위와 대부분 같지만 만약 해당 세션을 찾지 못하게 되면 세션을 생성하지 않고 null을 반환한다.


__세션에 클라이언트 정보 보관__
-----------------------------------
__" session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember) "__     
- 기존에 있는 세션을 가져오거나 새로 만든 세션에 map형태로 key는 "SessionConst.LOGIN_MEMBER"(임의로 지정해둔 상수 값) 이고 value는 "loginMember" 사용자 정보를 담아 둔다.
- 하나의 세션에 여러개의 key, value를 넣어둘 수 있다.


__세션에 있는 정보 가져오기__
---------------------------------
__" Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER) "__      
- 클라이언트의 쿠키의 value를 이용하여 해당 세션을 가져온 뒤 세션내에 지정한 key를 사용하여 value를 가져 올수 있다.
- 세션에 정보를 저장하거나 가져올 때 "Object"타입으로 저장하고 가져와지기 때문에 캐스팅을 해야된다.

__세션에 있는 정보 삭제하기__
----------------------------
__" session.invalidate() "__    
- 해당 세션내에 있는 정보(key,value)를 모두 삭제한다.

__" session.removeAttribute(SessionConst.LOGIN_MEMBER) " __      
- 해당 세션내에 지정한 key의 값을 삭제한다.


