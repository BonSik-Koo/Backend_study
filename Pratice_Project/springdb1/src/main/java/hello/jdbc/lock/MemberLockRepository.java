package hello.jdbc.lock;


import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface MemberLockRepository extends JpaRepository<MemberLock,Long> {

    @Lock(LockModeType.OPTIMISTIC) //@Version 사용
    Optional<MemberLock> findVersionByMemberId(String memberId);

    @Lock(LockModeType.OPTIMISTIC) //낙관적 락 사용
    Optional<MemberLock> findOPTIMISTICByMemberId(String memberId);

    Optional<MemberLock> findByMemberId(String memberId);

    @Lock(LockModeType.PESSIMISTIC_WRITE) //비관적 락 사용
    Optional<MemberLock> findPESSIMISTICByMemberId(String memberId);

}
