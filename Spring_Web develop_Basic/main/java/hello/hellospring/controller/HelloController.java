package hello.hellospring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller //Spring Framwork에서 Controller라는 걸 인지시켜준다.
public class HelloController {

    @GetMapping("hello") //URL에서 hello가 있으면 get하고 아래 메서드 실행 -> get요청의 API
    public String hello(Model model) { //Spring boot가 model을 만들어 같이 넘겨준다.
        model.addAttribute("data","Spring!!"); //model에 key가 "data"고 value가 "Spring!!"으로 넣는다.
        return "hello";
    }

    @GetMapping("hello-mvc") //파라메터를 받는 API인 경우
    //"@RequestParam" : 웹브라우저에서 "?name=Spring"(get방식)으로 입력하게 되면 아래 "name"에 Spring이라는 value가 들어가게 된다.
    public String Bon(@RequestParam("name") String value, Model model) { //웹 브라우저로부터 value를 받는다.
        model.addAttribute("name", value); //key가 "name"이고 value가 name에 든 값이다.
        return "hello-template";
    }


    //API 방식(보통 Spring에서 객체를 전달받는 방식)
    @GetMapping("hello-string")
    @ResponseBody //Http Body에 return 값을 그대로 넣어준다!!! -> http구조에서 body부분의 Html이 들어가는데 거기에 그냥 "hello"을 적는다.
    public String helloMvc(@RequestParam("name") String value, Model mode) {
        return "hello";
    }

    @GetMapping("hello-api")
    @ResponseBody
    public Hello helloapi(@RequestParam("name") String value, Model model) {
        Hello hello = new Hello();
        hello.setName(value);
        return hello; //Http Body에 Json형식으로 데이터를 넣어준다.
    }

    static class Hello {
        private String name;

        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name= name;
        }
    }





}