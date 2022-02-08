package hello.hellospring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController {

    @GetMapping("/") //기본 포트를 입력한 URL접속시 해당 메소드 실행
    public String home() {
        return "home";  // ->templates 파일 내에 home.html파일 찾아서 실행
    }
}
