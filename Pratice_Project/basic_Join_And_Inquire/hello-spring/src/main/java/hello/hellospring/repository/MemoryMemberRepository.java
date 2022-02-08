/* MemberRepository 인터페이스 구현체 클래스 */
package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ArrayList;

//@Repository //스프링이 시작될때 스프링 컨테이너가 생성되는데 "Repository"로 지정한 클래스를 생성하고 스프링 컨테이너에 등록한다. ->컴포넌트 스캔방식
public class MemoryMemberRepository implements MemberRepository {

    private static Map<Long, Member> store = new HashMap<>(); //아직 저장할 리퍼지토리를 정하지 않아 메모리를 사용
    private static long sequence = 0;

    @Override
    public Member save(Member member) {
        member.setId(++sequence);
        store.put(member.getId() , member);
        return member;
    }

    @Override
    //Optional이라는 형태로 감싸서 return하게 된다. -> 사용하는 이유는 null값이 나올때 그냥 null값보다는 Optional형태로 감싸서 반환하게 되면 다른 처리가 가능.
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(store.get(id));//!!!!!!!!!!!!!!!!!!!!null이라도 optional로 감싸서 반환해준다.???????????????????????????????????????
    }

    @Override
    public Optional<Member> findByName(String name){
        return store.values().stream()
                .filter(member -> member.getName().equals(name))
                .findAny(); //찾은 값을 Optional 형태로 감싸서 리턴해주게 되다.
    }

    @Override
    public List<Member> findAll() {
        return new ArrayList<Member>(store.values());
    }

    public void clearStore() {
        store.clear();
    }
}
