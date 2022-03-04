- FieldError , ObjectError 는 다루기 너무 번거롭다
- 또한  BindingResult는 검증할 객체인 target바로 다음에 온다. 따라서 BindingResult는 이미 본인이 검증해야 할 객체인 target을 이미 알고 있다.

__rejectVaule()__
===========================
```
void rejectValue(@Nullable String field, String errorCode, @Nullable Object[] errorArgs, @Nullable String defaultMessage);
예) bindingResult.rejectValue("price", "range", new Object[]{1000, 1000000}, null)
```
- field : 오류 필드명
- errorCode : 오류 코드 (※참고 :오류 코드들은 properties에 모아둔다.)
- messageResolver를 위한 오류 코드이다.
- errorArgs : 오류 메시지에서 "{0}" 을 치환하기 위한 값
- defaultMessage : 오류 메시지를 찾을 수 없을 때 사용하는 기본 메시지

- 앞에서 BindingResult 는 어떤 객체를 대상으로 검증하는지 target을 이미 알고 있다고 했다. 따라서 target(item)에 대한 정보는 없어도 된다.
- rejectValue는 해당 파라미터 정보를 사용해서 "Fielderror"를 자동으로 만들어준다.!!!!        
-> 하지만 여기서 Fielderror를 사용할때는 "errorCode"를 문자열 배열로 정확한 code명을 적어주었는데 해당 메소드는 target 정보(객체이름, 필드이름)등의 이름은 적어주지 않아도 된다.      
--> 객체이름, 필드 이름, 등을 조합해서 errorCode를 만든다.    
---> "MessageCodesResolver"가 이러한 기능을 한다.    
__----> 컨트롤러의  코드를 수정하지 않고 메시지 코드만을 변경하여 모두 적용할수 있다!!__      
- "fieldError"에서는 reject value를 직접 넣었지만 해당 메소드는 객체 필드명에 들어온 값을 자동으로 "reject value"로 넣어준다.!
 
__reject()__
====================
- 기본적인 기능이 "rejectValue()"와 같지만 "field"만 없다. 
- 즉 특정 필드의 오류가 아닌 더 넚은 오류에 관한 처리


__<오류 코드의 단순화>__
------------------------------------
-오류 코드를 만들 때 다음과 같이 자세히 만들 수도 있다 -> required.item.itemName : 상품 이름은 필수 입니다
- 또는 다음과 같이 단순하게 만들 수도 있다. -> required : 필수 값 입니다.
- 단순하게 만들면 범용성이 좋아서 여러곳에서 사용할 수 있지만, 메시지를 세밀하게 작성하기 어렵다. 반대로 너무 자세하게 만들면 범용성이 떨어진다. 가장 좋은 방법은 범용성으로 사용하다가, 세밀하게 작성해야 하는 경우에는 세밀한 내용이 적용되도록 메시지에 단계를 두는 방법이다.   
-> 개발자가 쉽게 오류 메시지들을 한번에 관리할수 있다.         
    
__MessageCodesResolver__
--------------------------
```
MessageCodesResolver codesResolver = new DefaultMessageCodesResolver();//인터페이스 - 구현체(클래스)
    @Test // -> 객체 오류
    void messageCodesResolverObject() {
        String[] messageCodes = codesResolver.resolveMessageCodes("required", "item");
        assertThat(messageCodes).containsExactly("required.item", "required");
    }
    
    @Test // -> 필드 오류
    void messageCodesResolverField() {
        String[] messageCodes = codesResolver.resolveMessageCodes("required", "item", "itemName", String.class);
        assertThat(messageCodes).containsExactly(
                "required.item.itemName",
                "required.itemName",
                "required.java.lang.String",
                "required"
        );
    }
```
- MessageCodesResolver는 전달되는 파라미터에 따라 우선순의를 정해 오류코드들을 생성한다.
- 주로 ObjectError , FieldError 함께 사용된다 -> 생성된 오류 코드들을 가지고 생성     
-> "rejectValue", "reject"들은 내부에 "MessageCodeResolver", "ObjectError ", "FieldError"들을 가지고있어 자동으로 해준다.           
(※참고! rejectValue->FieldError, reject->ObjectError)    


```
<객체 오류의 경우 다음 순서로 2가지 생성> -> reject()
1.: code + "." + object name
2.: code
예) 오류 코드: required, object name: item
1.: required.item
2.: required

<필드 오류의 경우 다음 순서로 4가지 메시지 코드 생성> -> rejectValue()
1.: code + "." + object name + "." + field
2.: code + "." + field
3.: code + "." + field type
4.: code
예) 오류 코드: typeMismatch, object name "user", field "age", field type: int
1. "typeMismatch.user.age"
2. "typeMismatch.age"
3. "typeMismatch.int"
4. "typeMismatch"
```


