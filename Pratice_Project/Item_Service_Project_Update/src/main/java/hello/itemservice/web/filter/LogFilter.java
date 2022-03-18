package hello.itemservice.web.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

/** 서블릿 filter 사용 - Log 남기는 기능의 필터**/
@Slf4j
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
            chain.doFilter(request, response); //다음 필터가 있으면 다음 필터 적용, 없으면 서블릿 호출 !!!!!!!

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
