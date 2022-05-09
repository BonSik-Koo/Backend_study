__1.servlet를 사용해서 Html form으로 부터 입력받은 문자, 파일 출력해보기__
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
 
 
 
__2.servlet를 사용해서 Html form으로 부터 입력받은 문자, 파일 저장해보기__
====================================================================

(1) 파일 저장 경로 설정                
![image](https://user-images.githubusercontent.com/96917871/167455931-827258ea-e5ef-4182-8c80-e153b7f26d64.png)

(2) "multipart/form-data"인 HTML form 에서 전달받은 데이터,파일 출력 및 파일 저장

```
@Value("${file.dir}")
    private String fileDir;
    
@PostMapping("/upload")
    public String saveFileV1(HttpServletRequest request) throws ServletException, IOException {
        log.info("request={}", request);

        String itemName = request.getParameter("itemName");
        log.info("itemName={}", itemName);

        Collection<Part> parts = request.getParts();
        log.info("parts={}", parts);

        for (Part part : parts) {

            log.info("==== PART ====");
            log.info("name={}", part.getName());

            Collection<String> headerNames = part.getHeaderNames();
            for (String headerName : headerNames) {
                log.info("header {}: {}", headerName, part.getHeader(headerName));
            }

            //편의 메서드 제공
            //content-disposition; filename -> part header 에 있는 내용중 필요한 정보 가져올수 있다(알아서 파싱해준다.)
            log.info("submittedFileName={}", part.getSubmittedFileName()); //파일 이름을 가져온다!!!!
            log.info("size={}", part.getSize()); //part body size

            //데이터 읽기(part body에 있는 데이터들)
            InputStream inputStream = part.getInputStream();
            String body = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            log.info("body={}", body);

            //파일 저장하기
            if(StringUtils.hasText(part.getSubmittedFileName())){
                String fullPath = fileDir + part.getSubmittedFileName();
                log.info("파일 저장 fullPath={}", fullPath);
                part.write(fullPath); //해당 part 의 데이터(파일)를 저장해주는 메소드!!!!!!!!
            }
        }

        return "upload-form";
    }
```

- application.properties 에서 설정한 file.dir 의 값을 주입한다. -> 스프링이 제공해주는 애노테이션!!
- "멀티파트 형식"은 전송 데이터를 하나하나 각각 부분(Part)으로 나누어 전송한다. parts 에는 이렇게 나누어진 데이터가 각각 담긴다.   
-> 각 "Part"는 헤더와 바디로 나누어져있다.!!!
- "파일" 전송의 경우에 일반적인 문자를 전달하는 것과 다르게 "Part 헤더"에 "filename"과 "content-type"이 추가가 된다.!!!!!!!!!

- "서블릿"이 제공하는 "Part" 는 멀티파트 형식을 편리하게 읽을 수 있는 다양한 메서드를 제공한다.
 
 |Part 주요 메서드|설명|
 |-----------|-------------------|
 |part.getSubmittedFileName()|클라이언트가 전달한 파일명, Port의 헤더중 "filename"에 해당하는 부분|
 |part.getInputStream()| Part의 전송 데이터를 읽을 수 있다.(Part의 body에 있는 부분)|
 |part.write(...)|Part를 통해 전송된 데이터를 저장할 수 있다.(실제 파일 저장!!)|
 
    
