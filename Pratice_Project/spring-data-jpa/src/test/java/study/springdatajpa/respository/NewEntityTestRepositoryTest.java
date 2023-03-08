package study.springdatajpa.respository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.springdatajpa.entity.NewEntityTest;

@SpringBootTest
class NewEntityTestRepositoryTest {

    @Autowired NewEntityTestRepository newEntityTestRepository;

    @Test
    public void 새로운객체판별테스트() throws Exception {

        //기본 "@GeneratedValue"일 경우
//        NewEntityTest entity = new NewEntityTest();
//        newEntityTestRepository.save(entity);

        //"@GeneratedValue"가 아닌 String 와 같은 자료형 일 경우
        NewEntityTest entity = new NewEntityTest("A");
        newEntityTestRepository.save(entity);
    }

}