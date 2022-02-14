__Scope란?__
======================



__프로토타입 스코프 -> 싱글톤내에서 프로토타입 빈을 사용할때__
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


__웹 스코프__
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
__- HTTP요청이 있을시 생성할수 있는 빈이기 때문에 해당 "request 스코프 빈"을 가지고 있는 controller가 있다면 처음 스프링 컨테이너가 생성되면서 controller가 빈으로 등록되고 의존관계로 가지고 있는 "request 스코프 빈"을 자동 의존관계주입시 시 오류가 발생한다!!!     
-> 이 빈은 고객요청(HTTP요청)이 있을시에 생성할수 있는 빈이다!!!     
--> 그러므로 "Provider" 또는 프록시를 통해 이를 해결할수 있다!!__       

__<스코프와 Provider>__
 


