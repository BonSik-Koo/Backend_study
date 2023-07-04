package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.ex.MyDBException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;

/**
 * 예외 누수 문제 해결
 * 체크 예외를 런타임 예외로 변경
 * MemberRepository 인터페이스 사용
 * throws SQLException 제거
 */
@Slf4j
public class MemberRepositoryV4_1 implements MemberRepository{

    private final DataSource dataSource;

    public MemberRepositoryV4_1(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Member save(Member member) {
        String sql = "insert into member(member_id, money) values(?,?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try{
            //데이터베이스 커넥션 획득
            con = getConnection();

            //커넥션을 이용해 데이터베이스에게 전달한 SQL 문, 파라미터 데이터 준비
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, member.getMemberId());
            pstmt.setInt(2, member.getMoney());

            //삽입 SQL 을 커넥션을 통해 DB 에 전달
            pstmt.executeUpdate();
            return member;
        } catch (SQLException e) {
            throw new MyDBException(e); //!!!!!!!!
        }finally {
            close(con, pstmt, null);
        }
    }

    @Override
    public Member findById(String memberId) {
        String sql = "select * from member where member_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);

            //조회 SQL 을 커넥션을 통해 DB 에 전달
            //응답으로 ResultSet 를 받는다.
            rs = pstmt.executeQuery();

            if(rs.next()){
                /**
                 * 순수 JDBC 를 사용하기 때문에 엔티티(객체)를 직접 생성해서 반환해줘야한다.
                 * SQL Mapper, ORM 기술들은 이를 대신 해준다.
                 */
                Member member = new Member();
                member.setMemberId(rs.getString("member_id"));
                member.setMoney(rs.getInt("money"));
                return member;
            }else {
                throw new NoSuchElementException("member not found memberId=" + memberId);
            }
        } catch (SQLException e) {
            throw new MyDBException(e); //!!!!!!!!
        }finally {
            close(con, pstmt, rs);
        }
    }

    @Override
    public void update(String memberId, int money){
        String sql = "update member set money = ? where member_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, money);
            pstmt.setString(2, memberId);

            //실제 영향을 받은 row 의 개수
            int resultSize = pstmt.executeUpdate();
            log.info("resultSize = {}", resultSize);
        } catch (SQLException e) {
            throw new MyDBException(e); //!!!!!!!!
        }finally {
            close(con, pstmt, null);
        }
    }

    @Override
    public void delete(String memberId) {
        String sql = "delete from member where member_id=?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new MyDBException(e); //!!!!!!!!
        } finally {
            close(con, pstmt, null);
        }
    }

    private void close(Connection con, Statement stmt, ResultSet rs){
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(stmt);

        //트랜잭션 동기화 매니저로 트랜잭션을 반납한다.(비지니스 로직에서 트랜잭션 그대로 유지됨)
        DataSourceUtils.releaseConnection(con, dataSource);
    }
    private Connection getConnection() throws SQLException {
        //트랜잭션 동기화 매니저에서 트랜잭션을 가져온다.
        Connection con = DataSourceUtils.getConnection(dataSource);
        return con;
    }
}
