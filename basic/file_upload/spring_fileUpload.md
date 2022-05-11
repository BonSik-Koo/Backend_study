__스프링을 이용한 파일 업로드__
===============================
- "MultipartFile" 이라는 인터페이스로 "멀티파트 파일"을 매우 편리하게 지원한다.
- "@RequestParam MultipartFile file": 업로드하는 HTML Form의 name에 맞추어 @RequestParam 을 적용하면 된다. 해당 Form 의 name에 맞는 "Part"를 가져오는 것이다.!!, 클래스로는 "MultipartFile"인터페이스를 사용하는것이다.  
- "@ModelAttribute" 에서도 MultipartFile 을 동일하게 사용할 수 있다.      
-> @ModelAttribute의 클래스에 "MultipartFile"를 넣고 정의하면 되는것이다.

 |MultipartFile 주요 메서드|설명|
 |-----------|-------------------|
 |file.getOriginalFilename()|업로드 파일명|
 |file.transferTo(...)|파일 저장|

--------------------------------------------------------------
```
/**
 * 스프링을 이용한 파일 가져오기 및 저장
 */
@Slf4j
@Controller
@RequestMapping("/spring")
public class SpringUploadController {

    @Value("${file.dir}")
    private String fileDir;

    @GetMapping("/upload")
    public String newFile(){
        return "upload-form";
    }

    @PostMapping("/upload")
    public String saveFile(@RequestParam String itemName,
                           /**
                            * 업로드하는 HTML Form의 name에 맞추어 @RequestParam 을 적용하면 된다.
                            * name해당하는 "Part"를 받아온다.!!! -> 스프링이 제공하는 MultipartFile객체에 담아준다.
                            * getOriginalFilename()를 사용하게 되면 "Part"의 "filename"을 얻을수 있다
                            */
                           @RequestParam MultipartFile file,
                           HttpServletRequest request) throws IOException {

        log.info("request={}", request);
        log.info("itemName={}", itemName);
        log.info("multipartFile={}", file);

        if(!file.isEmpty()) {
            String fullPath = fileDir + file.getOriginalFilename();
            log.info("파일 저장 fullPath={}", fullPath);

            /**
             * 파일을 저장하는 부분이다.
             */
            file.transferTo(new File(fullPath)); //방법1
            //file.transferTo(Path.of(fullPath)); //방법2
        }

        return "upload-form";
    }
}
```





