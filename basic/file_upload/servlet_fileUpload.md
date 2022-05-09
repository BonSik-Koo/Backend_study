__servlet를 사용해서 Html form으로 부터 입력받은 문자, 파일 출력해보기__
====================================================================
```
-HTML-

<form th:action method="post" enctype="multipart/form-data">
 <ul>
 <li>상품명 <input type="text" name="itemName"></li>
 <li>파일<input type="file" name="file" ></li>
 </ul>
 <input type="submit"/>
 </form>
 ```
 
 ```
 -Controller-
 
 @PostMapping("/upload")
 public String saveFileV1(HttpServletRequest request) throws
ServletException, IOException {
 log.info("request={}", request);
 String itemName = request.getParameter("itemName");
 log.info("itemName={}", itemName);
 Collection<Part> parts = request.getParts();
 log.info("parts={}", parts);
 return "upload-form";
 }
 ```
 
- HttpServletRequest 객체가 "RequestFacade" 에서 "StandardMultipartHttpServletRequest" 로 변한 것을 확인할 수 있다.    
-> RequestFacade는 HttpServletRequest의 기본 구현체       
- servlet를 이용해서(HttpServletRequest) Html form에서 입력받은 문자를 받아올수 있고 전달된 여러 데이터(문자, 바이너리데이터(파일))들이 "Parts"로 나누어져 있다.     
__-> Parts들은 여러개의 Part가 합쳐져 있는것인데 각각의 "Part"도 헤더와 바디로 구분되어 있다.__     
- 추가적으로 "multipart"의 파일 사이즈 제한, multipart 타입 처리 유무에 대해서도 설정이 가능하다.
 
