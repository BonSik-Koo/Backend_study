package hello.servlet.web.springmvc.v1;


import hello.servlet.domain.member.Member;
import hello.servlet.domain.member.MemberRepository;
import hello.servlet.web.frontcontroller.ModelView;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
public class SpringMemberSaveControllerV1 {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @RequestMapping("/springmvc/v1/members/save")
    public ModelAndView process(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));

        Member member = new Member(username, age);
        memberRepository.save(member);

        /**
         * -ModelAndView-
         * 직접만들었던 ModelView와 같은 기능으로 springMVC 에서 "view"에 지정한 문자열을 뷰 리졸버가 물리경로로 바꾸게 된다.
         * 그리고 frontController인 "DespatcherController"가 뷰 리졸버로 부터 받은 물리경로로 (jsp가 있는) Model을 넘기게 되고 view인 jsp에서 동적으로 html을 바꾸어 웹 브라우저로 전송!!!!!!
         */
        ModelAndView mv = new ModelAndView("save-result");
        mv.addObject("member", member);
        return mv;
    }
}
