__정적 컨텐츠__
------------------------
- 서버에서 파일을 변환시키지 않고 그대로 파일을 웹브라우저에게 전달하는 컨텐츠
- 모든 사용자의 웹페이지에 동일한 콘텐츠가 보기에 된다.
- 정적컨텐츠에는 HTML, CSS, 이미지, 비디오와 같은 파일이 포함된다.
- 사용하는 이유? : 처음 사이트에 접속하는 경우등 모든 사용자에게 같은 웹페이지를 보여줘야되는 상황에서 사용된다.  

예)
-Spring boot에는 정적 컨텐츠 기능이 내포되어 있다. -> static파일에서 자동으로 찾게 된다.     
![1111](https://user-images.githubusercontent.com/96917871/151364656-a5aa55a6-b250-406e-b49b-71132edda34a.PNG)      

① 스프링 부트에 가장 밑단에 있는 톰켓서버이 URL을 받게 된다.       
![222](https://user-images.githubusercontent.com/96917871/151364587-8c62d571-95f4-40f0-ae8a-08e6a1dd4b80.PNG)     
② 받은 URL을 Spring에 넘겨주게 된다.     
③ Spring 내부에서 먼저 Controller가 전달받은 URL의 hello-static.html의 Controller가 있는지 찾는다.      
③ 해당 컨트롤러가 없으면 Spring boot는 resources/static에서 "hello-static.html"을 찾게 되고 웹 브라우저로 반환해준다.      
![333](https://user-images.githubusercontent.com/96917871/151364889-ae09786f-d0bb-46a6-b6ef-19b8a544cd48.PNG)      


__MVC와 템플릿 엔진__
-------------------------
__MVC란?__
- 애플리케이션을 Mode, Controller, view의 역할로 나누어 개발하는 방식    
< Model >    
- 데이터 관련 로직 담당   
- controller와 소통   
- DB와 소통  

< View >
- 사용자가 보는 화면 (HTHL/CSS로 구성)  
- Controller와 소통    
- 템플릿 엔진이 있음   

< Controller >   
- Model, View를 이어주는 다리   
- Model로 부터 데이터를 받는다.   
- Request를 처리한다. (GET,POST 등)   
- 데이터를 View로 전달한다.   

__템플릿 엔진이란?__
- 템플릿 양식과 특정 데이터 모델에 따른 입력 데이터를 합성하여 결과 문서를 출력하는 소프트웨어.     
- 즉, html 파일을 브라우저로 그냥 보내주는 것이 아닌, 서버에서 프로그래밍을 통해 동적으로 바꾸어서 보내주는 역할을 뜻한다.     

예1) URL로 파라메터를 받지 않는 경우    
![44444](https://user-images.githubusercontent.com/96917871/151367815-ea8f09be-22ad-4d98-8654-b4bff04ee428.PNG)    
① 웹  브라우저에서 URL를 입력한다.     
![5555](https://user-images.githubusercontent.com/96917871/151368014-71385e02-4318-4c0d-8e07-572bb6c3e15a.PNG)    
② 스프링 부트의 내장 톰켓 서버는 받는 URL를 스프링에 넘기게 된다.     

③ 스프링내부에 Controller는 "hello" controller가 있는지 찾게 되고. 있다면 해당 메소드를 실행시킨다. 그 후 "hello"을 스프링에 반환하게 된다.      
```
@Controller //Spring Framwork에서 Controller라는 걸 인지시켜준다.    
public class HelloController {     

    @GetMapping("hello") //URL에서 hello가 있으면 get하고 아래 메서드 실행 -> get요청의 API    
    public String hello(Model model) { //Spring boot가 model을 만들어 같이 넘겨준다.     
        model.addAttribute("data","Spring!!"); //model에 key가 "data"고 value가 "Spring!!"으로 넣는다.   
        return "hello";    
    }   
}   
```  
※GetMapping    
- 스프링 부트에서 HTTP GET요청을 처리하는 메서드를 맵핑하는 어노테이션이다.   
- 즉 GET요청을 하는 API의 어노테이션이고 데이터를 가져올 때 사용한다.      
  
④ 스프링내 "뷰 리졸버"가 "hello".Html로 바꾸게 되고 해당하는 Html의 View을 찾아주고 템플릿 엔진을 연결시켜준다.
⑤ 템플릿 엔진이 해당 Html을 변형시켜 웹 브라우저로 전송시킨다.
```
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">  <!--템플릿 엔진인(thymeleaf)가 html을 요구에 맞게 바꿔주는 것이다.-->

<head>
    <title>Hello</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>

<body>
<p th:text="'안녕하세요. ' + ${data}" >안녕하세요. 손님</p>
</body>

</html>
```

  




