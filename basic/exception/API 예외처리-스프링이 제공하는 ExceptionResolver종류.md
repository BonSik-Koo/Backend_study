__1. ResponseStatusExceptionResolver__
========================================
- 스프링이 제공해주 "ExceptionResolver"중 하나이다.
- 예외에 따라서 Http상태코드를 지정해주는 역할을 한다.        
-> 사용자가 원하는 Http상태코드로 변환하여 WAS에 전달하는것이다.              
- "@ResponseStatus"가 달려있는 예외, "ResponseStatusException" 예외를 처리해준다.

------------------------------
__예1) "@ResponseStatus" 사용__
```
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "잘못된 요청 오류")
public class BadRequestException extends RuntimeException {
}
```
- BadRequestException 예외가 컨트롤러 밖으로 넘어가면 ResponseStatusExceptionResolver가 예외가 해당 애노테이션을 확인해서 오류 코드를 HttpStatus.BAD_REQUEST (400)으로 변경하고, 메시지도 담는다.
- ResponseStatusExceptionResolver 코드를 확인해보면 결국 response.sendError(statusCode, resolvedReason) 를 호출하는 것을 확인할 수 있다.            
-> WAS에세 sendError를 통해서 사용자가 원하는 HTTP상태코드로 처리한후 WAS로 전달하게 된다.(WAS입장에서는 정상적인 흐름)          
- sendError(400) 를 호출했기 때문에 WAS에서 다시 오류 페이지( /error )를 내부 요청한다.           
-> 스프링내부에 "BasicErrorController"가 처리           


<※추가기능>
- reason 을 MessageSource 에서 찾는 기능도 제공한다. reason = "error.bad"
-> "error.bad"에 맞는 코드를 "messages.properties"에서 찾아서 매핑 해주는 기능
-------------------------------

----------------------------
__예2) "ResponseStatusException" 사용__
```
@GetMapping("/api/response-status-ex2")
public String responseStatusEx2() {
 throw new ResponseStatusException(HttpStatus.NOT_FOUND, "error.bad", new IllegalArgumentException());
}
```
- @ResponseStatus 는 개발자가 직접 변경할 수 없는 예외에는 적용할 수 없다. (애노테이션을 직접 넣어야 하는데, 내가 코드를 수정할 수 없는 라이브러리의 예외 코드 같은 곳에는 적용할 수 없다.) 추가로 애노테이션을 사용하기 때문에 조건에 따라 동적으로 변경하는 것도 어렵다. 이때는 ResponseStatusException 예외를 사용하면 된다.    
--------------------------------------------------------


__2. DefaultHandlerExceptionResolver__
========================================
- "DefaultHandlerExceptionResolver" 는 스프링 내부에서 발생하는 스프링 예외를 해결한다. 대표적으로 "파라미터 바인딩(쿼리파라미터로 일치하지 않은 타입의 변수 입력했을경우)시점"에서 타입이 맞지 않는 경우가 있다. "ModelAttribute"등       
-> 이런경우 __"TypeMismatchException"__ 을 발생시키는데 "@bindingResult"와 같이 처리를 해주지 않으면 서블릿 컨테이너까지 오류가 올라가게 되고, 결과적으로 서블릿까지 전달되어 500오류가 발생하게 된다.    
--> 하지만 실제로는 500오류가 발생하지 않는데 그 이유는 바인딩 오류같은 스프링 내부에서 오류(TypeMismatchException 뿐만아니라 여러  종류가 존재!!) 가 발생하게 되면 "DefaultHandlerExceptionResolver"가 처리해주고 Http 상태코드를 400으로 바꾸어 처리하게 된다.     
---> "DefaultHandlerExceptionReslover"도 "response.sendError()"를 통해서 문제를 해결하고 sendError(400) 를 호출했기 때문에 WAS에서 다시 오류 페이지( /error )를 내부 요청한다.         
----> HTTP오류코드를 400으로 바꾸는 이유는 스프링 내부에서 발생하는 예외들은 대부분 클라이언트가 HTTP요청정보를 잘못입력하였기 때문이기 때문에 400오류로 사용하는 것이다.




