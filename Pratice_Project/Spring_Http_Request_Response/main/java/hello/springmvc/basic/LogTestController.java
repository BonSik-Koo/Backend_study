package hello.springmvc.basic;


import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController //"@ResponseBody" 에노테이션처럼 직접 "Http body"에 원하는 데이터를 직접넣는 방식이다. -> 흔히 "RestAPI"라 부른다.
@Slf4j //쉽게 "로그"를 사용할수 있도록 "lombok"에서 지원해주는 에노테이션!!!!!
public class LogTestController {

    //private final Logger log = LoggerFactory.getLogger(getClass());

    @RequestMapping("/log-test")
    public String logTest() {

        String name = "Spring";

        /**
         * 출력할 문을 모두 출력하면 알아보기가 힘든상황이 있다. 그렇기 때문에 "로그"기능을 이용해서 내가 원하는 레벨을 출력할수 있고 기본 설정을 통해서 로그의 레벨도 정할수 있다.!!!
         * 로그가 출력되는 포멧 확인 -> 시간, 로그 레벨, 프로세스 ID, 쓰레드 명, 클래스명, 로그 메시지
         * LEVEL: TRACE > DEBUG > INFO > WARN > ERROR
         * 개발 서버는 debug 출력
         * 운영 서버는 info 출력
         */
        log.trace("trace log={}" , name);
        log.debug("debug log={}" , name);
        log.info("info log={}" , name);
        log.warn("warn log={}" , name);
        log.error("error log={}" , name);

        return "ok";
    }
}
