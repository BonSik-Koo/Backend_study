package hello.example.core.beanfind;


import hello.example.core.appconfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ApplicationContextInfoTest {

    ApplicationContext ac = new AnnotationConfigApplicationContext(appconfig.class);

    @Test
    @DisplayName("스프링 빈 모두출력하기")
    public void findAllBean() {
        String[] str = ac.getBeanDefinitionNames(); //스프링으로 등록된 이름 모두 가져오기

        for(String beanName : str) {
            Object bean = ac.getBean(beanName); //스프링 빈의 이름만 지정하고 타입은 지정하지 않았으니 Object 타입이다.
            System.out.println("name=" + beanName +" object=" + bean);
        }
    }

    @Test
    @DisplayName("애플리케이션 빈 출력하기")
    public void findApplicationBean() {
        String[] str = ac.getBeanDefinitionNames();
        /*
        for(String beanName : str) {
            BeanDefinition beanDefinition = ac.getBeanDefinition(beanName); //스프링 컨테이너의 메타정보를 가져온다

             //Role ROLE_APPLICATION: 직접 등록한 애플리케이션 빈
             //Role ROLE_INFRASTRUCTURE: 스프링이 내부에서 사용하는 빈
            if(beanDefinition.getRole() == BeanDefinition.ROLE_APPLICATION) {
                Object bean = ac.getBean(beanName); //스프링 빈의 이름만 지정하고 타입은 지정하지 않았으니 Object 타입이다.
                System.out.println("name=" + beanName +" object=" + bean);
            }
        }
         */
    }

}
