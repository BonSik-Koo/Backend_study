package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@RequiredArgsConstructor
@Slf4j
public class MemberServiceV2 {

    private final DataSource dataSource;
    private final MemberRepositoryV2 memberRepository;

    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        Connection con = dataSource.getConnection();
        try{
            con.setAutoCommit(false); //트랜잭션 시작!!
            bizLogic(con, fromId, toId, money); //비지니스 로직
            con.commit(); //성공시 커밋!!
        }catch (Exception e){
            con.rollback(); //실패시 롤백!!
            throw new IllegalArgumentException(e);
        }finally {
            release(con);
        }
    }

    private void bizLogic(Connection con , String fromId, String toId, int money) throws SQLException {
        Member formMember = memberRepository.findById(con, fromId);
        Member toMember = memberRepository.findById(con, toId);

        memberRepository.update(con, fromId, formMember.getMoney() - money);
        validation(toMember);
        memberRepository.update(con, toId, toMember.getMoney() + money);
    }
    private void validation(Member member){
        if(member.getMemberId().equals("ex")){
            throw new IllegalArgumentException("이체중 예외 발생");
        }
    }
    private void release(Connection con){
        if (con != null) {
            try {
                /**
                 * 커넥션 풀을 사용할때 기존에 "수동 모드"의 commit 이 그대로 커넥션 풀로 반환되기 때문에
                 * default 설정인 "자동 모드"로 변경 후 반환
                 * "close()" -> 커넥션 풀 사용시 반환, ConnectionManager 사용시 커넥션 끝(없어짐)
                 */
                con.setAutoCommit(true);
                con.close();
            } catch (Exception e) {
                log.info("error", e);
            }
        }
    }
}
