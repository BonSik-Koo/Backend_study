__ArgumentReslover란__
=========================

![image](https://user-images.githubusercontent.com/96917871/159000660-3dbf1bc1-1379-4067-9100-077c7337ca67.png)

- 애노테이션 기반 컨트롤러에서 해당 애노테이션을 처리하는 기능을 한다. (스프링에 약 30개가 넘게 존재)
- 핸들러 어댑터에서 실제 핸들러(컨트롤러)를 호출하는 과정에서 각 애노테이션을 ArgumentResolver가 처리한다.
- HttpServletRequest , Model 은 물론이고, @RequestParam , @ModelAttribute 같은 애노테이션 그리고 @RequestBody , HttpEntity 같은 HTTP 메시지를 처리하는 부분까지 매우 큰 유연함을 보여주었다
- 애노테이션 기반 컨트롤러를 처리하는 "RequestMapping핸들러 어댑터" 는 바로 이 ArgumentResolver 를 호출해서 컨트롤러(핸들러)가 필요로 하는 다양한 파라미터의 값(객체)을 생성한다. 그리고 이렇게 파리미터의 값이 모두 준비되면 컨트롤러를 호출하면서 값을 넘겨준다.      

※참고: 또한 컨트롤러에서 리턴할때(디스패처 서블릿에게) 컨트롤러에서 String으로 뷰 이름을 반환해도, 동작하는 이유가 바로 "ReturnValueHandler 덕분이다.     
-> ArgumentResolver 와 비슷한데, 이것은 응답 값을 변환하고 처리한다.


__<HTTP 메시지 컨버터>__
--------------------------

![image](https://user-images.githubusercontent.com/96917871/159001021-16a390a7-b735-493f-874b-9dcbb56c9aa2.png)

- 애노테이션 중 @RequestBody, HttpEntity와 같이 "message Body"에 있는 것을 처리하는 경우 사용된다. -> "ArgumentResolver"가 사용
- 요청의 경우 @RequestBody 를 처리하는 ArgumentResolver 가 있고, HttpEntity 를 처리하는
rgumentResolver 가 있다. 이 ArgumentResolver 들이 HTTP 메시지 컨버터를 사용해서 필요한
객체를 생성하는 것이다. 





__ArgumentResolver 활용__
===========================
- 기본적으로 컨트롤러에 있는 스프링이 제공해주는 애노테이션을 만나면 스프링내에 기본적인 ArgumentResolver가 동작한다.
- 사용자가 "애노테이션"을 지정하게 되면 해당 애노테이션을 만날 때 사용자가 지정한 아래 "ArgumentResolver"가 동작하게 된다.


__<ArgumentResolver등록>__    
```   
@Configuration //반드시 필요
public class WebConfig implements WebMvcConfigurer {

    /** 인터페이스의 메소드 구현하여 사용(자동으로 스프링 ArgumentResolver 등록) -> "ArgumentResolver"로 사용자가 지정한 "ArgumentResolver"로 등록 **/
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver());
    }
}
```
- "WebMvcConfigurer" -> 스프링이 시작되고 스프링 MVC가 생성되는데 해당 인터페이스는 스프링MVC의 Config(구성)을 사용자가 설정할수 있는 인터페이스 이다. (@Configuration 애노테이션을 함께 사용해야지 적용이 가능)!!!
- 사용자가 정의한 "ArgumentResolver"를 등록하게 된다.


-----------------------------------------
__<사용자 애노테이션 등록>__
```
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Login {
}
```
- @Target(ElementType.PARAMETER) : 파라미터에만 사용
- @Retention(RetentionPolicy.RUNTIME) : 리플렉션 등을 활용할 수 있도록 런타임까지 애노테이션 정보가 남아있음
- ※참고: 사용자가 애노테이션을 선언만 하고 해당 애노테이션을 만났을 경우 동작하는 아래 "ArgumentResolver"를 구현하지 않으면 생략된 것으로 간주한다.

-----------------------------
__<ArgumentResolver 구현>__
```
/**
 * "ArgumentResolver" 기능 구현 -> 각 컨트롤러에서 "사용자가 지정한(@Login)애노테이션"을 만나면 사용자가 지정한 아래 구현한 "ArgumentResolver"가 동작하게 된다.
   --> 내부에 캐시가 있어서 "supportParameter"는 한번만 실행되고 캐시에서 꺼내서 애노테이션을 만날때마다 실행된다.
 * "supportsParameter" -> 전달되는 파라미터의 애노테이션을 확인한다.
 * "resolveArgument" -> 위에서 원하는 애노테이션이라면(반환 값이 true)이면 해당 메소드를 실행
 **/
@Slf4j
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {

        log.info("supportsParameter 실행");

        //해당 애노테이션이 지정한 "Login.class"타입 인지 확인
        boolean hasLoginAnnotation = parameter.hasParameterAnnotation(Login.class);

        //애노테이션의 파라미터 타입이 "Member"타입인지 확인
        boolean hasMemberType = Member.class.isAssignableFrom(parameter.getParameterType());

        //두개다 "true"일 경우에 아래 "resolveArgument" 메소드 실행
        return hasLoginAnnotation && hasMemberType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {


        log.info("resolveArgument 실행");

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        HttpSession session = request.getSession(false);
        if(session==null) {
            return null;
        }

        //해당 key 에 "Member.class"가 들어있기 때문에 Member.class 반환
        return session.getAttribute(SessionConst.LOGIN_MEMBER);
    }
}
```
- "ArgumentResolver" 기능 구현         
-> 각 컨트롤러에서 "사용자가 지정한(@Login)애노테이션"을 만나면 사용자가 구현한 "ArgumentResolver"가 동작하게 된다.               
--> 내부에 캐시가 있어서 "supportParameter"는 한번만 실행되고 캐시에서 꺼내서 애노테이션을 만날때마다 실행된다.  

- "supportsParameter" -> 전달되는 파라미터의 애노테이션을 확인한다.
- "resolveArgument" -> 위에서 원하는 애노테이션이라면(반환 값이 true)이면 해당 메소드를 실행
