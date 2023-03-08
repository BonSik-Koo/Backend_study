package study.springdatajpa.respository.SpringDataJPA;

import org.springframework.data.jpa.repository.JpaRepository;
import study.springdatajpa.entity.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
