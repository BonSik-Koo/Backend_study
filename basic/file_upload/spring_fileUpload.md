__스프링을 이용한 파일 업로드__
===============================
- "MultipartFile" 이라는 인터페이스로 "멀티파트 파일"을 매우 편리하게 지원한다.
- "@RequestParam MultipartFile file": 업로드하는 HTML Form의 name에 맞추어 @RequestParam 을 적용하면 된다. 해당 Form 의 name에 맞는 "Part"를 가져오는 것이다.!!, 클래스로는 "MultipartFile"인터페이스를 사용하는것이다.  
- "@ModelAttribute" 에서도 MultipartFile 을 동일하게 사용할 수 있다.      
-> @ModelAttribute의 클래스에 "MultipartFile"를 넣고 정의하면 되는것이다.




