package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.util.List;

/*Test
* 구현한 기능이 정상동작하는지 판별해보는 것
* 주위! 전체 run을 할 때 정의한 Test 메소드들이 정해진 순서없이 실행된다 !!!! ->Test메소드가 하나 실행될때마다 clear를 해주어야 된다.!!!!!!!!! ->순서와 상관없이 설게되어야 된다.
*/

public class MeomoryMemberRepositoryTest {

    MemoryMemberRepository repository = new MemoryMemberRepository(); //판별할 클래스 객체 생성

    @AfterEach //@Test인 메소드를 하나 실행할때마다 해당 메소드를 실행한다.
    public void afterEach() {
        repository.clearStore();
    }

    @Test //Save 메소드 테스트
    public void save() {
        Member member =new Member();
        member.setName("spring");

        repository.save(member);

        Member result = repository.findById(member.getId()).get(); //Option인데 get()을 사용하여 안에있는걸 꺼낸다.
        Assertions.assertThat(member).isEqualTo(result); //!!!!!!!!!!!!!출력방식을 게속 사용할수 없으니 객체를 비교하여 해당 줄에서 성립하지 않으면 넘어가지 않게 하는 기능
    }

    @Test //findByName 메소드 테스트
    public void findByName() {
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);

        Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);

        Member result = repository.findByName("spring1").get();
        Assertions.assertThat(member1).isEqualTo(result);
    }

    @Test
    public void findAll() {
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);

        Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);

        List<Member> result = repository.findAll();

        Assertions.assertThat(result.size()).isEqualTo(2);
    }
}
