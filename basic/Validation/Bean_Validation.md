__Bean_Validation__
==========================
- 앞서 spring validator에서 글로벌로 설정할수도 있는데 해당 Bean Validation을 사용하게 되면 글로벌로 Validator로 등록이 가능하고 다양한 애노테이션을 사용하여 검증 로직을 직접 구현할 필요 없이 편리하게 검증할 수 있다.
- 또한 필드 오류에 대한 FieldError로 자동으로 만들어서 BindlingResult에 담아준다.!!
- 스프링 부트가 spring-boot-starter-validation 라이브러리를 넣으면 자동으로 Bean Validator를 인지하고 스프링에 통합한다.         
-> 스프링 부트가 시작될때 LocalValidatorFactoryBean 을 글로벌 Validator로 등록하게 되고 검증이 필요할때마다 해당 Validator를 호출하여 사용한다.

```
 @NotBlank
    private String itemName; //상품 이름

    @NotNull
    @Range(min=1000, max=1000000)
    private Integer price; //가격과 수량이 null들어갈수 있기 때문에 -> Integer자료형 사용

    @NotNull
    @Max(9999)
    private Integer quantity; //상품 수량
```
```
public String addItemV(@Validated @ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) 
```
- 이 Validator는 @NotNull 같은 애노테이션을 보고 검증을 수행한다. 이렇게 글로벌 Validator가 적용되어 있기 때문에, @Valid ,@Validated 만 적용하면 된다.      
 -> 애당 애노에티션이 있는 객체를 검증해준다.!!!              
- 검증 오류가 발생하면, FieldError , ObjectError 를 생성해서 BindingResult 에 담아준다.           
-> "rejectValue"처럼 "MessageCodeResolver"가 있어 에러코드를 생성해서 자동으로 담아준다.!!!     
- @ModelAttribute 로 각각의 필드에 타입 변환을 시도한다. 만약 __타입에러가 발생한다면__ 해당 오류타입,오류코드들을 포함하는 "FieldError"를 생성하여 BindingResult에 담아준다. 또한 이런 상황에서는 "Bean Validation"이 적용되지 않는다!!!!.
- @ModelAttribute 로 각각의 필드에 타입 변환을 시도한다. 만약 __타입에러가 발생하지 않는다면__ "Bean Validation"이 적용된다.!!!

__Bean Validation - 에러코드__
=================================
- 오류 코드가 애노테이션 이름으로 등록된다. 마치 typeMismatch 와 유사하다.
- 필드에 지정한 애노테이션 이름 기반으로 오류코드를 MessageCodesResolver가 아래와 같이 생성한다.
```
- @NotBlank -
NotBlank.item.itemName
NotBlank.itemName
NotBlank.java.lang.String
NotBlank

- @Range -
Range.item.price
Range.price
Range.java.lang.Integer
Range
```
- 만약 @NotBlank, @Range 두개의 애노테이션이 필드에 선언되있으면 순서대로 검증하면서 오류가 발생한 애노테이션의 이름을 기반으로 에러코드를 생성한다.
- 사용자가 "properties"에 Level에 맞게 오류 메시지를 정의하여 사용하면 된다. 만약 메시시 코드를 생성하지 않으면 라이브러리가 제공하는 기본 값을 사용하게 된다. 추가로 기본값을 @NotBlank(message="~") 처럼 애노테이션에도 지정할 수 있다.   

__Bean Validation - groups__
================================
- 동일한 모델 객체를 등록폼, 수정폼에서 각각 다르게 검증해야 되는 상황에서 사용된다.    
-> 등록시에 검증할 기능과 수정시에 검증할 기능을 각각 그룹으로 나누어 적용할 수 있다.

```
public interface SaveCheck {
}
public interface UpdateCheck {
}
```
- groups을 구분하는 인터페이스, 클래스를 지정한다.

```
@NotNull(groups = UpdateCheck.class) //수정시에만 적용
 private Long id;

 @NotBlank(groups = {SaveCheck.class, UpdateCheck.class})
 private String itemName;

 @NotNull(groups = {SaveCheck.class, UpdateCheck.class})
 @Range(min = 1000, max = 1000000, groups = {SaveCheck.class,UpdateCheck.class})
 private Integer price;
```
- 사용될 검증 기능의 그룹을 지정하는 것이다.
- 참고) @Valid(자바표준제공) 에는 groups를 적용할 수 있는 기능이 없다. 따라서 groups를 사용하려면 @Validated(스프링제공) 를 사용해야 한다.
- 추가로 실제 프로젝트에서 등록폼의 객체와 수정폼의 객체가 다른 상황이 많기 때문에 group사용하지 않고 대부분  form을 분리시켜 사용을 많이 한다.

__Bean Validation -Http 메시지 컨버터__
===================================
- @Valid , @Validated 는 HttpMessageConverter(@RequestBody)에도 적용할 수 있다. -> http 바디에 Json형태로 오는 데이터도 검증 가능
- "@RequestBody"의 애노테이션을 사용하면 된다. @ModelAttribute(쿼리 스트링, Post form처리)와 같이 Bean Validation을 사용하면 된다.!!! 
- httpmessageConverter가 컨트롤러를 호출하기전 Json객체의 필드 타입 오류가 발생할  경우 해당 컨트롤러를 호출도 하지 않고 예외가 발생한다.   
-> httpmessageConverter가 Json객체를 생성하는 거 자체를 실패함 
--> 검증기도 작동하지 못함      


__@ModelAttribute vs @RequestBody__
========================================
__<@ModelAttribute>__     
----------------------
- BindingResult가 있을 때 HTTP 요청 파리미터를 처리하는 @ModelAttribute 는 각각의 필드 단위로 세밀하게 적용된다. 그래서 특정 필드에 타입이 맞지 않는 오류가 발생해도 나머지 필드는 정상 처리할 수 있었다.    
-> 타입오류가 발생해도 해당 컨트롤러가 실행된다.!!
- @ModelAttribute 는 필드 단위로 정교하게 바인딩이 적용된다. 특정 필드가 바인딩 되지 않아도 나머지 필드는 정상 바인딩 되고, Validator를 사용한 검증도 적용할 수 있다.


__<@RequestBody : HttpMessageConverter(Json,String) 사용>__     
-------------------------------
- HttpMessageConverter(Argument Resolver안에 있는) 는 @ModelAttribute 와 다르게 각각의 필드 단위로 적용되는 것이 아니라, 전체 객체 단위로 적용된다. 따라서 메시지 컨버터의 작동이 성공해서 Json 객체를 만들어야 @Valid , @Validated 가 적용된다.   
-> 타입오류가 발생하면 Json객체를 만드는거부터 실패하기 때문에 해당 컨트롤러가 호출이 되지 않는다.!!    
- @RequestBody 는 HttpMessageConverter 단계에서 JSON 데이터를 객체로 변경하지 못하면 이후 단계 자체가 진행되지 않고 예외가 발생한다. 컨트롤러도 호출되지 않고, Validator도 적용할 수 없다.     
