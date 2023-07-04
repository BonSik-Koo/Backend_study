package hello.jdbc.lock;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberLockService {

    private final EntityManager em;
    private final MemberLockRepository memberLockRepository;

    @Transactional
    public MemberLock findMemberByVersionAndUpdate(String memberId, int money){
        MemberLock memberLock = memberLockRepository.findVersionByMemberId(memberId)
                .orElseThrow(() -> new NullPointerException());
        //memberLock.setMoney(money);
        memberLock.upMoney();
        return memberLock;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public MemberLock findMemberByOPTIMISTIC(String memberId) throws InterruptedException {
        MemberLock findMember = memberLockRepository.findOPTIMISTICByMemberId(memberId)
                .orElseThrow(() -> new NullPointerException());

        Thread.sleep(5000); //커밋되기전 다른 트랜잭션이 동작이 완료될때까지 기다리기 위해서
        return findMember;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public MemberLock findMemberByPERSSIMISTIC_WRITEAndUpdate(String memberId, int money){
        MemberLock memberLock = memberLockRepository.findPESSIMISTICByMemberId(memberId)
                .orElseThrow(() -> new NullPointerException());
        memberLock.setMoney(money);
        return memberLock;
    }



    @Transactional(readOnly = true)
    public MemberLock findMemberLock(String memberId){
        return memberLockRepository.findByMemberId(memberId)
                .orElseThrow(() -> new NullPointerException());
    }
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public MemberLock findMemberAndUpdate(String memberId, int money){
        MemberLock memberLock = memberLockRepository.findByMemberId(memberId)
                .orElseThrow(() -> new NullPointerException());
        memberLock.setMoney(money);
        return memberLock;
    }
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public MemberLock saveMemberLock(String memberId, int money){
        MemberLock memberLock = new MemberLock(memberId, money,1);
        MemberLock save = memberLockRepository.save(memberLock);
        em.flush();
        em.clear();
        return save;
    }
}
