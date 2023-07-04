package hello.jdbc.connect;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static hello.jdbc.connect.ConnectionConst.*;

@Slf4j
public class DBConnectionUtil {
    public static Connection getConnection(){
        try{
            /**
             * JDBC가 라이브러리에 있는 데이터베이스 드라이버를 찾아서 해당 드라이버가 제공해주는 커넥션을 반환해준다.
             * 각 데이터베이스 드라이버는(MySQL, H2 등) JDBC Connection 인터페이스를 구현한 구현체를 제공해준다.
             * JDBC Connection 인터페이스는 java.sql.Connection 표순 커넥션 인터페이스를 정의하고 있다.
             */
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            log.info("get connection={}, class={}", connection, connection.getClass());
            return connection;

        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
