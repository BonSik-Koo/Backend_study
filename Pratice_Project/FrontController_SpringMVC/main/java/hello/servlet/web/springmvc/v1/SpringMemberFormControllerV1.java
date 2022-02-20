package hello.servlet.web.springmvc.v1;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller //Component 기능과 RequestMappingHandlerMapping 기능을 다 가지고 있다. ->스프링 내부에서 핸들러 매핑이 일어날때 해당 에노테이션이 붙은 컨트롤러를 매핑가능
public class SpringMemberFormControllerV1 {

    @RequestMapping("/springmvc/v1/members/new-form")
    public ModelAndView process() {
        return new ModelAndView("new-form");
    }

}
