__서블릿 필터__
==========================
- 애플리케이션 여러 로직에서 웹과 관련된 여러 공통 관심사항을 해결할때 사용되는 기능이다.    
-> 공통 관심사항이라 AOP로 처리할수 있지만 URL과 관련된 공통관심사항을 처리할 때는 서블릿 필터, 스프링 인터셉터를 사용하는 것이 좋다.      

![image](https://user-images.githubusercontent.com/96917871/158068206-03b1c55b-65c4-4b6a-9c9d-22a9d17b0755.png)      
- 필터는 서블릿이 지원하는 수문장과 같은 역할을 한다.
- 여기서 서블릿은 스프링의 디스패처 서블릿이고 WAS(톰캣)에서 필터를 거쳐 통과된 경우에만 서블릿을 호출하고 컨트롤러를 호출할 수 있다.

- 필터는 체인으로 구성되는데, 여러 필터를 넣을수 있다. 사용자가 정의한 우선순의 대로도 필터를 적용할 수 있다.

__서블릿 필터 사용__
----------------------------
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
-> 스프링부트가 올라올때 WAS도 같이 올리는데 그렇게 되면 WAS를 통해 http요청시 등록한 필터를 거치고 서블릿으로 이동하게 된다.

- setFilter(new LogFilter()) : 등록할 필터를 지정한다.     
- setOrder(1) : 필터는 체인으로 동작한다. 따라서 순서가 필요하다. 낮을 수록 먼저 동작한다.
- addUrlPatterns("/*") : 필터를 적용할 URL 패턴을 지정한다. 한번에 여러 패턴을 지정할 수 있다. -> 여기선 모든 URL를 대상으로








