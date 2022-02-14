__<빈 Scope란?>__
======================
- 빈이 존재할수 있는 범위를 뜻한다.
- 빈의 메타정보(beanDefinition)중에 있는 하나이다.
- 기본적으로 빈을 등록하게 되면 스코프는 defalut이다 -> 즉 "싱글톤(singleton)"이다.

- 싱글톤: 기본 스코프, 스프링 컨테이너의 시작과 종료까지 유지되는 가장 넓은 범위의 스코프이다
- 프로토타입: 스프링 컨테이너는 프로토타입 빈의 생성과 의존관계 주입까지만 관여하고 더는 관리하지 않는 매우 짧은 범위의 스코프이다
- 웹 관련 스코프: request, session, application

__"singleton"(싱글톤 스코프)__
--------------------------------
![55](https://user-images.githubusercontent.com/96917871/153867935-aee55274-ed4f-4b30-a699-d1220ba0ba9c.PNG)
```
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
```

__"prototype"(프로토타입 스코프)__
--------------------------------------               
![123123](https://user-images.githubusercontent.com/96917871/153868274-5078a61b-a737-466f-b444-bda5d579f419.PNG)                  
![321321](https://user-images.githubusercontent.com/96917871/153868439-e2efd344-e62f-450e-bb16-88d72d4b5529.PNG)
- 스프링 컨테이너는 프로토타입 빈의 요청이 있을시 생성하고, 의존관계주입, 초기화까지만 처리한 후 클라이언트에 빈을 반환하고 그 후 스프링 컨테이너는 해당 프로토타입 빈을 관리하지 않는다.!!
- 스프링 컨테이너가 관리하지 않으니 스프링 컨테이너가 종료되기전 빈을 반환하는 과정에서 프로토타입의 빈은 컨테이너에게 없기 때문에 프로토타입의 빈의 "@PreDestroy"같은 종료 메서드는 호출되지 않는다.
```
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
```


__<프로토타입 스코프 -> 싱글톤내에서 프로토타입 빈을 사용할때>__
=============================================

__스프링 컨테이너에서 직접 빈을 가져오는 방식__
---------------------------------------------------------
```
static class ClientBean {
        @Autowired
        private ApplicationContext ac; //항상 새로운 프로토타입을 가져와야 하는경우니 스프링 컨테이너객체를 가져와 필요할때 마다 스프링 컨테이너에 요청한다.

        public int logic() {
            PrototypeBean prototypeBean = ac.getBean(PrototypeBean.class);
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
```
```
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
```
- 이렇게 스프링의 애플리케이션 컨테스트 전체를 주입받게 되면, 스프링 컨테이너에 종속적인 코드가 되고, 단위 테스트도 어려워진다.
- 지금 필요한 기능은 지정된 프로토타입 빈을 컨테이너에서 대신 찾아주는 "DL(dependency Lookup)"정도의 기능만 필요하다.
- 의존관계를 외부에서 주입(DI) 받는게 아니라 이렇게 직접 필요한 의존관계를 찾는 것을 "Dependency Lookup (DL) 의존관계 조회(탐색)" 이라한다

__스프링의 기능중 "ObjectProvider"사용 방식__
-----------------------------------------------
```
static class ClientBean {
        @Autowired
        private ObjectProvider<PrototypeBean> prototypeBeansProvider ;

        public int logic() {
            PrototypeBean prototypeBean = prototypeBeansProvider.getObject();
            prototypeBean.addConut();
            return prototypeBean.getCount();
        }
}
```
- "ObjectProvider"의 "getObject()" 를 호출하면 내부에서는 스프링 컨테이너를 통해 해당 빈을 찾아서 반환해준다 -> "DL" 
- ObjectProvider 는 지금 딱 필요한 DL 정도의 기능만 제공한다.
- 스프링 컨테이너 객체를 직접 가져오는 방식보다 훨씬 좋은 방식이다.
- 하지만 스프링 컨테이너에서만 사용할수 있는 기능이다. -> 다른 컨테이너 프레임워크에는 해당 기능이 없다 -> 그렇땐 아래 "자바 표준"을 사용한다.

__자바 표준라이브러리의중 "Provider" 사용 방식__
--------------------------------------------------
```
static class ClientBean {

          @Autowired
          private Provider<PrototypeBean> provider;

        public int logic() {
            PrototypeBean prototypeBean = provider.get();
            prototypeBean.addConut();
            return prototypeBean.getCount();
        }
    }
```
- 'javax.inject:javax.inject:1' 라이브러리를 gradle에 추가해야 한다.
- provider 의 get() 을 호출하면 내부에서는 스프링 컨테이너를 통해 해당 빈을 찾아서 반환한다. (DL)
- Provider 는 지금 딱 필요한 DL 정도의 기능만 제공한다.
- 자바 표준이므로 스프링이 아닌 다른 컨테이너에서도 사용할 수 있다.

__정리__
-----------------
- 프로토타입 빈을 언제 사용할까? 매번 사용할 때 마다 의존관계 주입이 완료된 새로운 객체가 필요하면 사용하면 된다. 그런데 실무에서 웹 애플리케이션을 개발해보면, 싱글톤 빈으로 대부분의 문제를 해결할 수 있기 때문에 프로토타입 빈을 직접적으로 사용하는 일은 매우 드물다
- "ObjectProvider", "JSR330 Provider"등은 프로토타입을 사용할때 뿐만 아니라 "DL"을 사용해야 할때도 많이 사용된다.
- 스프링 컨테이너 환경에서 사용한다면 "ObjectProvider"를 다른 컨테이너에서 사용된다면 "JSR330 Provider"를 경우에 맞추어 사용하면 된다.


__<웹 스코프>__
======================
- 싱글톤은 스프링 컨테이너의 시작과 끝까지 함께하는 매우 긴 스코프이고, 프로토타입은 생성과 의존관계 주입, 그리고 초기화까지만 진행하는 특별한 스코프이다.
- 웹 스포프는 웹 환경에서만 동작한다.!!!
- 웹 스코프는 프로토타입과 다르게 스프링이 해당 스코프의 종료시점까지 관리한다. 따라서 종료 메서드(@PreDestroy)가 호출된다.

__웹 스코프 종류__
-------------------
- request: HTTP 요청 하나가 들어오고 나갈 때 까지 유지되는 스코프, 각각의 HTTP 요청마다 별도의 빈
- session: HTTP Session과 동일한 생명주기를 가지는 스코프
- application: 서블릿 컨텍스트( ServletContext )와 동일한 생명주기를 가지는 스코프
- websocket: 웹 소켓과 동일한 생명주기를 가지는 스코프    
-> 각 종류는 나머지도 범위만 다르지 동작 방식은 비슷하다

__request 스코프__
----------------------
![111](https://user-images.githubusercontent.com/96917871/153861103-568e4d2e-4a3f-487b-9bde-2f1bd4784d52.PNG)
- 동시에 여러 클라이언트가 HTTP요청이 오면 정확히 어떤 클라이언트가 어떤 HTTP요청을 했는지 구분하기 어렵다. -> 이렇게 사용하면 좋은것이 "request스코프"이다.
- @Scope(value = "request") 를 사용해서 request 스코프로 지정했다. 이제 이 빈은 HTTP 요청 당 하나씩 생성되고, HTTP 요청이 끝나는 시점에 소멸된다.
- 즉 스프링 컨테이너가 생성될때 요청하여 등록할수 있는 빈이 아니라 스프링 컨테이너가 생성되 후 http 요청이 있을시 생성할수 있는 빈이다.      
- __HTTP요청이 있을시 생성할수 있는 빈이기 때문에 해당 "request 스코프 빈"을 가지고 있는 controller가 있다면 처음 스프링 컨테이너가 생성되면서 controller가 빈으로 등록되고 의존관계로 가지고 있는 "request 스코프 빈"을 자동 의존관계주입시 시 오류가 발생한다!!!     
-> 이 빈은 고객요청(HTTP요청)이 있을시에 생성할수 있는 빈이다!!!     
--> 그러므로 "Provider" 또는 "프록시(Proxy)" 를 통해 이를 해결할수 있다!!__       

__스코프와 Provider , 스코프와 프록시__
---------------------------------------------
```
//로그를 출력하기 위한 클래스
@Component
/*
@Scope(value = "request") 를 사용해서 request 스코프로 지정했다. 이제 이 빈은 HTTP 요청 당 하나씩 생성되고, HTTP 요청이 끝나는 시점에 소멸된다
 -> 즉 스프링 컨테이너가 생성될때 요청하여 등록할수 있는 빈이 아니라 스프링 컨테이너가 생성되 후 http 요청이 있을시 생성할수 있는 빈이다!!!!!!!!!!!!!!!
 + 생성되는 시점이 예를 들어 컨트롤러에서 http를 요청받는 메소드에서 해당 클래스를 사용하려고 불렀는경우에 생성된다 -> "myLogger.setRequestURL(requestURL);"여기서 myLogger의 메소드를 호춣하는순간 생성되어 빈으로 등록됨
 */
//@Scope("request") // = @Scope(value = "request") -> "ObjectProvider" 사용
@Scope(value="request" , proxyMode = ScopedProxyMode.TARGET_CLASS) // -> "Proxy" 사용
public class MyLogger {

    private String uuid;
    private String requestURL;

    public void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
    }

    public void log(String message) {
        System.out.println("[" + uuid + "]" + "[" + requestURL + "] " + message);
    }

    @PostConstruct
    public void init() {
        uuid = UUID.randomUUID().toString();
        System.out.println("[" + uuid + "] request scope bean create " + this);
    }

    @PreDestroy
    public void close() {
        System.out.println("[" + uuid + "] request scope bean close " + this );
    }
}
```

```
@Controller
public class LogDemoController {

    private final LogDemoService logDemoService;
    //private final ObjectProvider<MyLogger> myLoggerProvider; // ->"ObjectProvider" 사용 / 스프링 컨테이너가 올라올때 "ObjectProvider<T>"는 자동으로 생성해서 빈으로 등록해준다!!
    private final MyLogger myLogger;  // -> "Proxy" 사용


//    @Autowired // ->"ObjectProvider" 사용
//    public LogDemoController(LogDemoService logDemoService, ObjectProvider<MyLogger> myLogger) {
//        this.logDemoService = logDemoService;
//        this.myLoggerProvider = myLogger;
//    }

    @Autowired  // -> "Proxy" 사용
    public LogDemoController(LogDemoService logDemoService, MyLogger myLogger) {
        this.logDemoService = logDemoService;
        this.myLogger = myLogger;
    }

    @RequestMapping("log-demo")  // =? @GetMapping("log-demo")
    @ResponseBody
    public String logDemo(HttpServletRequest request) {

        String requestURL = request.getRequestURI().toString();

       // MyLogger myLogger = myLoggerProvider.getObject(); // ->"ObjectProvider" 사용
        myLogger.setRequestURL(requestURL);
        myLogger.log("controller test");
        logDemoService.logic("testId");
        /*
        Sevice"안에서 "getObject"해서 컨테이너에 요청해서 새로운 빈을 만들어서 주는게 아니라 해당 빈이 "request"이니깐 http요청있을때 "getObject"하면 각각의 전용빈!!을 생성해서 반환해주고
        http가 종료 즉 해당 메소드가 종료될때까지 같은 메소드내에서 즉 같은 손님이면 "MyLogger" 타입의 빈을 getObject하면 전용으로 만들었던 빈을 반환해주게 된다!!!
        http요청 후 반환되면 (해당 메소드 종료되면) 전용빈도 사라지게 된다.
        */

        return "OK";
    }
}
```

```
@Service
public class LogDemoService {

    //private final ObjectProvider<MyLogger> myLoggerProvider;
    private final MyLogger myLogger;

//    @Autowired  //-> "ObjectProvider" 사용
//    public LogDemoService(ObjectProvider<MyLogger> myLogger) {
//        this.myLoggerProvider = myLogger;
//    }

    @Autowired  // -> "Proxy" 사용
    public LogDemoService(MyLogger myLogger) {
        this.myLogger = myLogger;
    }

    public void logic(String id) {
       // MyLogger myLogger = myLoggerProvider.getObject(); //-> "ObjectProvider" 사용
        myLogger.log("service id = " + id);
    }
```
__<스코프와 Provider>__   
- ObjectProvider 덕분에 ObjectProvider.getObject() 를 호출하는 시점까지 request scope 빈의 생성을 지연할 수 있다.
- ObjectProvider.getObject() 를 호출하시는 시점에는 HTTP 요청이 진행중이므로 request scope 빈의 생성이 정상 처리된다
- ObjectProvider를 사용하지 않고 기존의 클래스 이름을(MyLogger)를 사용하려면 "프록시" 방법을 사용하면 된다.

__<스코프와 프록시>__   
- proxyMode = ScopedProxyMode.TARGET_CLASS 를 추가해주자. 적용 대상이 인터페이스가 아닌 클래스면 TARGET_CLASS 를 선택 , 적용 대상이 인터페이스면 INTERFACES 를 선택
- 이렇게 하면 MyLogger의 가짜 프록시 클래스를 만들어두고 HTTP request와 상관 없이 가짜 프록시 클래스를 다른 빈에 미리 주입해 둘 수 있다
- "CGLIB" 라는 라이브러리로 내 클래스를 상속 받은 가짜 프록시 객체를 만들어서 주입한다.
- @Scope 의 proxyMode = ScopedProxyMode.TARGET_CLASS) 를 설정하면 스프링 컨테이너는 CGLIB라는 바이트코드를 조작하는 라이브러리를 사용해서, MyLogger를 상속받은 가짜 프록시 객체를
생성한다.
- 스프링 컨테이너에 등록된 MyLogger타입의 빈을 조회해보면 한 순수한 MyLogger 클래스가 아니라 "MyLogger$$EnhancerBySpringCGLIB" 이라는 클래스로 만들어진 객체가 대신 등록된 것을 확인할 수 있다.
- 스프링 컨테이너가 생성되면서 "Controller"에 있는 "MyLogger"의 의존관계 주입에 가짜 객체인 프록시 객체가 주입되는것이다!
![image](https://user-images.githubusercontent.com/96917871/153865823-8996eba8-5cab-467a-b878-9e6003acf033.png)

- 클라이언트가 myLogger.logic() 을 호출하면 사실은 가짜 프록시 객체의 메서드를 호출한 것이다.
- 가짜 프록시 객체는 request 스코프의 진짜 myLogger.logic() 를 호출한다.

__정리__
-------------------
- 사실 Provider를 사용하든, 프록시를 사용하든 핵심 아이디어는 진짜 객체 조회를 꼭 필요한 시점까지 지연처리 한다는 점이다. -> "가장 중요한 점!!"
- 단지 애노테이션 설정 변경만으로 원본 객체를 프록시 객체로 대체할 수 있다. 이것이 바로 다형성과 DI 컨테이너가 가진 큰 강점이다.!!
- 꼭 웹 스코프가 아니어도 프록시는 사용할 수 있다.

