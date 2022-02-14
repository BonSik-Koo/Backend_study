package hello.example.core.scope;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class SingleTonTest {
    @Test
    public void singletonBeanFind() {
        /*
        <참고>
        : "AnnotationConfigApplicationContext"의 parameter가 애초에 component클래스를 받는것이다 그래서 해당 클래스를 "Component"로 등록하지 않아도 자동으로 스프링 빈에 등록된다
           -> ComponentScan이 있어야지 Component로 등록된 것들을 스프링 빈에 등록하지만 "AnnotationConfigAp~"로 스프링 컨테이너가 만들어지면서 ComponentScan이 실행되어  Component로 등록된 객체들이 자동으로 빈으로 등록된다.
         */
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(SingletonBean.class);

        SingletonBean singletonBean1 = ac.getBean(SingletonBean.class);
        SingletonBean singletonBean2 = ac.getBean(SingletonBean.class);

        System.out.println("singletonBean1 = " + singletonBean1);
        System.out.println("singletonBean2 =" + singletonBean2);

        Assertions.assertThat(singletonBean1).isSameAs(singletonBean2);

        ac.close();
    }

    @Scope("singleton") //-> bean으로 등록될때 속성값 -> 기본(defalut)는 "singleton"이다.!!!!
    //@Component
    static class SingletonBean {
        
        @PostConstruct
        public void init() {
            System.out.println("SingletonBean.init");
        }
        
        @PreDestroy
        public void destroy() {
            System.out.println("SingletonBean.destroy");
        }
    }
}
