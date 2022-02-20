package hello.servlet.bash.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.servlet.bash.HelloData;
import org.springframework.util.StreamUtils;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

//http api 방식(http message body에 직접 데이터를 넣는 방식)에서 body에 json 타입으로 데이터 전달 경우
@WebServlet(name = "requestBodyJsonServlet" , urlPatterns = "/request-body-json")
public class RequestBodyJsonServlet  extends HttpServlet {

    /*JSON 결과를 파싱해서 사용할 수 있는 자바 객체로 변환하려면 Jackson, Gson 같은 JSON 변환
    /라이브러리를 추가해서 사용해야 한다. 스프링 부트로 Spring MVC를 선택하면 기본으로 Jackson
    라이브러리( ObjectMapper )를 함께 제공한다.*/
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ServletInputStream inputStream = req.getInputStream();//http body에 있는 데이터를 바이트로 다읽어오는것
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        //System.out.println("messageBody = " + messageBody);

        HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);
        System.out.println("helloData.username = " + helloData.getUsername());
        System.out.println("helloData.age = " + helloData.getAge());

        resp.getWriter().write("ok");

    }
}
