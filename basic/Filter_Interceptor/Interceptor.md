__Interceptor__
=======================
![image](https://user-images.githubusercontent.com/96917871/158424196-d67438e7-0de6-45cf-b75a-f873f3803ef0.png)

- 스프링 인터셉터는 스프링 MVC가 제공하는 기술이다.
- 스프링 인터셉터는 디스패처 서블릿과 컨트롤러 사이에서 컨트롤러 호출 직전에 호출 된다. -> 서블릿 필터는 디스패츠 서블릿 호출 전
- 스프링 인터셉터에도 URL 패턴을 적용할 수 있는데, 서블릿 URL 패턴과는 다르고, 매우 정밀하게 설정할 수 있다.(포함할 URL, 제외할 URL등)
- 서블릿 필트와 마친가지로 "체인"의 기능도 가지고 있다.
- 서블릿 필터와 다르게 스프링 인터셉터는 컨트롤러 호출전(preHandle, 더 정확히는 핸들러 어댑터 호출전), 호출후(postHandel, 컨트롤러 호출후 ModelAndView를 디스패처 서블릿에 반환 후), 요청 완료 이후(afterCompletion, 뷰 템플릿 렌더링을 마친 후) 와 같이 단계적으로 잘 세분화 되어 있다.        
-> 서블릿 필터의 경우 단순히 request , response 만 제공했지만, 인터셉터는 어떤 컨트롤러(handler)가 호출되는지 호출 정보도 받을 수 있다. 그리고 어떤 modelAndView 가 반환되는지 응답 정보도 받을 수 있다.

__스프링 인터셉터 호출 흐름(정상적인 흐름, 예외 상황 흐름)__
=============================

__<정상적인 흐름>__
------------------------------
![image](https://user-images.githubusercontent.com/96917871/158425115-b92f0484-3b91-48f9-bb1c-6a7bd2b514f1.png)

- 스프링 인터셉터에는 3가지 기능(메소드) 있다. -> preHandle, postHandle, afterCompletion
- preHandle : 컨트롤러 호출 전에 호출된다. (더 정확히는 핸들러 어댑터 호출 전에 호출된다.)     
-> preHandle 의 응답값이 true 이면 다음으로 진행하고, false 이면 더는 진행하지 않는다. false 인 경우 나머지 인터셉터는 물론이고, 핸들러 어댑터도 호출되지 않는다. 그림에서 1번에서 끝이 나버린다.
- postHandle : 컨트롤러 호출 후에 호출된다. (더 정확히는 핸들러 어댑터 호출 후에 호출된다.)
- afterCompletion : 뷰가 렌더링 된 이후에 호출된다.

__<예외 상황 흐름>
----------------------------------------------
![image](https://user-images.githubusercontent.com/96917871/158425719-7d84a26b-86c8-4ba8-98cb-607b6b0a99eb.png)

- preHandle : 컨트롤러 호출 전에 호출된다. -> 예외 상황이 컨트롤러에서 발생하기 때문에 상관없이 호출
- postHandle : 컨트롤러에서 예외가 발생하면 postHandle 은 호출되지 않는다.!!!!!!
- afterCompletion : afterCompletion 은 항상 호출된다.      
-> 해당 메소드에서 예외(Exception)을 받는 파라미터가 있는데 컨트롤러에서 어떤 예외가 발생했는지 확인할 수 있다.      
-->예외 발생시 postHadle는 호출되지 않지만 "afterCompletion"은 항상 호출 되기 때문에 예외와 무관하게 공통 처리를 하려면 해당 메소드를 사용하면 된다.


__스프링 인터셉터 사용1 - Log 출력__
=================================
```
/** 스프링 인터셉트 사용 - Log 남기는 기능의 필터 **/
@Slf4j
public class LogInterceptor implements HandlerInterceptor {

    public static final String LOG_ID = "logId";

    /** 컨트롤러 호출전 (정확힌 핸들러 어댑터 호출전) **/
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        String uuid = UUID.randomUUID().toString();
        request.setAttribute(LOG_ID, uuid);

        log.info("REQUEST [{}] [{}] [{}]", uuid, requestURI, handler);
        return true; //false 진행x
    }

    /** 핸들러(컨트롤러)호출후  **/
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("postHandel [{}]" ,modelAndView);
    }

    /** 뷰 렌더링 마친 후  **/
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String requestURI = request.getRequestURI();
        Object logId = request.getAttribute(LOG_ID);

        log.info("REQUEST [{}] [{}]", logId, requestURI);
        if(ex!=null) {
            log.error("afterCompletion error!!", ex);
        }
    }
}
```
- 스프링 인터셉터를 사용하려면 "HandlerInterceptor"의 인터페이스를 구현해야된다. -> 필요한 메소드를 오버라이딩 하여 사용하면 된다.

- request.setAttribute(LOG_ID, uuid)   
-> 서블릿 필터의 경우 지역변수로 해결이 가능하지만, 스프링 인터셉터는 호출 시점이 완전히 분리되어 있다. 따라서 preHandle 에서 지정한 값을 postHandle , afterCompletion 에서 함께 사용하려면 어딘가에 담아두어야 한다. LogInterceptor 도 싱글톤 처럼 사용되기 때문에 맴버변수를 사용하면 위험하다. 따라서 request 에 담아두었다. 이 값은 afterCompletion 에서 request.getAttribute(LOG_ID) 로 찾아서 사용한다.

- return true -> true 면 정상 호출이다. 다음 인터셉터나 컨트롤러가 호출된다.

- "preHandle"메소드에서 "Handler"파라미터를 받아올 수 있다.     
-> 핸들러 목록조회(핸들러 매핑- @controller,@RequestMapping 을 통한 컨트롤러 탐색)를 통해 컨트롤러를 찾게 되고 해당 핸들러 어댑터를 호출하기전에 스프링 인터셉터가 호출되기 때문에 컨트롤러 객체를 받아올 수 있다.  

-------------------------------------------
```
 @Configuration
public class WebConfig implements WebMvcConfigurer {
 @Override
 public void addInterceptors(InterceptorRegistry registry) {
 registry.addInterceptor(new LogInterceptor())
 .order(1)
 .addPathPatterns("/**")
 .excludePathPatterns("/css/**", "/*.ico", "/error");
 }
 //...
}
```
- 인터셉터를 등록하는 코드
- WebMvcConfigurer 가 제공하는 addInterceptors() 를 사용해서 인터셉터를 등록할 수 있다. -> 스프링이 제공하는 방식. 
- registry.addInterceptor(new LogInterceptor()) : 인터셉터를 등록한다.
- order(1) : 인터셉터의 호출 순서를 지정한다. 낮을 수록 먼저 호출된다.
- addPathPatterns("/**") : 인터셉터를 적용할 URL 패턴을 지정한다. -> 서블릿 필터와 URL 패턴과  다르다!.
- excludePathPatterns("/css/**", "/*.ico", "/error") : 인터셉터에서 제외할 패턴을 지정한다.     
-> 필터와 비교해보면 인터셉터는 addPathPatterns , excludePathPatterns 로 매우 정밀하게 URL 패턴을 지정할 수 있다.






