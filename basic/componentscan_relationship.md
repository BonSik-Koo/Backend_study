__컴포넌트 스캔__
-----------------------------
- 스프링 빈을 등록할때 자바코드의 @Bean이나 XMA의 <bean>등을 통해서 설정 정보를 직접 스프링 빈에 등록하였다. -> 빈의 개수가 많아 지면 누락하는경우가 발생하고 코드의 길이와 설정정보의 규모도 커진다
- 스프링은 설정정보가 없어도(@Bean으로 작성하여 생성하는) 자동으로 스프링 빈을 등록하는 "컴포넌트 스캔"기능을 제공한다.
- 또한 기존에는 "Bean"으로 등록하면서 의존관계를 주입하였지만 컴포넌트 스캔 방식을 사용할때에는 "@Autowired"를 사용하여 의존관계도 자동으로 주입하는 기능을 가지고 있다.

__생성 및 과정(Component Scan)__
----------------------------
```
@Configuration
@ComponentScan(
 excludeFilters = @Filter(type = FilterType.ANNOTATION, classes = Configuration.class))
public class AutoAppConfig {
}
```
```
@Component
public class MemoryMemberRepository implements MemberRepository {}
```
- @ComponentScan 은 @Component 가 붙은 모든 클래스를 스프링 빈으로 등록한다
- 기존과 다르게 "@bean"으로 등록한 클래스 하나도 없어진다.
- 그리고 기존에 "@Configuration"으로 지정한 "appconfig"가 스프링 빈에 등록된 이유도 "@Configuration" 소스 내에 "@Component" 어노테이션이 붙어 있었기 때문이다.              

![11](https://user-images.githubusercontent.com/96917871/153417259-e3cb1a91-4b5a-4b27-9cba-bd5f50017994.PNG)

__※참고(스프링 부트)__
```
@SpringBootApplication //스프링 부트어플리케이션 어노테이션
						// -> 실행하게 되면여기에서 @componentscan이 있기 때문에 우리가 따로 Componentscan하지 않아도 component로 선언되있는 것을 빈으로 등록한다.!!!
                       // 자동빈, 수동빈와 관련하여 같은 이름의 빈이 있으면 오류를 발생시킨다 -> 스프링 부트의 기능
public class CoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoreApplication.class, args);
	}

}
```

__Autowired__
-------------------------------

```
@Component
public class MemberServiceImpl implements MemberService {
 private final MemberRepository memberRepository;
 @Autowired
 public MemberServiceImpl(MemberRepository memberRepository) {
 this.memberRepository = memberRepository;
 }
```
- "@Bean"을 사용할때에는 설정정보와 의존관계도 직접 표시하였다. 하지만 컨포넌트 스캔을 사용할때에는 "@Autowired" 어노테이션을 사용하여 의존관계를 자동으로 주입해준다.
- 반드시 Autowired는 스프링 빈이 등록된 객체에서만 사용이 가능하다.
- 여러개의 의존관계도 주입받을수 있다.
- 스프링 컨테이너가 자동으로 해당 스프링 빈을 찾아서 주입한다 ->이때 기본 조회 전략은 타입이 같은 빈을 찾아서 주입한다 (ex. MemberService와 MemberServiceImpl은 같은 타입이다 MemberService가 더 상위 타입이다)

![2222222222](https://user-images.githubusercontent.com/96917871/153417948-ea1585db-2487-4bf8-aaa7-0c4628e5bcd6.PNG)    

__탐색 위치와 기본 스캔 대상__
-------------------------------------------
```
@Configuration
@ComponentScan ( //@ComponentScan 은 @Component 가 붙은 모든 클래스를 스프링 빈으로 등록한다
        basePackages = "hello.example.core.member",  //"@Component"로 붙은 걸 찾을 파일을 선택할수 있다. 해당 경로의 하위 파일을 모두 찾게 된다.

        /*
        만약 지정하지 않으면 @ComponentScan 이 붙은 설정 정보 클래스의 패키지가 시작 위치가 된다 -> AutoAppConfig의 패키지인 "hello.example.core"부터 시작

        <권장방법>
        예를 들어서 프로젝트가 다음과 같이 구조가 되어 있으면
        com.hello
        com.hello.serivce
        com.hello.repository

        com.hello 프로젝트 시작 루트, 여기에 AppConfig 같은 메인 설정 정보를 두고,
        @ComponentScan 애노테이션을 붙이고, basePackages 지정은 생략한다
        */
```

__중복 등록과 충돌__
-----------------------------
__1. 자동 빈 등록 vs 자동 빈 등록__
- 이름이 같은 빈이 자동 등록될 시 "ConflictingBeanDefinitionException" 예외 발생
- 스프링 부트 어플리케이션을 실행시킬시 수동이든 자동빈 등록이든 같은 이름의 빈이 있으면 오류를 발생시킨다. -> 스프링부트 기능

__2. 수동 빈 등록 vs 자동 빈 등록__
- 위와 다르게 오류는 발생하지 않고 수동빈등록이 우선권을 가진다 -> 수동빈이 자동빈을 오버라이딩 하여 재정의 한다.
- 마찬가지로 스프링 부트는 이와 같은 이름이 있으면 오류를 발생시켜준다.
