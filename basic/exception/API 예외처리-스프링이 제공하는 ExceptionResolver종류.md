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
