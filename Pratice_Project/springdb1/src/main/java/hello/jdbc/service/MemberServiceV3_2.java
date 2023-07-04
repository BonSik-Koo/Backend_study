package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.SQLException;

/**
 * 트랜잭션 - 트랜잭션 템플릿
 */
@Slf4j
public class MemberServiceV3_2 {

    private final TransactionTemplate txTemplate;
    private final MemberRepositoryV3 memberRepository;
    public MemberServiceV3_2(PlatformTransactionManager transactionManager, MemberRepositoryV3 memberRepository) {
        this.txTemplate = new TransactionTemplate(transactionManager);
        this.memberRepository = memberRepository;
    }
    public void accountTransfer(String fromId, String toId, int money) throws SQLException {

        //트랜잭션 템플릿을 이용하여 트랜잭션 시작, 커밋, 롤백을 자동으로 수행해준다.
        //언체크 예외는 롤백, 체크 예외는 커밋하게 됨
        txTemplate.executeWithoutResult((status) -> {
            //비지니스 로직
            try {
                bizLogic(fromId, toId, money);
            } catch (SQLException e) {
                throw new IllegalArgumentException(e);
            }
        } );
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
