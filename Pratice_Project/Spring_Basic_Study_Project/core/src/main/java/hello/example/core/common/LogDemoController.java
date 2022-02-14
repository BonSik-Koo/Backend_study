package hello.example.core.common;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LogDemoController {

    private final LogDemoService logDemoService;
    //private final ObjectProvider<MyLogger> myLoggerProvider; // ->"ObjectProvider" 사용 / 스프링 컨테이너가 올라올때 "ObjectProvider<T>"는 자동으로 생성해서 빈으로 등록해준다!!
    private final MyLogger myLogger;  // -> "Proxy" 사용


//    @Autowired // ->"ObjectProvider" 사용
//    public LogDemoController(LogDemoService logDemoService, ObjectProvider<MyLogger> myLogger) {
//        this.logDemoService = logDemoService;
//        this.myLoggerProvider = myLogger;
//    }

    @Autowired  // -> "Proxy" 사용
    public LogDemoController(LogDemoService logDemoService, MyLogger myLogger) {
        this.logDemoService = logDemoService;
        this.myLogger = myLogger;
    }

    @RequestMapping("log-demo")  // =? @GetMapping("log-demo")
    @ResponseBody
    public String logDemo(HttpServletRequest request) {

        String requestURL = request.getRequestURI().toString();

       // MyLogger myLogger = myLoggerProvider.getObject(); // ->"ObjectProvider" 사용
        myLogger.setRequestURL(requestURL);
        myLogger.log("controller test");
        logDemoService.logic("testId");
        /*
        "Sevice"안에서 "getObjec"t해서 컨테이너에 요청해서 새로운 빈을 만들어서 주는게 아니라 해당 빈이 "request"이니깐 http요청있을때 "getObject"하면 각각의 전용빈!!을 생성해서 반환해주고
        http가 종료 즉 해당 메소드가 종료될때까지 같은 메소드내에서 즉 같은 손님이면 "MyLogger" 타입의 빈을 getObject하면 전용으로 만들었던 빈을 반환해주게 된다!!!
        http요청 후 반환되면 (해당 메소드 종료되면) 전용빈도 사라지게 된다.
        */

        return "OK";
    }
}
