package study.springdatajpa.controller;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.springdatajpa.entity.Member;
import study.springdatajpa.respository.SpringDataJPA.MemberRepository;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    //도메인 클래스 컨버터 사용 전
    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id) {
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    //도메인 클래스 컨버터 사용 후
    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable("id") Member member){
        return member.getUsername();
    }

    //페이징
    @GetMapping("/members")
    public Page<MemberDto> page (Pageable pageable){

        Page<Member> page = memberRepository.findAll(pageable);
        return page.map(MemberDto::new);
    }

    @Data
    class MemberDto {
        private Long id;
        private String username;

        public MemberDto(Member m){
            this.id = m.getId();
            this.username = m.getUsername();
        }
    }

    //@PostConstruct
    public void init(){
        for(int i=0;i<100;i++)
            memberRepository.save(new Member("user"+i, i));
    }

}
