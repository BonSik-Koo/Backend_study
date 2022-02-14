package hello.example.core;

import hello.example.core.member.Grade;
import hello.example.core.member.Member;
import hello.example.core.member.MemberService;
import hello.example.core.member.MemberServiceImpl;
import hello.example.core.order.Order;
import hello.example.core.order.OrderService;
import hello.example.core.order.OrderServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class OrderApp {

    public static void main(String [] args) {

        ApplicationContext ac = new AnnotationConfigApplicationContext(appconfig.class);
        MemberService memberService = ac.getBean("memberService", MemberService.class);
        OrderService orderService = ac.getBean("orderService", OrderService.class);

        Long memberId = 1L;
        Member member = new Member(memberId, "member" , Grade.VIP);
        memberService.join(member);

        Order order = orderService.createOrder(memberId, "itemA", 20000);

        System.out.println("order="+ order);
        System.out.println("order.calculatePrce =" + order.calculatePrice());

    }
}
