package hello.servlet.web.springmvc.v3;

import hello.servlet.domain.member.Member;
import hello.servlet.domain.member.MemberRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/springmvc/v3/members")
public class SpringMemberControllerV3 {

    MemberRepository memberRepository = MemberRepository.getInstance();

    /**
     * 직접 parameter를 받을수도 있고 Model(view로 전달할 데이터를 저장하는곳)도 받을수 있다.!!!!!!!!!!!
     * 직접 return을 통해 view의 위치를 리턴하면 DespatcherController(프론트 컨트롤러)가 해당 뷰 리졸버를 통해 view의 위치를 알아내고 view의 jsp를 호출하게 되는데
     * 직접 전달받은 Model에 넣었기 때문에 "프론트 컨트롤러"가 jsp에 "model"을 전송할수 있게 된다.!!!!!!
     */

    //두 개의 의미가 같은 것이다!!!! -> method를 지정하게 되면 다른 메서드로는 호출할수 없게 된다!!!!! --> 이것이 좋은 설계!!!
    @GetMapping("/new-form") // == @RequestMapping(value = "/new-form", method= RequestMethod.GET)
    public String newForm() {
        return "new-form";
    }

    @PostMapping("/save") // == @RequestMapping(value="/save",  method= RequestMethod.POST)
    public String save(
            @RequestParam("username") String username,
            @RequestParam("age") int age, //string 이지만 사용자가 원하는 자료형으로 받아올수 있다!!!!!
            Model model
            )
    {
        Member member = new Member(username, age);
        memberRepository.save(member);

        model.addAttribute("member", member);
        return "save-result";
    }

    @GetMapping // == @RequestMapping(value = "/new-form", method= RequestMethod.GET)
    public String members(Model model) {
        List<Member> members = memberRepository.findAll();

        model.addAttribute("members", members);
        return "members";
    }
}
