__Spring AOP란__
===================
- 어떤 로직을 기준으로 공통 관심 사항(핵심 비지니스 로직에 추가적인 기능 -> 메소드 시간 측정등), 핵심 관심 사항(핵심 비지니스 로직)을 나누어 모듈화 시키는 것 -> 재사용 가능

![33333_LI](https://user-images.githubusercontent.com/96917871/152482171-43ff9207-8d81-4133-a776-64b16b0a474f.jpg)
![4444](https://user-images.githubusercontent.com/96917871/152482228-9eb665fe-4506-4333-9aaf-319a313ba643.PNG)

```
@Aspect //AOP로 사용할수 있는 어노테이션
//@Component
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
```
- "@Aspect" : "AOP"사용하기 위해 추가하는 어노테이션
- "@Around" : 타켓으로하는 메소드를 감싸서 특정 advice를 실행한다는 의미 -> 

advice정리, aop나머지 정리

