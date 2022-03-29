- 스프링 부트가 제공하는 BasicErrorController 는 HTML 페이지를 제공하는 경우에는 매우 편리하다. 4xx, 5xx 등등 모두 잘 처리해준다. 그런데 API 오류 처리는 다른 차원의 이야기이다. . API 마다, 각각의 컨트롤러나 예외마다 서로 다른 응답 결과를 출력해야 할 수도 있다.
그렇기 때문에 HTML화면을 처리할때는 BasicErrorController를 사용하고, API 오류 처리는 따로 처리해야할 필요가 있다.
----------------------------------

__API 예외 처리 HandlerExceptionResolver__
==================================
- 스프링 MVC는 컨트롤러(핸들러) 밖으로 예외가 던져진 경우 예외를 해결하고, 동작을 새로 정의할 수 있는 방법을 제공한다. 컨트롤러 밖으로 던져진 예외를 해결하고, 동작 방식을 변경하고 싶으면 HandlerExceptionResolver 를 사용하면 된다. 줄여서 ExceptionResolver 라 한다.
- 예외가 발생하게 해서 서블릿을 넘어 WAS로 예외가 전달되게 되면 HTTP상태코드는 예외와 상관없이 500으로 처리된다. 따라서 해당 ExceptionResolver를 사용하게 되면 예외에 따라서 400,404등 다른 상태코드로 처리할 수 있다.

---------------------------------------------------------------
![image](https://user-images.githubusercontent.com/96917871/160661135-43e19b38-2565-4f02-bfec-2b425a69ea7f.png)


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
