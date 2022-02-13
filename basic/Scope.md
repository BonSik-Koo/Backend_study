



__싱글톤내에서 프로토타입 빈을 사용할때__
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

