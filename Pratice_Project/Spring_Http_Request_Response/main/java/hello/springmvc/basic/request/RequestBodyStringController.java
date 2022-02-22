package hello.springmvc.basic.request;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

//버전 1
@Slf4j
@Controller
public class RequestBodyStringController {

    // v1: message-body -> text 형식으로 받기
    @PostMapping("/request-body-string-v1")
    public void requestBodyStringV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletInputStream inputStream = request.getInputStream(); //http body에 있는 데이터를 바이트로 읽어온다.
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8); // 'utf-8"형식으로 바이트를 변환시킴

        log.info("messageBody={}", messageBody);

        response.getWriter().write("ok");
    }

    // v2: message-body -> text 형식으로 받기
    @PostMapping("/request-body-string-v2")
    public void requestBodyStringV2(InputStream inputStream, Writer responseWriter) throws IOException {
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8); // 'utf-8"형식으로 바이트를 변환시킴
        log.info("messageBody={}", messageBody);
        responseWriter.write("ok");

    }

    // v3 -> 위와 같이 body에 있는 데이터를 그냥 바로 받아올수 있다.!!! -->애노테이션을 사용하지 않는 방법
    @PostMapping("/request-body-string-v3")
    public HttpEntity<String> requestBodyStringV3(HttpEntity<String> httpEntity) throws IOException {

        /**
         * HttpEntity: HTTP header, body 정보를 편리하게 조회
         * 메시지 바디 정보를 직접 조회
         * HttpEntity는 응답에도 사용 가능
         * 메시지 바디 정보 직접 반환 -> "HttpEntity"객체를 생성할때 넘기는 파라미터가 "body"에 들어간다.
         * 헤더 정보 포함 가능
         */
        String messageBody = httpEntity.getBody();
        log.info("messageBody={}", messageBody);

        return new HttpEntity<>("ok"); //"HttpEntity"객체를 생성할때 넘기는 파라미터가 "body"에 들어간다.
        //http-body에 데이터를 넣어 전달하기 때문에 "view" 조회기능은 없어진다
    }

    // v4 -> 애노테이션을 사용해서 그냥 "http-body"의 값을 바로 가져오기!!! --> 이것이 가장 많이 사용된다!!!!!!!!!
    @PostMapping("/request-body-string-v4")
    @ResponseBody
    public String requestBodyStringV4(@RequestBody String messageBody )  {
        log.info("messageBody={}", messageBody);
        return "ok";
    }
}
