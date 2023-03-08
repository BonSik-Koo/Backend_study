package study.springdatajpa.entity.projections;

import lombok.Data;
import study.springdatajpa.entity.Team;

@Data
public class UsernameOnlyDto {

    private String username;
//    private Long id;
//    private Team team;
//    public UsernameOnlyDto(String username, Long id, Team team){
//        this.username = username;
//        this.id = id;
//        this.team = team;
//    }

    private String teamName;
    public UsernameOnlyDto(String username, String teamName) {
        this.username = username;
        this.teamName = teamName;
    }


}
