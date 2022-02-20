package hello.servlet.bash;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
WAS인 톰켓 서버가 시작되면서 servlet컨테이너를 생성하게 된다. 그리고 톰켓 서버가 http요청이 들어오면 http request의 http 스펙들와 reponst하는 http 스팩들의 객체를 각각 생성해서
servlet컨테이너에 전달하게 된다. !! -> 스프링 MVC도 servlet을 가지고 만들어진것!!!
서블릿 컨테이너가 생성되고 안에 아래 지정한 "hellServlet"이름의 서블릿이 생성되다.
 */
@WebServlet(name = "helloServlet" , urlPatterns = "/hello") //url매핑
public class HelloServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("HelloServlet.service");
        System.out.println("request = " + request);
        System.out.println("response = " + response);

        String username = request.getParameter("username"); //쿼리 파라미터로 넘어오는거 get하는거
        System.out.println("username = "+ username );

        response.setContentType("text/plain");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write("hello " + username);


    }
}
