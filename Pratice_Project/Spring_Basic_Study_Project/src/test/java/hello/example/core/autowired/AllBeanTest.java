package hello.example.core.autowired;

import hello.example.core.AutoAppConfig;
import hello.example.core.discount.AllDiscountService;
import hello.example.core.member.Grade;
import hello.example.core.member.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

//"AllDiscountService" test
public class AllBeanTest {

    @Test
    public void findAllBean() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(AutoAppConfig.class);

        AllDiscountService allDiscountService = ac.getBean(AllDiscountService.class);
        Member member = new Member(1L, "useA", Grade.VIP);
        int discountPrice1 = allDiscountService.discount(member, 10000, "fixDiscountPolicy");


        Assertions.assertThat(discountPrice1).isEqualTo(1000);

        int discountPrice2 = allDiscountService.discount(member, 20000, "rateDiscountPolicy");
        Assertions.assertThat(discountPrice2).isEqualTo(2000);
    }


}
