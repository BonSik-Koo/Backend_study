package hello.jdbc.exception;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.ConnectException;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
public class CheckedAppTest {

    @Test
    void checked(){
        Controller controller = new Controller();
        assertThatThrownBy(() -> controller.request())
                .isInstanceOf(Exception.class); //SQLException, ConnectException
    }

    static class Controller{
        Service service = new Service();

        //Service 와 마찬가지
        public void request() throws SQLException, ConnectException {
            service.logic();
        }
    }

    static class Service {
        Repository repository = new Repository();
        NetworkClient networkClient = new NetworkClient();

        /**
         * 처리할 수 없으므로 throws 로 던지게 된다.
         * Service 가 특정 repository 기술 예외의 의존하게 된다.
         */
        public void logic() throws SQLException, ConnectException { //throws Exception 으로 해도 되지만 이건 정말 "안티 패턴"!!
            repository.call();
            networkClient.call();
        }
    }

    static class NetworkClient {
        public void call() throws ConnectException {
            throw new ConnectException("connection ex");
        }
    }
    static class Repository {
        public void call() throws SQLException {
            throw new SQLException("ex");
        }
    }
}
