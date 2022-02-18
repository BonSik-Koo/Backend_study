package hello.servlet.bash.request;

import org.springframework.util.StreamUtils;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

//http api 방식(http message body에 직접 데이터를 넣는 방식)에서 body에 간단한 text 데이터 전달 경우
@WebServlet(name= "requestBodyStringServlet" , urlPatterns = "/request-body-string")
public class RequestBodyStringServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ServletInputStream inputStream = req.getInputStream(); //http body에 있는 데이터를 바이트로 다읽어오는것
        String s = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8); //utf-8로 번역하는것
        System.out.println("message body = " + s);
        System.out.println();

        resp.getWriter().write("ok");
    }
}
