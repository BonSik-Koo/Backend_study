package hello.jdbc.connect;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static hello.jdbc.connect.ConnectionConst.*;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class ConnectionTest {

    /**
     * 순수 DriverManager 사용
     * 항상 새로운 커넥션을 만든다.
     */
    @Test
    void driverManage() throws SQLException {

        Connection con1 = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        Connection con2 = DriverManager.getConnection(URL, USERNAME, PASSWORD);

        log.info("connection={}, class={}", con1, con1.getClass());
        log.info("connection={}, class={}", con2, con2.getClass());
        assertThat(con1).isNotEqualTo(con2);
    }

    /**
     * 스프링이 제공해주는 DataSource 가 적용된 DriverManagerDataSource 사용
     * 마찬가지로 항상 새로운 커넥션을 만든다.
     * DataSource 인터페이스를 사용하기 때문에 DriverManagerDataSource, 커넥션 풀 등으로 교체가 가능하다.
     */
    @Test
    void dataSourceDriverManager() throws SQLException, InterruptedException {
        //커넥션을 가져오기 위한 설정 단계
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);

        //커넥션을 사용하는(가져오는) 단계
        Connection con1 = useDataSource(dataSource);
        log.info("connection={}, class={}", con1, con1.getClass());
        JdbcUtils.closeConnection(con1); //반납

        Connection con2 = useDataSource(dataSource);
        log.info("connection={}, class={}", con2, con2.getClass());
        assertThat(con1).isNotEqualTo(con2);
    }

    /**
     * 스프링이 제공해주는 DataSource 가 적용된 커넥션 풀(HikariCP 커넥션 풀) 사용
     */
    @Test
    void dataSourceConnectionPool() throws SQLException, InterruptedException {
        //커넥션을 가져오기 위한 설정 단계
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        dataSource.setMaximumPoolSize(10); //커넥션 풀 최대 사이즈
        dataSource.setPoolName("MyPool");

        //커넥션 사용을 사용하는 단계
        //커넥션 풀에서 커넥션을 가져오기 전에 커넥션 풀 최대 사이즈 만큼 커넥션을 생성하게 됌.
//        Connection con1 = useDataSource(dataSource);
//        log.info("connection={}, class={}", con1, con1.getClass());
//        JdbcUtils.closeConnection(con1);
//        Connection con2 = useDataSource(dataSource);
//        log.info("connection={}, class={}", con2, con2.getClass());


        //커넥션 풀의 개수보다 초과하게 되면??
        for(int i=0;i<10;i++){
            Connection con = useDataSource(dataSource);
            log.info("connection={}, class={}", con, con.getClass());
        }
    }

    private Connection useDataSource(DataSource dataSource) throws SQLException, InterruptedException {
        Connection connection = dataSource.getConnection();
        Thread.sleep(1000); //커넥션 풀에서 커넥션 생성 시간 대기
        return connection;
    }
}
