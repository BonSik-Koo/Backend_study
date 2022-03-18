package hello.itemservice.web.argumentresolver;

import hello.itemservice.domain.member.Member;
import hello.itemservice.web.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
