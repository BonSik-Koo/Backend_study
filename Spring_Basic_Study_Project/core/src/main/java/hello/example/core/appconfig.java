package hello.example.core;

import hello.example.core.discount.DiscountPolicy;
import hello.example.core.discount.RateDiscountPolicy;
import hello.example.core.member.*;
import hello.example.core.order.OrderService;
import hello.example.core.order.OrderServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

//자바 코드를 이용한 사용자 지정 컨테이너 빈 등록하는법
@Configuration
public class appconfig {


    @Bean
    public MemberService memberService() {
        System.out.println("inputinputinputinput");
        return new MemberServiceImpl(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }


    @Bean
    public OrderService orderService() {
        return new OrderServiceImpl(memberRepository(),discountPolicy());
    }

    @Bean
    public DiscountPolicy discountPolicy() {
        return new RateDiscountPolicy();
    }
}
