package hello.servlet.bash.request;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//실제 http의 전달 데이터를 확인해보는것
@WebServlet(name = " requestParamServlet", urlPatterns = "/request-param")
public class RequestParamServlet extends HttpServlet {

    /**
     * 1. 파라미터 전송 기능
     * http://localhost:8080/request-param?username=hello&age=20
     * <p>
     * 2. 동일한 파라미터 전송 가능
     * http://localhost:8080/request-param?username=hello&username=kim&age=20
     */

    /*
    get방식이나, html-form을 이용한 post방식이나 getParam로 동일하게 가져올수 있다.-> 이유는 get방식에서는 URL에 쿼리 파라미터를 사용하고
    html-form의 post방식에서는 html body에 쿼리 파라미터방식으로 값들이 전달되기 때문이다.
     */
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        System.out.println("[전체 파라미터 조회] - start");
        req.getParameterNames().asIterator()
                .forEachRemaining(paramName -> System.out.println( paramName + " = " + req.getParameter(paramName)));
        System.out.println("[전체 파라미터 조회] - end");
        System.out.println();

        System.out.println("[단일 파라미터 조회]");
        String username = req.getParameter("username");
        String age = req.getParameter("age");
        System.out.println("username = " + username);
        System.out.println("age = " + age);
        System.out.println("[단일 파라미터 조회] - end");
        System.out.println();

        System.out.println("[이름이 같은 복수 파라미터 조회] - start");
        String[] user = req.getParameterValues("username");
        for(String name : user) {
            System.out.println("username = " + name);
        }
        System.out.println("[이름이 같은 복수 파라미터 조회] - end");


        resp.getWriter().write("ok");
    }
}
