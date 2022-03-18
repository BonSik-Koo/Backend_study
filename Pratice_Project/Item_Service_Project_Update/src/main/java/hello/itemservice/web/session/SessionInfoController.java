package hello.itemservice.web.session;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.Iterator;

/** session 정보들 출력 해본 것 -> 생명 주기를 정할수 있다. **/
@Slf4j
@RestController
public class SessionInfoController {

    @GetMapping("/session-info")
    public String sessionInfo(HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        if(session==null) {
            return "세션이 없습니다";
        }

        //세션 데이터 출력
        session.getAttributeNames().asIterator()
                .forEachRemaining(name -> log.info("session name={}, value={}", name, session.getAttribute(name)));


        log.info("sessionId={}", session.getId()); //해당 session
        log.info("maxInactiveInterval={}", session.getMaxInactiveInterval()); //session 생명 주기
        log.info("creationTime={}", new Date(session.getCreationTime())); //session 만들어진 시간
        log.info("lastAccessedTime={}", new Date(session.getLastAccessedTime())); //해당 session 에 마지막으로 접근한 시간
        log.info("isNew={}", session.isNew()); //해당 세션이 방금 생성된 session 인지 확인

        return "세션 출력";
    }
}
