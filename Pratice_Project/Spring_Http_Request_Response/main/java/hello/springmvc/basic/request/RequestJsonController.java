package hello.springmvc.basic.request;


import com.fasterxml.jackson.databind.ObjectMapper;
import hello.springmvc.basic.HelloData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.i18n.AcceptHeaderLocaleContextResolver;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Http 메시지 바디의 내용-> http 메시지 바디에 직접넣은 data 가져오는것
 * */
@Controller
@Slf4j
public class RequestJsonController {

    private ObjectMapper objectMapper = new ObjectMapper(); //Json 타입의 데이터를 바꾸어주는 "jackson"라이브러리의 기능!!!!

    //v1: json 형태의 데이터르 받기
    @PostMapping("/request-body-json-v1")
    public void requestBodyJsonV1(HttpServletRequest request, HttpServletResponse response) throws IOException {

        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("messageBody={}", messageBody);
        HelloData data = objectMapper.readValue(messageBody, HelloData.class); //messageBody 의 데이터를 해당 클래스로 매핑!!
        log.info("username={}, age={}", data.getUsername(), data.getAge());

        //String s = objectMapper.writeValueAsString(data); ->json 형태를 문자열로 바꾸어 준는것 --> 전송할때!!! 하지만 문자열이다!!!!!

        response.getWriter().write("ok");
    }

    //v2
    @PostMapping("/request-body-json-v2")
    @ResponseBody
    public String requestBodyJsonV2(@RequestBody String messageBody) throws IOException {
        /**
         * @RequestBody
         * HttpMessageConverter 사용 -> StringHttpMessageConverter 적용
         *
         * @ResponseBody
         * - 모든 메서드에 @ResponseBody 적용
         * - 메시지 바디 정보 직접 반환(view 조회X)!!!!!!!!!!!!!!!!!!!!!!!!!!
         * - HttpMessageConverter 사용 -> StringHttpMessageConverter 적용
         */

        log.info("messageBody={}", messageBody);
        HelloData data = objectMapper.readValue(messageBody, HelloData.class);
        log.info("username={}, age={}", data.getUsername(), data.getAge());

        return "ok";
    }

    //v3 ->"@ModelAttribute"처럼 지정한 클래스로 바로 매핑되는것
    // but!!!!) 하지만 "@Model~"은 요청 파라미터(get, html form)방식의 쿼리 파라미터를 가져오는것이고 "@ResponseBody"를 사용하는것은 쿼리 파라미터가 아닌 http 메시지 바디에 있는 데이터를 처리
    // http 메시지 컨버터거 "json"형태는 아래 처럼 해주지만 "text"형태는 해주지 않는다!!!!!!!!
    @PostMapping("/request-body-json-v3")
    @ResponseBody
    public String requestBodyJsonV3(@RequestBody HelloData helloData) throws IOException {
        /**
         * @RequestBody
         * - 메시지 바디 정보를 직접 조회(@RequestParam X, @ModelAttribute X)
         * - HttpMessageConverter 사용 -> StringHttpMessageConverter 적용
         *
         * @ResponseBody
         * - 메시지 바디 정보 직접 반환(view 조회X)!!!!!!!!!!!!!!!!!!!!!
         * - HttpMessageConverter 사용 -> StringHttpMessageConverter 적용
         */

        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());

        //String s = objectMapper.writeValueAsString(helloData); ->json 형태를 문자열로 바꾸어 준는것 --> 전송할때!!! --> 하지만 문자열이다!!!!
        return "ok";
    }

    //v4: "HttpEntity" 사용 + json 형태로 데이터 response 하기!!!!!!!!
    @PostMapping("/request-body-json-v4")
    @ResponseBody
    public HttpEntity<HelloData> requestBodyJsonV4(HttpEntity<HelloData> Data) throws IOException {
        HelloData helloData = Data.getBody();
        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());

        //return "ok";
        return new HttpEntity<>(helloData); //http body에 해당 데이터를(json형태인) response에 담게되고 전달
    }

    //v5 : json 형태로 데이터 response 하기!!!!!!!!
    @PostMapping("/request-body-json-v5")
    @ResponseBody
    public HelloData requestBodyJsonV5(@RequestBody HelloData helloData) throws IOException {
        /**
         * @ResponseBody
         * 응답의 경우에도 @ResponseBody 를 사용하면 해당 객체를 HTTP 메시지 바디에 직접 넣어줄 수 있다. -> 정말 "json"형태이다
         * 물론 이 경우에도 HttpEntity 를 사용해도 된다
         */
        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());

        return helloData; //위에서는 json을 문자열의 형태로 보냈지만 정말 "json"형태로 데이터를 보낼수 있다!!!!
    }
}
