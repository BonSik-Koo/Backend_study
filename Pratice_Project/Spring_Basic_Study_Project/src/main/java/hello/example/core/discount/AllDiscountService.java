package hello.example.core.discount;

import hello.example.core.member.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

//동일한 타입의 객체가 빈으로 2개이상등록되있을시 모두 가져오는거 -> 이런경우는 대부분 클라이언트가 할인정책을 선택할수 있는 상황에 주로 사용된다!!!
//List, Map 사용가능
@Component
public class AllDiscountService {

    private final Map<String, DiscountPolicy> policyMap;
    private final List<DiscountPolicy> policyList;

    @Autowired //map,list를 사용하면 같은 타입의 빈을 모두 가져올수 있다.
    public AllDiscountService(Map<String, DiscountPolicy> policyMap, List<DiscountPolicy> policyList) {
        this.policyMap = policyMap;
        this.policyList = policyList;
    }

    public int discount(Member member, int price, String discountCode) {

        DiscountPolicy discountPolicy = policyMap.get(discountCode);

        System.out.println("discountCode= " + discountCode);
        System.out.println("discountPolicy= " +  discountPolicy);

        return discountPolicy.discount(member, price);
    }


}
