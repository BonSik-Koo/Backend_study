package hello.example.core.discount;

import hello.example.core.member.Member;

public interface DiscountPolicy {

    public int discount(Member member, int price);
}
