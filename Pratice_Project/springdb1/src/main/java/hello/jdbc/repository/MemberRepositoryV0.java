package hello.jdbc.repository;

import hello.jdbc.connect.DBConnectionUtil;
import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.NoSuchElementException;

@Slf4j
public class MemberRepositoryV0 {

    public Member save(Member member) throws SQLException {
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
            log.error("db error", e);
            throw e;
        }finally {
            /**
             * SQL 전달간 예외가 발생하더라도 리소스 정리는 반드시 수행되어야 된다.
             */
            close(con, pstmt, null);
        }
    }

    public Member findById(String memberId) throws SQLException {
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
            log.error("db error", e);
            throw e;
        }finally {
            close(con, pstmt, rs);
        }
    }

    public void update(String memberId, int money) throws SQLException {
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
            log.error("db error", e);
            throw e;
        }finally {
            close(con, pstmt, null);
        }
    }

    public void delete(String memberId) throws SQLException {
        String sql = "delete from member where member_id=?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }

    private void close(Connection con, Statement stmt, ResultSet rs){
        /**
         * ResultSet, Statement 종료간 예외가 발생할 수 있다.
         * 예외와 별개로 Connection 은 종료되어야 하기 때문에 별개로 close 해준다. -> 끊지않으면 "리소스 낭비(누수)" 발생할 수 있다.
         */
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                log.info("error", e);
            }
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                log.info("error", e);
            }
        }
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                log.info("error", e);
            }
        }
    }
    private Connection getConnection(){
        /**
         * 연결된 데이터베이스 드라이브의 커넥션 반환
         */
        return DBConnectionUtil.getConnection();
    }
}
