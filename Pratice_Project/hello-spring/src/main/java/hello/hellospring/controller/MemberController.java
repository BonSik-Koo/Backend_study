package hello.hellospring.controller;


import hello.hellospring.domain.Member;
import hello.hellospring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller //스프링이 시작될때 스프링 컨테이너가 생성되는데 "@Controller"라고 지정한 controller들을 생성하고 스프링 컨테이너에 등록하게 된다. ->컴포넌트 스캔방식
public class MemberController {

    private final MemberService memberService;

    @Autowired // controller 가 생성될때 스피링 컨테이너에 있는 스프링 빈으로 지정한 memberService를 가져오게 하는 기능.
    public MemberController(MemberService memberService) {  // ->"DI"방식중 생성자 주입을 사용
        this.memberService = memberService;
    }

    @GetMapping("/members/new")
    public String createForm() {
        return "members/creatMemberForm";
    }

    @PostMapping("/members/new") // "key->name", "value->입력한 문자", post 방식을 포함한 "Form" 태그로 지정한 URL로 전송한것을 받는다.
    public String create(MemberForm form){ //해당 "Member"클래스의 key와 일치하는 이름에!!!! value를 저장한다.!!!!! -> 예)String name 이ㄴ라해도 저장됨
        Member member = new Member();
        member.setName(form.getName());

        /* 내가 해본거 - 중복된 아이디를 입력했을때
        try {
            memberService.join(member);
            return "redirect:/";
        }catch (IllegalStateException e) {
            return "hello";
        }
         */
        memberService.join(member);
        return "redirect:/"; //redirect의 오른쪽 URL로 요청한다!!!!!! -> 여기서 home URL이다
    }

    @GetMapping("/members")
    public String list(Model model) {
        List<Member> members = memberService.findMember();
        model.addAttribute("members", members);
        return "members/memberList";
    }







}
