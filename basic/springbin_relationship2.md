__스프링 컨테이너 생성__
-------------------------------
```
@Configuration
public class appconfig {

    @Bean
    public MemberService memberService() {
        return new MemberServiceImpl(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }

    @Bean
    public OrderService orderService() {
        return new OrderServiceImpl(memberRepository(),discountPolicy());
    }

    @Bean
    public DiscountPolicy discountPolicy() {
        return new RateDiscountPolicy();
    }
}
```
- "ApplicationContext"를 스프링 컨테이너라고 한다. -> 인터페이스이며 구현체로 "AnnotionConfigApplicationContext", "GenericXmlApplicationContext"등이 있다.
- 위의 appconfing에 사용했던 방식은 애노테이션 기반의 자바 설정 클래스로 스프링 컨테이너를 만든 것이다. 
- __ApplicationContext ac = new AnnotationConfigApplicationContext(appconfig.class)__ : appconfig가 스프링 컨테이노로 등록되고 해당 클래스내에 "@Bean"으로 선언되어있는 메소드를 호출하여 
스프링빈의 이름은 해당 메소드의 이름으로 스프링 빈의 객체는 new로 생성한 객체를 등록하게 된다. -> 해당 객체의 타입은 메소드에서 지정한 리턴 타입이 된다.(하지만 리턴 타입으로도 스프링 빈에서 꺼낼수
있고 스프링 빈의 객체 타입으로도 꺼낼수 있다. 

__스프링 컨테이너 생성과정__
-----------------------------------
![221111](https://user-images.githubusercontent.com/96917871/153236239-90c86a60-43ac-4c23-bf35-37125c400497.PNG)

__스프링 빈 의존관계 설정__
-----------------------------
- 스프링 컨테이너는 설정 정보를 참고해서 의존관계를 주입(DI)한다.
- 스프링 빈으로 등록할때 기존에 존재하는 빈이 있다면 등록한 빈을 가져와 주입한다. -> 싱글톤 컨테이너에서 상세히 설명
![33](https://user-images.githubusercontent.com/96917871/153237104-23367854-07d5-4766-9c3b-e85d7e87922f.PNG)      

※참고     
- 스프링은 빈을 생성하고, 의존관계를 주입하는 단계로 나누어져있다. 그런데 순수한 자바 코드로 스프링 빈에 등록하게 되면 생성자를 호출하면서 의존관계가 있는 객체도 생성하기 때문에 의존관계 주입도 한번에 처리된다.


__싱글톤 패턴__
-----------------------------
- 클래스의 객체가 딱 1개만 생성되는 것을 보장하는 디자인 패턴이다.
- 여러 클라이언트가 서비스를 요청할때 서비스 객체를 하나만 생성하여 공유하는것이다 -> 클라이언트의 수만큼 객체를 생성하지 않으니 메모리 낭비를 막을수 있다.

<자바 싱글톤 패턴 예제>
```
public class SingleTonService {

    //해당 클래스를 생성하면 딱 하나로 자기자신을 참조하는 객체를 생성하여 가지고있는다. static로 선언하여 해당 클래스를 생성해도 해당 static변수는 하나만 생성되어 있다.
    private static final SingleTonService singletonservice = new SingleTonService();

    public static SingleTonService getInstance() {return singletonservice;}

    private SingleTonService() {} //외부에서 클래스 객체를 생성하지 못하도록 한다.
}
```
```
 @Test
    @DisplayName("스프링 없이 싱글톤 패턴을 적용한 객체 사용")
    public void singleServiceTest() {

        SingleTonService singleTonService1 = SingleTonService.getInstance();

        SingleTonService singleTonService2 = SingleTonService.getInstance();

        System.out.println("SingleTonService1: " + singleTonService1);
        System.out.println("SingleTonService2: " + singleTonService2);

        //Assertions.assertThat(singleTonService1).isEqualTo(singleTonService2);
        Assertions.assertThat(singleTonService1).isSameAs(singleTonService2);

    }
```    

__※싱글톤 패턴문제점__
- 싱글톤을 구현하는 코드의 양이 많아진다.
- 의존관계상 클라이언트가 구체 클래스에 의존한다. DIP를 위반한다. (DIP:추상화의 의존해야지 구체화에 의존하면 안된다)
- 클라이언트가 구체 클래스에 의존해서 OCP 원칙을 위반할 가능성이 높다.
- 내부 속성을 변경하거나 초기화 하기 어렵다.
- 유연성이 떨어진다.

__싱글톤 컨테이너__
--------------------------
- 스프링 컨테이너는 싱글톤 패턴의 문제를 해결하면서 객체를 싱글톤으로 생성하고 관리한다.
- 스프링 컨테이너는 싱글턴 패턴을 적용하지 않아도, 객체 인스턴스를 싱글톤으로 관리한다. -> 처음 자바의 코드를 보면 "memberservice", "orderservice"메소드를 호출할 때 모두 "memberRepository"를
호출하여 각각 생성하는것 처럼보이지만 하나의 "memberRepository"를 주입한다 --> "@Configuration" 어노테이션 때문! 아래에서 더 자세히 설명

<싱글톤 컨테이너 적용 전>
![2222](https://user-images.githubusercontent.com/96917871/153240158-0cdcea42-aedf-4dfd-915d-edaed9054ebd.PNG)

<싱글톤 컨테이너 적용 후>
![1](https://user-images.githubusercontent.com/96917871/153239994-ea3d832e-bcc7-49ae-b68e-332a37ab7b6b.PNG)

__싱글톤 방식의 주의점__
----------------------------
- 싱글톤 패턴이든, 싱글톤 컨테이너를 사용하든, 객체 인스턴스를 하나만 생성해서 여러 클라이언트에게 공유하는 방식이기 때문에 싱글톤 객체는 유지(stateful)하게 설계되면 안된다.
- 무상태(stateless)하게 설계되어야된다 -> 특정 클라이언트가 싱글톤 객체의 필드의 값을 변경할수 있으면 안된다. 읽기만 가능하게 하여야 한다. 
- 스프링 빈의 필드에 공유값을 설정하면 클라이언트가 공유값을 변경하게 되고 다른 클라이언트가 정상적이지 않은 값을 가져가게 된다 -> 전달받은 값을 싱글톤 객체 필드에 저장하는것이 아니라 읽기만 해야된다.

__"@Configuration"과 싱글톤, 바이트코드 조작__
--------------------------------
- 처음 코드에서 스프링 빈으로 등록할때 memberservice, orderservice에서 memberRepository를 각각 따로 호출하는것 처럼보인다. 하지만 하나의 memberRepository를 사용하는데 그 이유는 "@Configuration" 어노테이션 때문이다.
- 스프링 빈으로 "appconfig"또한 등록되는데 여기서 "@Configuration"에 의해 "CGLIB"라는 바이트코드 조작 라이브러리를 사용하여 appconfig클래스를 상속받은 임의의 다른 클래스를 만들고 그 클래스를 스프링빈의 객체로 등록하게 하는 것이다.
![44](https://user-images.githubusercontent.com/96917871/153243636-90ed0421-f00d-41c0-9183-d86ed52ef8f4.PNG)
- CGLIB로 인해 만들어진 클래스는 "@Bean"이 붙은 메소드를 호출마다 스프링 빈에 등록된 빈이면 존재하는 빈을 반환하고, 스프링빈에 없으면 생성하여 스프릉 빈에 등록하게 된다. ->싱글톤이 보장된다.
- "appconfig"를 "@Configuration"으로 선언하지 않고 "@Bean"만 사용하게 되면 스프링 빈으로는 등록되지만 싱글톤이 보장되지 않게 되고 빈으로 등록할때 필요한 빈들을 새로 계속 생성하게 된다. -> 스프링 컨테이너가 관리하는 것이 아니게 된다.

