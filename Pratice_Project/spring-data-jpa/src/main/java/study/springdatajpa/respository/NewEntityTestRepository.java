package study.springdatajpa.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.springdatajpa.entity.NewEntityTest;

public interface NewEntityTestRepository extends JpaRepository<NewEntityTest, Long> {

}
