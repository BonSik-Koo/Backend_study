package hello.itemservice.web.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static hello.itemservice.web.SessionConst.LOGIN_MEMBER;

/** 서블릿 필터 사용 - 로그인 확인 기능의 필터 **/
@Slf4j
public class LoginCheckFilter implements Filter {

    //검사를 하지않을 URL!
    private static final String[] whitelist = {"/", "/members/add", "/login", "logout", "/css/*"};

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        HttpServletResponse httpResponse = (HttpServletResponse) response;

        try { //서블릿, 컨트롤러 호출하기 전!!!!!
            log.info("인증 체크 필터 시작 {}", requestURI);

            if(isLoginCheckPath(requestURI)) {
                log.info("인증 체크 로직 실행 {}",requestURI);

                HttpSession session = httpRequest.getSession();
                //로그인 하지 않은 사용자
                if(session==null || session.getAttribute(LOGIN_MEMBER)==null) {
                    log.info("미인증 사용자 요청 {}",requestURI);

                    /**
                     * 로그인으로 redirect -> response "redirect"의 기능의 메소드 사용
                     * 추가로 사용자가 들어온 URL을 쿼리파라미터로 넘기므로써 사용자가 login 한 후 처음 들어온 URL로 보내주기 위해서 -> 파라미터로 넘김
                     */
                    httpResponse.sendRedirect("/login?redirectURL=" + requestURI);

                    return; //여기가 중요, 미인증 사용자는 다음으로 진행하지 않고 끝!!! -> 서블릿도 호출 하지 않는다.!!
                }
            }

            chain.doFilter(request, response);
        }catch(Exception e) {
            throw e;
        }finally { //서블릿, 컨트롤러 , 뷰 호출을 마친 후!!!!
            log.info("인증 체크 필터 종료 {}", requestURI);
        }
    }

    /**
     * 화이트 리스트의 경우 인증 체크X
     */
    private boolean isLoginCheckPath(String requestURI) {
        return !PatternMatchUtils.simpleMatch(whitelist, requestURI); //검사해야할 URI일 경우 true
    }

}
