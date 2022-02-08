package hello.hellospring;

import hello.hellospring.AOP.TimeTraceAop;
import hello.hellospring.repository.JpaMemberRepository;
import hello.hellospring.repository.MemberRepository;
import hello.hellospring.service.MemberService;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.persistence.EntityManager;

@Configuration
public class SpringConfig {

    /*Jdbc , Jdbc Template
    private DataSource dataSource;
    @Autowired
    public SpringConfig(DataSource dataSource) {
        this.dataSource= dataSource;
    }
    */

    // Jpa
    private EntityManager em;
    @Autowired
    public SpringConfig(EntityManager em) {
        this.em = em;
    }

    /*SpringDataJpa ->스프링 데이터가 자동으로 구현한 구현체를 스프링 빈으로 등록한 MemberRepository 를 가져온다.
    private final MemberRepository memberRepository;
    @Autowired
    public SpringConfig(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
    */

    /* configuation으로 bean으로 등록하는 방법
    @Bean
    public TimeTraceAop timeTraceAop() {
        return new TimeTraceAop();
    }
     */

    @Bean //스프링 컨테이너에 스프링 빈으로 등록한다.
    public MemberService memberService() {
        return new MemberService(memberRepository()); //"memberRepository()" 메소드를 호출하여 필요한 객체를 스프링빈에 등록하고 가져온다.
    }

    @Bean
    public MemberRepository memberRepository() {
        //return new MemoryMemberRepository();
        //return new JdbcMemberRepository(dataSource);
        //return new JdbcTemplateMemberRepository(dataSource);
        return new JpaMemberRepository(em);
    }

}
