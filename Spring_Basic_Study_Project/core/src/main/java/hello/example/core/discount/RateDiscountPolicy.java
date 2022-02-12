package hello.example.core.discount;

import hello.example.core.member.Grade;
import hello.example.core.member.Member;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary //-> 같은 타입의 여러빈이 있을때 해당 어노테이션을 붙혀준 구현체가 우선순위를 가진다. -> 주로 "@Qualifier" 보다 많이 사용된다
//@Qualifier("DiscountPolicy") //->같은 타입의 여러빈이 있을때 추가 구분자를 붙혀 구분하는 방법이다.
public class RateDiscountPolicy implements DiscountPolicy{

    private int discountPercent = 10;

    @Override
    public int discount(Member member, int price) {

        if(member.getGrade() == Grade.VIP) {
            return price * discountPercent /100;
        }else
            return 0;
    }
}
