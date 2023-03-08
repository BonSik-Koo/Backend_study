package study.springdatajpa.entity.projections;

import org.springframework.beans.factory.annotation.Value;

public interface UsernameOnly {

//    String getUsername();
//
//    TeamInfo getTeam();
//    interface TeamInfo {
//        String getName();
//    }

//    String getTeamName();

    @Value("#{target.username + ' ' + target.age + ' ' + target.team.name}")
    String getUsername();

}
