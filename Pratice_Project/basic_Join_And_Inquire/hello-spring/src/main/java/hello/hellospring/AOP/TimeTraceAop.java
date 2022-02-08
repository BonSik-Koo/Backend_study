package hello.hellospring.AOP;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect //AOP로 사용할수 있는 어노테이션
@Component //spring bean으로 등록하기
public class TimeTraceAop {


    @Around("execution(* hello.hellospring.service..*(..))") //AOP로 어디 적용할껀지 정한다. -> 현재는 service파일내에 있는 클래스 전부
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {

        long start = System.currentTimeMillis();
        System.out.println("START: " + joinPoint.toString());

        try {
            return joinPoint.proceed(); //다음 메소드로 진행된다 -> 실제 메소드로 실행된다.
        }finally {
            long finish = System.currentTimeMillis();
            long timeMs = finish - start;

            System.out.println("END: " + joinPoint.toString() + " " +timeMs +"ms");
        }
    }
}





