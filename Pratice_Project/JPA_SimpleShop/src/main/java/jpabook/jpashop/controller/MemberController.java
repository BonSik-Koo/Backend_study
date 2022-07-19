package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(@ModelAttribute MemberForm memberForm) {
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(@Valid @ModelAttribute MemberForm memberForm , BindingResult result) {

        if(result.hasErrors()) {
            return "members/createMemberForm";
        }

        Address address = new Address(memberForm.getCity(), memberForm.getStreet(), memberForm.getZipcode());
        Member member =new Member();
        member.setName(memberForm.getName());
        member.setAddress(address);

        try {
            memberService.join(member);
            return "redirect:/";
        } catch (IllegalStateException e) {
            result.rejectValue("name", "duplicate", "중복된 아이디 입니다.");
            return "members/createMemberForm";
        }
    }

    /**
     * 여기서도 원래는 엔티티를 그대로 보내면 안된다.!! "DTO"로 API 만들기!! -> 여기서는 서버사이드 랜더링 하는거니깐!!
     */
    @GetMapping("/members")
    public String list(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";
    }

}
