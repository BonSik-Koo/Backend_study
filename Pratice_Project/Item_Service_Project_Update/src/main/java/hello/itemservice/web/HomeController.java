package hello.itemservice.web;

import hello.itemservice.domain.member.Member;
import hello.itemservice.domain.member.MemberRepository;
import hello.itemservice.web.argumentresolver.Login;
import hello.itemservice.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@Slf4j
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;
    private final SessionManager sessionManager;

//    @GetMapping("/")
//    public String home() {
//        return "home";
//    }

    /** 쿠기를 사용한 home **/
    //@GetMapping("/")
    public String homeLoginV1(@CookieValue(value = "memberId",required = false) Long memberId, Model model) {
        //로그인하지 않은 사용자 화면
        if(memberId ==null) {
            return "home";
        }

        Member loginMember = memberRepository.findById(memberId);
        if(loginMember==null){
            return "home";
        }

        //로그인 한 사용자 화면
        model.addAttribute("member", loginMember);
        return "loginHome";
    }

    /** 직접구현한 session 을 사용한 home **/
    //@GetMapping("/")
    public String homeLoginV2(HttpServletRequest request, Model model) {
        Member member = (Member) sessionManager.getSession(request);

        //로그인하지 않은 사용자 화면
        if(member ==null) {
            return "home";
        }

        //로그인 한 사용자 화면
        model.addAttribute("member", member);
        return "loginHome";
    }

    /** Httpsession 을 사용한 home **/
    //@GetMapping("/")
    public String homeLoginV3(HttpServletRequest request, Model model) {

        //세션이 없으면 home
        //로그인 하지 않은 사용자가 들어왔는데 굳이 세션을 만들 필요없다 -> 메모리를 사용해야되는 것이기 때문에(세션을 만들려면)
        HttpSession session = request.getSession(false);
        if(session ==null) { //로그인 하지 않은 사용자
            return "home";
        }

        //서버의 session 저장소에 key가 cookie의 value인 sessionId(UUID로 생성된)이고 해당 value(member)를 반환
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        //세션에 회원 데이터가 없으면 home
        if(loginMember==null) {
            return "home";
        }

        //세션이 유지되면 로그인으로 이동
        model.addAttribute("member", loginMember);
        return "loginHome";
    }

    /** @SessionAttribute 를 사용한 home!! -> 새로운 세션을 만들어주지 않기 때문에 로그인 한 사용자의 한에서만 사용하기, 해당 애노테이션에서는 session을 만들어주지 않는다. **/
    //@GetMapping("/")
    public String homeLoginV4(@SessionAttribute(name="loginMember", required = false) Member loginMember , Model model) {

        //세션에 회원 데이터가 없으면 home
        if(loginMember==null) {
            return "home";
        }

        //세션이 유지되면 로그인으로 이동
        model.addAttribute("member", loginMember);
        return "loginHome";
    }


    /** 사용자가 구현한 "ArgumentResolver"를 추가시켜 사용자가 직접 구현한 애노테이션으로 동작하게 하기
     * 사용자가 지정한 "ArgumentResolver"가 동작하게 되고 구현한 메소드에서 모두 정상적인 값이면  "resolveArgument" 메소드에서 "Member"가 반환되게 된다.
     **/
    @GetMapping("/")
    public String homeLoginV5(@Login Member loginMember , Model model) {

        //세션에 회원 데이터가 없으면 home
        if(loginMember==null) {
            return "home";
        }

        //세션이 유지되면 로그인으로 이동
        model.addAttribute("member", loginMember);
        return "loginHome";
    }
}
