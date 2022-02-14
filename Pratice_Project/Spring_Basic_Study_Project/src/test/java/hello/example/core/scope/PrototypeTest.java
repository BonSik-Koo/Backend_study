package hello.example.core.scope;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class PrototypeTest {

    @Test
    public void prototypeBeanFind() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(ProtoTypeBean.class);
        ProtoTypeBean protoTypeBean1 = ac.getBean(ProtoTypeBean.class);
        ProtoTypeBean protoTypeBean2 = ac.getBean(ProtoTypeBean.class);

        System.out.println("protoTypeBean1 = " + protoTypeBean1);
        System.out.println("protoTypeBean2 = " + protoTypeBean2);

        Assertions.assertThat(protoTypeBean1).isNotSameAs(protoTypeBean2);

        ac.close();
    }

    //프로토타입을 사용하는경우는 대부분 요청할때마다 새로운 객체를 생성해서 사용해야 할때!!!!
    @Scope("prototype") //->스프링 컨테이너에 해당 빈을 요청 했을때만 객체를 생성하고 의존관계 주입을 마친후 스프링 컨테이너에서 관리 하지 않는다!!!!! 후에 관리 하지 않는것이 핵심
    //@Component
    public static class ProtoTypeBean {

        @PostConstruct
        public void init() {
            System.out.println("PrototypeBean.init");
        }

        @PreDestroy //-> prototype에서 요청시 생성만 하고 반환후 스프링 컨테이너에서 관리하지 않으니 스프링 컨테이너 종료전 자동으로 빈을 반납하지도 않고 해당 메소드도 호출하지 않는다.
        public void destroy() {
            System.out.println("PrototypeBena.destroy");
        }
    }
}
