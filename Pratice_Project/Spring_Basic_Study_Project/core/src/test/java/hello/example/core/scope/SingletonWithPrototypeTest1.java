package hello.example.core.scope;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Provider;

public class SingletonWithPrototypeTest1 {

    @Test
    void prototypeFind() {

        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class);

        PrototypeBean prototypeBean1 = ac.getBean(PrototypeBean.class);
        prototypeBean1.addConut();
        Assertions.assertThat(prototypeBean1.getCount()).isEqualTo(1);

        PrototypeBean prototypeBean2 = ac.getBean(PrototypeBean.class);
        prototypeBean2.addConut();
        Assertions.assertThat(prototypeBean2.getCount()).isEqualTo(1);
    }

    @Test //싱글톤 내에 프로토타입 빈을 객체로 가지고 있는 경우
    void singletonClientUsePrototype() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(ClientBean.class, PrototypeBean.class);

        ClientBean clientBean1 = ac.getBean(ClientBean.class);
        int count1 = clientBean1.logic();
        Assertions.assertThat(count1).isEqualTo(1);

        ClientBean clientBean2 = ac.getBean(ClientBean.class);
        int count2 = clientBean2.logic();
        Assertions.assertThat(count2).isEqualTo(2);
    }

    @Test //실글톤 내에 새로운 포로토타입의 빈을 생성해야 할경우
    void providerTest() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(ClientBean.class, PrototypeBean.class);

        ClientBean clientBean1 = ac.getBean(ClientBean.class);
        int count1 = clientBean1.logic();
        Assertions.assertThat(count1).isEqualTo(1);

        ClientBean clientBean2 = ac.getBean(ClientBean.class);
        int count2 = clientBean2.logic();
        Assertions.assertThat(count2).isEqualTo(1);
    }

    static class ClientBean {

//        <test2>
//        private final PrototypeBean prototypeBean;
//        @Autowired // 생성자가 하나니 생략가능
//        public ClientBean(PrototypeBean prototypeBean) {
//            this.prototypeBean = prototypeBean;
//        }

//        <스프링 컨테이너를 통해서 직접 빈을 가져오는 방식>
//        @Autowired
//        private ApplicationContext ac; //항상 새로운 프로토타입을 가져와야 하는경우니 스프링 컨테이너객체를 가져와 필요할때 마다 스프링 컨테이너에 요청한다.


//        <스프링의 기능중 "ObjectProvider"기능사용 방식>
//        @Autowired
//        private ObjectProvider<PrototypeBean> prototypeBeansProvider ;

//        <자바표준 라이브러리의 기능중 " provider"기능사용 방식>
          @Autowired
          private Provider<PrototypeBean> provider;

        public int logic() {
//          <스프링 컨테이너를 통해서 직접 빈을 가져오는 방식>
//          prototypeBean prototypeBean = ac.getBean(PrototypeBean.class);

//          <스프링의 기능중 "ObjectProvider"기능사용 방식>
//          PrototypeBean prototypeBean = prototypeBeansProvider.getObject()

//          <자바표준 라이브러리의 기능중 " provider"기능사용 방식>
            PrototypeBean prototypeBean = provider.get();


            prototypeBean.addConut();
            return prototypeBean.getCount();
        }
    }
    @Scope("prototype")
    static class PrototypeBean {

        private int count =0;

        public void addConut() {
            count++;
        }
        public int getCount() {
            return count;
        }
        @PostConstruct
        public void init() {
            System.out.println("PrototypeBean.inint " + this);
        }
        @PreDestroy
        public void destroy() {
            System.out.println("PrototypeBean.destroy");
        }
    }
}
