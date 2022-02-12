package hello.example.core.order;

import hello.example.core.discount.DiscountPolicy;
import hello.example.core.member.Member;
import hello.example.core.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component //컴포넌트 스캔 방식
public class OrderServiceImpl implements OrderService {

    //참고 -> "final"은 생성자는 처음 초기화에서 선언하게 되면 값을 바꿀수 없다.
    private final MemberRepository memberRepository ;
    private final DiscountPolicy discountPolicy ;

    /* "@Qualifier 어노테이션 사용예시 -> 같은 타입의 빈이 2개 이상있을때 "@Qualifier"로 지정한 이름을 조회해서 일치하는 이름의 빈을 가져온다!
    @Autowired //컴포넌트 스캔 방식 -> 생성자가 하나만 있으면 생략이 가능하다!!!. -> 생성자 주입 -->이걸 권장한다.!!
    public OrderServiceImpl(MemberRepository memberRepository, @Qualifier("DiscountPolicy") DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }
    */
    @Autowired //컴포넌트 스캔 방식 -> 생성자가 하나만 있으면 생략이 가능하다!!!. -> 생성자 주입 -->이걸 권장한다.!!
    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }

    /* setter 주입 기존와 마찬가지로 스프링 빈에 모두등록하고 난 후 의존관계주입시 Autowired 어노테이션으로 지정된것을 호출하여 자동 의존관계 주입한다.
    @Autowired
    public void setMemberRepository(MemberRepository memberRepository ){
        this.memberRepository = memberRepository;
    }
    @Autowired
    public void setDiscountPolicy(DiscountPolicy discountPolicy){
        this.discountPolicy = discountPolicy;
    }*/

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(member, itemPrice);

        return new Order(memberId, itemName, itemPrice, discountPrice);
    }

    //테스트 용도
    public MemberRepository getMemberRepository() {
        return this.memberRepository;
    }
}
