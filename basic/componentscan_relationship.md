__컴포넌트 스캔__
-----------------------------
- 스프링 빈을 등록할때 자바코드의 @Bean이나 XMA의 <bean>등을 통해서 설정 정보를 직접 스프링 빈에 등록하였다. -> 빈의 개수가 많아 지면 누락하는경우가 발생하고 코드의 길이와 설정정보의 규모도 커진다
- 스프링은 설정정보가 없어도(@Bean으로 작성하여 생성하는) 자동으로 스프링 빈을 등록하는 "컴포넌트 스캔"기능을 제공한다.
- 또한 기존에는 "Bean"으로 등록하면서 의존관계를 주입하였지만 컴포넌트 스캔 방식을 사용할때에는 "@Autowired"를 사용하여 의존관계도 자동으로 주입하는 기능을 가지고 있다.

```
@Configuration
@ComponentScan(
 excludeFilters = @Filter(type = FilterType.ANNOTATION, classes =
Configuration.class))
public class AutoAppConfig {
 
}
```


