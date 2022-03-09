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
- 검증 오류가 발생하면, FieldError , ObjectError 를 생성해서 BindingResult 에 담아준다.
- @ModelAttribute 로 각각의 필드에 타입 변환을 시도한다. 만약 __타입에러가 발생한다면__ 해당 오류타입,오류코드들을 포함하는 "FieldError"를 생성하여 BindingResult에 담아준다. 또한 이런 상황에서는 "Bean Validation"이 적용되지 않는다!!!!.
- @ModelAttribute 로 각각의 필드에 타입 변환을 시도한다. 만약 __타입에러가 발생하지 않는다면__ "Bean Validation"이 적용된다.!!!

