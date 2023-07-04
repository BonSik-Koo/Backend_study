package hello.jdbc.lock;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest
@Slf4j
public class DBLockTest {

    @Autowired private MemberLockService memberLockService;

    @BeforeEach
    void setUp() {
        memberLockService.saveMemberLock("memberA", 1000);
    }

    @Test
    void JPA_Version_Test() throws InterruptedException {

        final int numberOfThreads = 3;
        CountDownLatch countDownLatch = new CountDownLatch(numberOfThreads);
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        final String memberId = "memberA";
        final int changeMoney = 80;

        for(int i=0; i<numberOfThreads;i++){
            executorService.execute(() -> {
                try {
                    //수정
                    memberLockService.findMemberByVersionAndUpdate(memberId, changeMoney);
                }catch (ObjectOptimisticLockingFailureException e) {
                    System.out.println("출동 감지");
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
    }

    @Test
    void JPA_낙관적락_Test() throws InterruptedException {

        final int numberOfThreads = 2;
        CountDownLatch countDownLatch = new CountDownLatch(numberOfThreads);
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        final String memberId = "memberA";
        final int changeMoney = 80;

        //스래드 1(트랜잭션1) -> 낙관적 락 조회
        executorService.execute(() -> {
            try {
               memberLockService.findMemberByOPTIMISTIC(memberId);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                countDownLatch.countDown();
            }
        });

        //스래드 2(트랜잭션2) -> 데이터 수정
        executorService.execute(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            memberLockService.findMemberAndUpdate(memberId, changeMoney);
            countDownLatch.countDown();
        });
        countDownLatch.await();
    }

    @Test
    void JPA_비관적락_Test() throws InterruptedException {

        final int numberOfThreads = 5;
        CountDownLatch countDownLatch = new CountDownLatch(numberOfThreads);
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        final String memberId = "memberA";
        AtomicInteger changeMoney = new AtomicInteger(80);

        for(int i=0; i<numberOfThreads;i++){
            executorService.execute(() -> {
                try {
                    //비관적 락 사용
                    memberLockService.findMemberByPERSSIMISTIC_WRITEAndUpdate(memberId, changeMoney.getAndIncrement());
                }catch (ObjectOptimisticLockingFailureException e) {
                    System.out.println("출동 감지");
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
    }
}
