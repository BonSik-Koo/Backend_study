package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV1;
import lombok.RequiredArgsConstructor;

import java.sql.SQLException;

@RequiredArgsConstructor
public class MemberServiceV1 {

    private final MemberRepositoryV1 memberRepository;

    public void accountTransfer(String formId, String toId, int money) throws SQLException {

        Member formMember = memberRepository.findById(formId);
        Member toMember = memberRepository.findById(toId);

        memberRepository.update(formId, formMember.getMoney() - money);
        validation(toMember);
        memberRepository.update(toId, toMember.getMoney() + money);

    }
    private void validation(Member member){
        if(member.getMemberId().equals("ex")){
            throw new IllegalArgumentException("이제충 예외 발생");
        }
    }
}
