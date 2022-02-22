package hello.springmvc.basic.response;


import hello.springmvc.basic.HelloData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Controller
//@RestController -> Controller + responseBody 의 기능을 모두 가지고 있다. -->  해당 클래스의 메서드가 모둔 "body"에 쓰는것이라면 보통 "@RestController"를 많이 사용한다.!!
public class ResponseBodyController {

    /** text 형식 **/
    @GetMapping("/response-body-string-v1")
    public void responseBodyV1(HttpServletResponse response) throws IOException {
        response.getWriter().write("ok");
    }

    @GetMapping("/response-body-string-v2")
    public HttpEntity<String> responseBodyV2() {
        return new HttpEntity<>("ok");
        //return new ResponseEntity<>("ok", HttpStatus.OK); ->이거도 사용가능 RequestEntity, ResponseEntity 둘다 HttpEntity 를 상속받은 것이다!!!
    }

    @GetMapping("response-body-string-v3")
    @ResponseBody
    public String responseBodyV3 () {
        return "ok";
    }


    /** json 형식 **/
    @GetMapping("/response-body-json-v1")
    public HttpEntity<HelloData> responseBodyJsonV1() {
        HelloData helloData  = new HelloData();
        helloData.setAge(20);
        helloData.setUsername("userA");

        return new HttpEntity<>(helloData);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/response-body-json-v2")
    public HelloData responseBodyJsonV2() {
        HelloData helloData = new HelloData();
        helloData.setUsername("userA");
        helloData.setAge(20);

        return helloData;
    }
}
