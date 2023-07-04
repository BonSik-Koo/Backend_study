package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV2;
import hello.jdbc.repository.MemberRepositoryV3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 트랜잭션 - 트랜잭션 매니저
 */
@RequiredArgsConstructor
@Slf4j
public class MemberServiceV3_1 {

    //추상화된 트랜잭션 매니저 사용
    private final PlatformTransactionManager transactionManager;
    private final MemberRepositoryV3 memberRepository;
    public void accountTransfer(String fromId, String toId, int money) throws SQLException {

        //트랜잭션 시작(인자로는 옵션값)
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try{
            //비지니스 로직
            bizLogic(fromId, toId, money);
            transactionManager.commit(status); //성공시 커밋!!
        }catch (Exception e){
            transactionManager.rollback(status); //실패시 롤백!!
            throw new IllegalArgumentException(e);
        }
        //트랜잭션 리소스 정리는 트랜잭션 매니저가 자동으로 수행
    }

    private void bizLogic(String fromId, String toId, int money) throws SQLException {
        Member formMember = memberRepository.findById(fromId);
        Member toMember = memberRepository.findById(toId);

        memberRepository.update(fromId, formMember.getMoney() - money);
        validation(toMember);
        memberRepository.update(toId, toMember.getMoney() + money);
    }
    private void validation(Member member){
        if(member.getMemberId().equals("ex")){
            throw new IllegalArgumentException("이체중 예외 발생");
        }
    }
}
