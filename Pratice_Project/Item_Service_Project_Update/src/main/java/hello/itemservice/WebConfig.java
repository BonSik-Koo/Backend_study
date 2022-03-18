package hello.itemservice;

import hello.itemservice.web.argumentresolver.LoginMemberArgumentResolver;
import hello.itemservice.web.filter.LogFilter;
import hello.itemservice.web.filter.LoginCheckFilter;
import hello.itemservice.web.interceptor.LogInterceptor;
import hello.itemservice.web.interceptor.LoginCheckInterceptor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;
import java.util.List;

@Configuration //반드시 필요
public class WebConfig implements WebMvcConfigurer {

    /** 서블릿 필터 사용 - Log 남기는 기능의 필터 등록 **/
    //@Bean
    public FilterRegistrationBean logFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();

        filterRegistrationBean.setFilter(new LogFilter());
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.addUrlPatterns("/*");

        return filterRegistrationBean;
    }

    /** 서블릿 필터 사용 - 로그인 확인 기능의 필터 등록**/
    //@Bean
    public FilterRegistrationBean loginCheckFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();

        filterRegistrationBean.setFilter(new LoginCheckFilter());
        filterRegistrationBean.setOrder(2);
        filterRegistrationBean.addUrlPatterns("/*");

        return filterRegistrationBean;
    }

    /** 스프링 인터셉터 사용 - 인터페이스의 메소드 구현하여 사용(자동으로 스프링 인터셉터로 등록) - Log 기능의 인터셉터 , 로그인 확인 기능의 인터셉터 **/
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/**") //모든 경로!!!!!
                .excludePathPatterns("/css/**", "/*.ico", "/error");

        registry.addInterceptor(new LoginCheckInterceptor())
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns("/", "/member/add", "/login", "/logout", "/css/**","/*.ico", "/error");
    }


    /** 인터페이스의 메소드 구현하여 사용(자동으로 스프링 ArgumentResolver 등록) -> "ArgumentResolver"로 사용자가 지정한 "ArgumentResolver"로 등록 **/
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver());
    }
}
