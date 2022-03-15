__서블릿 필터__
==========================
- 애플리케이션 여러 로직에서 웹과 관련된 여러 공통 관심사항을 해결할때 사용되는 기능이다.    
-> 공통 관심사항이라 AOP로 처리할수 있지만 URL과 관련된 공통관심사항을 처리할 때는 서블릿 필터, 스프링 인터셉터를 사용하는 것이 좋다.      

![image](https://user-images.githubusercontent.com/96917871/158068206-03b1c55b-65c4-4b6a-9c9d-22a9d17b0755.png)      
- 필터는 서블릿이 지원하는 수문장과 같은 역할을 한다.
- 여기서 서블릿은 스프링의 디스패처 서블릿이고 WAS(톰캣)에서 필터를 거쳐 통과된 경우에만 서블릿을 호출하고 컨트롤러를 호출할 수 있다.

- 필터는 체인으로 구성되는데, 여러 필터를 넣을수 있다. 사용자가 정의한 우선순의 대로도 필터를 적용할 수 있다.

__서블릿 필터 사용__
============================
```
@Bean
 public FilterRegistrationBean logFilter() {
 FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
 filterRegistrationBean.setFilter(new LogFilter());
 filterRegistrationBean.setOrder(1);
 filterRegistrationBean.addUrlPatterns("/*");
 return filterRegistrationBean;
 }
```
- 스프링 부트를 사용하는 경우 빈으로 "FilterRegistrationBean" 객체를 생성하여 빈으로 등록하면 된다.
-> 스프링부트가 올라올때 WAS(서블릿 컨테이너)도 같이 올려 서블릿을 사용할수 있게 되고 서블릿 컨테이너가 올라오면서 해당 "서블릿 필터"가 빈으로 등록되어 있으면 서블릿 컨테이너가 필터를 싱글톤 객체로 생성하고 관리한다.         
--> http 요청시 WAS를 통해 서블릿 컨테이너의 필터를 거치고 서블릿(디스패처 서블릿)으로 이동하게 된다.     

- setFilter(new LogFilter()) : 등록할 필터를 지정한다. -> 서블릿에서 제공하는 "Filter" 인터페이스를 구현한 구현체(LogFilter)    
- setOrder(1) : 필터는 체인으로 동작한다. 따라서 순서가 필요하다. 낮을 수록 먼저 동작한다.
- addUrlPatterns("/*") : 필터를 적용할 URL 패턴을 지정한다. 한번에 여러 패턴을 지정할 수 있다. -> 여기선 모든 URL를 대상으로

---------------------------------------------------------
```
public class LogFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("log filter init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        /**
         * ServletRequest 는 HttpServletReqeust 의 부모이기 때문에 다운 캐스팅 하여 사용
         * ServletRequest request 는 HTTP 요청이 아닌 경우까지 고려해서 만든 인터페이스이다.
         */
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        String requestURI = httpRequest.getRequestURI();
        String uuid = UUID.randomUUID().toString();

        try {
            log.info("REQUEST [{}] [{}]", uuid, requestURI);
            chain.doFilter(request, response); //다음 필터가 있으면 다음 필터 적용, 없으면 서블릿 호출 !!!!!!!가장 중요한 부분

        }catch(Exception e) {
            throw e;
        }finally {
            log.info("REQUEST [{}] [{}]",uuid,requestURI);
        }
    }

    @Override
    public void destroy() {
        log.info("log filter destroy");
    }
}
```
- 서블릿에서 제공하는 "Filter" 인터페이스를 구현한 구현체(LogFilter)
- 필터 인터페이스(Filter)를 구현하고 등록하면 서블릿 컨테이너가 필터를 싱글톤 객체로 생성하고, 관리한다.
- init(): 필터 초기화 메서드, 서블릿 컨테이너가 생성될 때 호출된다.
- doFilter(): 고객의 요청이 올 때 마다 해당 메서드가 호출된다. 필터의 로직을 구현하면 된다.
- destroy(): 필터 종료 메서드, 서블릿 컨테이너가 종료될 때 호출된다
- 최근에는 "Filter" 인터페이스의 항목을 모두 구현 하지 않아도 되고 "doFilter()"의 메소드만 구현하면 된다.








