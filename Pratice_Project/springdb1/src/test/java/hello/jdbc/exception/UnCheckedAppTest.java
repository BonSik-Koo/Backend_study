package hello.jdbc.exception;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.net.ConnectException;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
public class UnCheckedAppTest {

    @Test
    void checked(){
        Controller controller = new Controller();

//        controller.request();
        assertThatThrownBy(() -> controller.request())
                .isInstanceOf(Exception.class); //SQLException, ConnectException
    }

    static class Controller{
        Service service = new Service();

        public void request(){
            service.logic();
        }
    }

    static class Service {
        Repository repository = new Repository();
        NetworkClient networkClient = new NetworkClient();

        public void logic() { //repository 에서 올라오는 RuntimeException 을 신경쓰지 않아도 된다.
            repository.call();
            networkClient.call();
        }
    }

    static class NetworkClient {
        public void call(){
            throw new RuntimeConnectException("connection ex");
        }
    }
    static class Repository {
        public void call(){
            try {
                runSQL();
            } catch (SQLException e) {
                /**
                 * 체크 예외를 언체크 예외로 교체해서 던진다.
                 * 상위 계층의 클래스는 예외를 신경쓰지 않아도 된다.
                 * repository 기술이 바뀌어도 신경쓸 필요가 없다.!! -> 의존할 필요가 없다.
                 */
                throw new RuntimeSQLException(e);
            }
        }

        private void runSQL() throws SQLException {
            throw new SQLException("ex");
        }
    }

    static class RuntimeConnectException extends RuntimeException{
        public RuntimeConnectException(String message) {
            super(message);
        }
    }
    static class RuntimeSQLException extends RuntimeException{
        public RuntimeSQLException() {
            super();
        }
        public RuntimeSQLException(Throwable cause) {
            super(cause);
        }
    }
}
