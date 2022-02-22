package hello.springmvc.basic.requestmapping;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController //"@ResponseBody" 에노테이션처럼 직접 "Http body"에 원하는 데이터를 직접넣는 방식이다. -> 흔히 "RestAPI"라 부른다.
@Slf4j
public class MappingController {

    @RequestMapping("/hello-basic")
    public String helloBasic() {
        log.info("helloBasic");
        return "ok";
    }

    @GetMapping(value = "/mapping-get-v1")
    public String mappingGetV2() {
        log.info("mapping-get-v1");
        return "ok";
    }

    /**
     * PathVariable 사용 -> 최근 HTTP API는 다음과 같이 리소스 경로에 식별자를 넣는 스타일을 선호한다
     * 변수명이 같으면 생략 가능
     * @PathVariable("userId") String userId == @PathVariable userId
     */
    // "/mapping/userA" 의 URL이 오면 -> "userA"를 바로 받을수 있다.
    @GetMapping("/mapping/{userId}")
    public String mappingPath(@PathVariable("userId") String data) {
        log.info("mappingPath userId={}", data);
        return "ok";
    }

    /**
     * PathVariable 사용 다중
     */
    @GetMapping("/mapping/users/{userId}/orders/{orderId}")
    public String mappingPath(@PathVariable String userId, @PathVariable Long orderId) {
        log.info("mappingPath userId={}, orderId={}", userId, orderId);
        return "ok";
    }

    /**
     * 파라미터로 추가 매핑
     * params="mode",
     * params="!mode"
     * params="mode=debug"
     * params="mode!=debug" (! = )
     * params = {"mode=debug","data=good"}
     */
    @GetMapping(value = "/mapping-param", params = "mode=debug")
    public String mappingParam() {
        log.info("mappingParam");
        return "ok";
    }

    /**
     * 특정 헤더로 추가 매핑
     * headers="mode",
     * headers="!mode"
     * headers="mode=debug"
     * headers="mode!=debug" (! = )
     */
    @GetMapping(value = "/mapping-header", headers = "mode=debug")
    public String mappingHeader() {
        log.info("mappingHeader");
        return "ok";
    }

    /**
     * Content-Type 헤더 기반 추가 매핑 Media Type
     * consumes="application/json"
     * consumes="!application/json"
     * consumes="application/*"
     * consumes="*\/*"
     * MediaType.APPLICATION_JSON_VALUE
     */
    @PostMapping(value = "/mapping-consume", consumes = "application/json") //서버가 웹 브라우저로 부터 해당 context-type 가 왔을때 실행되는 옵션
    public String mappingConsumes() {
        log.info("mappingConsumes");
        return "ok";
    }

    /**
     * Accept 헤더 기반 Media Type
     * produces = "text/html"
     * produces = "!text/html"
     * produces = "text/*"
     * produces = "*\/*"
     */
    @PostMapping(value = "/mapping-produce", produces = "text/html") //위와 반대로 웹 브라우저가 서버로 부터 해당 "Accept"옵션의 헤더에서 context-type 이 왔을때 받을수 있는 옵션
    public String mappingProduces() {
        log.info("mappingProduces");
        return "ok";
    }
}
