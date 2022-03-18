package hello.itemservice.web.login;

import hello.itemservice.domain.login.LoginService;
import hello.itemservice.domain.member.Member;
import hello.itemservice.web.SessionConst;
import hello.itemservice.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.Enumeration;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final SessionManager sessionManager;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm form) {
        return "login/loginForm";
    }

    /** Cookie 사용 **/
    //@PostMapping("/login")
    public String loginV1(@Valid @ModelAttribute LoginForm form , BindingResult bindingResult, HttpServletResponse response) {
        if(bindingResult.hasErrors()) { //아이디, 비밀번호 중 하나라도 를 입력 안했을 때
            return "login/loginForm";
        }

        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());
        log.info("login={}" ,loginMember);

        if(loginMember==null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        //로그인 성공 로직
        Cookie cookie = new Cookie("memberId", String.valueOf(loginMember.getId())); //쿠기 생성
        response.addCookie(cookie);

        return "redirect:/";
    }

    /** 직접 구현한 Session 사용 **/
    //@PostMapping("/login")
    public String loginV2(@Valid @ModelAttribute LoginForm form , BindingResult bindingResult, HttpServletResponse response) {
        if(bindingResult.hasErrors()) { //아이디, 비밀번호 중 하나라도 를 입력 안했을 때
            return "login/loginForm";
        }

        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());
        log.info("login={}" ,loginMember);

        if(loginMember==null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        //로그인 성공 로직
        sessionManager.createSession(loginMember, response);

        return "redirect:/";
    }

    /** HttpSession 사용 **/
    //@PostMapping("/login")
    public String loginV3(@Valid @ModelAttribute LoginForm form , BindingResult bindingResult, HttpServletRequest request) {
        if(bindingResult.hasErrors()) { //아이디, 비밀번호 중 하나라도 를 입력 안했을 때
            return "login/loginForm";
        }

        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());
        log.info("login={}" ,loginMember);

        if(loginMember==null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        //로그인 성공 로직

        //request 에서 cookie의 value(UUID)를 key로 가지고 있는 서버의 session이 있으면 해당 session반환 아니면 생성 -> 서버에 각 회원마다 각각의 session이 생성(메모리 사용 증가)
        HttpSession session = request.getSession();

        //세션에 로그인 회원 정보 보관
        //서버 세션 저장소에 sessionId(UUID)와 value(loginMember)를 저장
        //쿠기로 key(SessionConst.LOGIN_MEMBER), value(sessionId)를 만들어 response에 담아줌
        session.setAttribute(SessionConst.LOGIN_MEMBER,loginMember);

        return "redirect:/";
    }

    /** HttpSession 사용 + Login 필터에서 로그인한 후 처음 접속한 URL로 보내주는 기능 **/
    @PostMapping("/login")
    public String loginV4(@Valid @ModelAttribute LoginForm form , BindingResult bindingResult, HttpServletRequest request,
                          @RequestParam(defaultValue = "/") String redirectURL) {

        if(bindingResult.hasErrors()) { //아이디, 비밀번호 중 하나라도 를 입력 안했을 때
            return "login/loginForm";
        }

        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());
        log.info("login={}" ,loginMember);

        if(loginMember==null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        //로그인 성공 로직

        //request 에서 cookie의 value(UUID)를 key로 가지고 있는 서버의 session이 있으면 해당 session반환 아니면 생성 -> 서버에 각 회원마다 각각의 session이 생성(메모리 사용 증가)
        HttpSession session = request.getSession();

        //세션에 로그인 회원 정보 보관
        //서버 세션 저장소에 sessionId(UUID)와 value(loginMember)를 저장
        //쿠기로 key(SessionConst.LOGIN_MEMBER), value(sessionId)를 만들어 response에 담아줌
        session.setAttribute(SessionConst.LOGIN_MEMBER,loginMember);

        return "redirect:"+ redirectURL;
    }

    /** Cookie 사용 **/
    //@PostMapping("/logout")
    public String logoutV1(HttpServletResponse response) {
        Cookie cookie = new Cookie("memberId", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return "redirect:/";
    }

    /** 직접 구현한 Session 사용 **/
   // @PostMapping("/logout")
    public String logoutV2(HttpServletRequest request) {

        sessionManager.expire(request);
        return "redirect:/";
    }

    /** HttpSession 사용 **/
    @PostMapping("/logout")
    public String logoutV3(HttpServletRequest request) {

        //request 에서 cookie의 value(UUID)를 key로 가지고 있는 서버의 session 을 반환, 없을시 생성 하지 않음
        HttpSession session = request.getSession(false);
        if(session !=null ) { //session 이 있으면
            session.invalidate(); //session에 있는 모든 값들을 삭제 한다.
            //session.removeAttribute(SessionConst.LOGIN_MEMBER); //session에 있는 해당 name의 값을 삭제한다.
        }

        return "redirect:/";
    }
}
