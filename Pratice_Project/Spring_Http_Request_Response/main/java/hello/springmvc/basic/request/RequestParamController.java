package hello.springmvc.basic.request;


import hello.springmvc.basic.HelloData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 요청 파라미터의 내용 -> Get, Html form와 같은 !!!!!
 * */
//버전 1
@Slf4j
@Controller
public class RequestParamController {

    //버전 1
    @RequestMapping("/request-param-v1")
    public void requestParamV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));

        log.info("username={}, age={}", username, age);

        response.getWriter().write("ok");
    }

    //버전 2
    @RequestMapping("/request-param-v2")
    @ResponseBody
    public String requestParamV2(
            @RequestParam("username") String username,
            @RequestParam("age") int age ){

        log.info("username={}, age={}", username, age);
        return "ok";
    }

    //버전 3
    @RequestMapping("/request-param-v3")
    @ResponseBody
    public String requestParamV3(
            @RequestParam String username, //get하는 parameter의 key의 이름과 같으면 생략가능!!!!!!!
            @RequestParam int age ){

        log.info("username={}, age={}", username, age);
        return "ok";
    }

    //버전 4
    @RequestMapping("/request-param-v4")
    @ResponseBody
    public String requestParamV4( String username, int age ){ //get하는 parameter의 key의 이름과 "애노테이션도" 생략이 가능하다!!!!!!!!!!!

        log.info("username={}, age={}", username, age);
        return "ok";
    }

    @ResponseBody
    @RequestMapping("/request-param-required")
    public String requestParamRequired(
            /**
             * "username"의 parameter 는 필수로 받아야된다.!!!
             * "age"는 필수 파라미터가 아닌데 "age"가 전달되지 않으면 기본적으로 "null"값을 넣어야 되는데 "int"자료형은 "null"값을 넣을수 없다 -> 그렇기 때문에 "Integer" 객체 자료형을 쓰는것이 맞다.
             */
            @RequestParam(required = true) String username,
            @RequestParam(required = false) Integer age) {

        log.info("username={}, age={}", username, age);
        return "ok";
    }

    // 파라미터에 값이 없는 경우 defaultValue 를 사용하면 기본 값을 적용할 수 있다.
    // 이미 기본 값이 있기 때문에 required 는 의미가 없다 -> 둘이 같이 쓰지 않는다 required의 기능이 무의미 해진다.
    @ResponseBody
    @RequestMapping("/request-param-default")
    public String requestParamDefault(
            @RequestParam(required = true, defaultValue = "guest") String username,
            @RequestParam(required = false, defaultValue = "-1") int age) {

        log.info("username={}, age={}", username, age);
        return "ok";
    }

    //파라미터 한번에 Map으로 받기
    @ResponseBody
    @RequestMapping("/request-param-map")
    public String requestParamMap(@RequestParam Map<String, Object> map) {

        log.info("username={}, age={}", map.get("username"), map.get("age"));
        return "ok";
    }

    // "parameter"를 "Model"로 받기!!
    @ResponseBody
    @RequestMapping("/model-attribute-v1")
    public String modelAttributeV1(@ModelAttribute HelloData helloData) {

        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());
        return "ok";
    }

    // "parameter"를 생랼가능!!
    @ResponseBody
    @RequestMapping("/model-attribute-v2")
    public String modelAttributeV2(HelloData helloData) { //생략가능!!

        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());
        return "ok";
    }
    /**
     * <스프링은 해당 생략시 다음과 같은 규칙을 적용한다!!!!!!>
     * String , int , Integer 같은 단순 타입 = @RequestParam
     * 나머지 = @ModelAttribute (argument resolver 로 지정해둔 타입 외)
     */
}

