__Spring의 Validator__
===========================
- 컨트롤러에서 검증 로직이 차지하는 부분은 매우 크다. 이런 경우 별도의 클래스로 역할을 분리하는 것이 좋다. 그리고 이렇게 분리한 검증 로직을 재사용 할 수도 있다
- 스프링이 Validator 인터페이스를 별도로 제공하는 이유는 체계적으로 검증 기능을 도입하기 위해서다.
- 스프링이 Validator 인터페이스를 구현하지 않고 검증 기능을 가진 객체를 생성하여 사용해도 되지만 해당 스프링 Validator인터페이스를 구현한 검증기를 사용하면 이전과 같이 직접 불러서(스프링빈등록, 객체 생성등) 사용 가능하지만 추가적인 기능도 얻을 수 있다!!!.      
-> 검증 기능 호출없이 편하게 검증을 할 수 있다.            


```
@InitBinder
public void init(WebDataBinder dataBinder) {
 log.info("init binder {}", dataBinder);
 dataBinder.addValidators(itemValidator);
}
````
- itemValidator는 스프링 validator 인터페이스를 구현한 검증 기능 구현체
- WebDataBinder 는 스프링의 파라미터 바인딩의 역할을 해주고 검증 기능도 내부에 포함한다.
- 적용할 컨트롤러에 WebDataBinder 검증기를 추가하면              
-> 해당 컨트롤러에서는 검증기를 자동으로 적용할 수 있다.            
--> 해당 컨트롤러가 호출 될때마다 처음 @InitBinder로 선언된 메소드가 호출되고 파라미터로 받는 WebDataBinder가 생성된다.    
---> 컨트롤러가 호출될때마다 새로 생성되는 것이다.!
----> 글로벌 설정도 가능 ( 모든 컨트롤러에 다 적용)           
- @InitBinder 해당 컨트롤러에만 영향을 준다.

```
@PostMapping("/add")
public String addItemV6(@Validated @ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes)
```
- @Validated 는 검증기를 실행하라는 애노테이션이다.
- 이 애노테이션이 붙으면 앞서 WebDataBinder 에 등록한 검증기를 찾아서 실행한다. 그런데 여러 검증기를 등록한다면 그 중에 어떤 검증기가 실행되어야 할지 구분이 필요하다. 이때 ItemValidator의 supports() 가 사용된다. 
- ItemValidator의 supports(Item.class) 호출되고, 결과를 반환하고 true이면 ItemValidator의 validate() 가 호출된다.
- 검증을 마치게 된 후 결과를 bindingResult에 담게 된다.
