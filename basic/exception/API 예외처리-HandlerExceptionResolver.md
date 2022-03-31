- 스프링 부트가 제공하는 BasicErrorController 는 HTML 페이지를 제공하는 경우에는 매우 편리하다. 4xx, 5xx 등등 모두 잘 처리해준다. 그런데 API 오류 처리는 다른 차원의 이야기이다. . API 마다, 각각의 컨트롤러나 예외마다 서로 다른 응답 결과를 출력해야 할 수도 있다.
그렇기 때문에 HTML화면을 처리할때는 BasicErrorController를 사용하고, API 오류 처리는 따로 처리해야할 필요가 있다.
----------------------------------

__API 예외 처리 HandlerExceptionResolver__
==================================
- 스프링 MVC는 컨트롤러(핸들러) 밖으로 예외가 던져진 경우 예외를 해결하고, 동작을 새로 정의할 수 있는 방법을 제공한다. 컨트롤러 밖으로 던져진 예외를 해결하고, 동작 방식을 변경하고 싶으면 HandlerExceptionResolver 를 사용하면 된다. 줄여서 ExceptionResolver 라 한다.             
-> 이름 그대로 Exception을 해결하는것이 목적이다.      
--> WAS까지 예외를 던지지 않는 것이 핵심이다.!!!! 아래의 2가지 예에서 정상적인 처리를 WAS로 다른 에러코드를 전달하여 BasicController를 호출하는 예와 에러를 처리하여 BasicController를 다시 호출하지않고 http body에 바로 처리하는 예이다.

- 예외가 발생하게 해서 서블릿을 넘어 WAS로 예외가 전달되게 되면 HTTP상태코드는 예외와 상관없이 500으로 처리된다. 따라서 해당 ExceptionResolver를 사용하게 되면 예외에 따라서 400,404등 다른 상태코드로 처리할 수 있다.     

![image](https://user-images.githubusercontent.com/96917871/160661135-43e19b38-2565-4f02-bfec-2b425a69ea7f.png)


__<예1>__
----------------------------
__[HandlerExceptionResolver등록]__
```
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
        resolvers.add(new MyHandlerExceptionResolver());
    }
}
```

__[HadlerExceptionResolver 구현]__
```
@Slf4j
public class MyHandlerExceptionResolver implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        try {
            if(ex instanceof IllegalArgumentException) {
                log.info("IllegalArgumentException", ex);
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
                return new ModelAndView();
            }
        }catch(IOException e) {
            log.error("resolver ex" ,e);
        }
        return null;
    }
}
```
- 컨트롤러에서 예외가 발생하게 되면 지정한 "ExceptionResolver"가 호출되게 된다.
- 반환 값으로 ModelAndView을 반환하게 되는데 해당 유무에 따라 Exception을 사용자가 원하는 예외로 처리해서 정상흐름처럼 변경하거나 기존의 발생한 예외를 그대로 전달하는 역할을 한다.     
-> 이 예에서는 에러를 send하여 다시 WAS에서 BasicController를 호출하여 처리하게 된다.      
--> 전달된 ModelAndView의 형태에 따라 아래와 같이 "DispatcherServlet"이 동작하게 된다.     


» 빈 ModelAndView: new ModelAndView() 처럼 빈 ModelAndView 를 반환하면 뷰를 렌더링 하지 않고, 정상 흐름(사용자가 지정한 처리 방식으로)으로 서블릿이 리턴된다.                     
» ModelAndView 지정: ModelAndView 에 View , Model 등의 정보를 지정해서 반환하면 뷰를 렌더링한다.                 
» null: null 을 반환하면, 다음 ExceptionResolver 를 찾아서 실행한다. 만약 처리할 수 있는 ExceptionResolver 가 없으면 예외 처리가 안되고, 기존에 발생한 예외를 서블릿 밖으로 던진다.          



__<예2>__
---------------------------    
__[UserHandlerExceptionResolver 등록]__
```
@Configuration
public class WebConfig implements WebMvcConfigurer {
    /** ExceptionResolver 등록 **/
    @Override
    public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
        resolvers.add(new MyHandlerExceptionResolver());
        resolvers.add(new UserHandlerExceptionResolver());
    }
}
```

__[UserHandlerExceptionResolver 구현]__
```
@Slf4j
public class UserHandlerExceptionResolver implements HandlerExceptionResolver {

    private final ObjectMapper objectMapper = new ObjectMapper(); //json형태로 바꾸어주는 Jectson 기능

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        try{
            if(ex instanceof UserException) {
                log.info("UserException resolver to 400");
                String acceptHeader = request.getHeader("accept");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

                if (acceptHeader.equals(acceptHeader)) {
                    Map<String, Object> errorResult = new HashMap<>();
                    errorResult.put("ex", ex.getClass());
                    errorResult.put("message", ex.getMessage());
                    String result = objectMapper.writeValueAsString(errorResult);//!!!!!!!!

                    //Http body 에 바로 데이터 넣기
                    response.setContentType("application/json");
                    response.setCharacterEncoding("utf-8");
                    response.getWriter().write(result);

                    return new ModelAndView();
                } else { //text/html 일때
                    return new ModelAndView("error/500");
                }
            }
        }catch(IOException e) {
            log.error("resolver ex" ,e);
        }
        return null;
    }
}
```
- 컨트롤러에서 발생한 예외가 WAS까지 예외를 던지지 않고 "ExceptionResolver"를 사용하여 정상적인 흐름을 유지되는 예이다.    
-> "ExceptionResolver"의 가장 큰 핵심!!!   
--> 스프링 MVC에서 예외 처리를 모두 끝낼 수 있다.

- 예외가 발생했을때 "ExceptionResolver"를 사용하여 직접 HTTP body에 Json형태의 데이터를 넣게 된다.     
-> WAS입장에서 예외가 발생하지 않고 정상적인 흐름으로 인식   

----------------------------------------
__▶ 그런데 직접 ExceptionResolver 를 구현하려고 하니 상당히 복잡하다. 지금부터 스프링이 제공하는 ExceptionResolver 들을 사용하면 된다.__

  
