__정적 컨텐츠__
------------------------
- 서버에서 파일을 변환시키지 않고 그대로 파일을 웹브라우저에게 전달하는 컨텐츠
- 모든 사용자의 웹페이지에 동일한 콘텐츠가 보기에 된다.
- 정적컨텐츠에는 HTML, CSS, 이미지, 비디오와 같은 파일이 포함된다.

예)
-Spring boot에는 정적 컨텐츠 기능이 내포되어 있다. -> static파일에서 자동으로 찾게 된다.
![1111](https://user-images.githubusercontent.com/96917871/151362193-8647ded1-2625-46e8-9eb1-b37f78180a2e.PNG)

① 스프링 부트에 가장 밑단에 있는 톰켓서버이 URL을 받게 된다.
![image](https://user-images.githubusercontent.com/96917871/151364475-971538e8-3162-48d5-8075-5584e7fb3a24.png)

② 받은 URL을 Spring에 넘겨주게 된다.
③ Spring 내부에서 먼저 Controller가 전달받은 URL의 hello-static.html의 Controller가 있는지 찾는다.
③ 해당 컨트롤러가 없으면 Spring boot는 resources/static에서 "hello-static.html"을 찾게 되고 웹 브라우저로 반환해준다.

__MVC와 템플릿 엔진__
-------------------------
- Model, Controller, View



