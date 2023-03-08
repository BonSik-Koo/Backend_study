package study.springdatajpa.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //private X -> 프록시가 객체를 만들때 오류가 발생
@ToString(of = {"id", "username", "age"}) //출력할때 찍은 컬렴들 지정 가능
@NamedQuery( //Named 쿼리
        name = "Member.findByUsername",
        query = "select m from Member m where m.username = :username"
)
public class Member extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private  Long id;

    private String username;

    private Integer age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    //생성자
    public Member(String username) {
        this.username = username;
    }
    public Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;
        if(team != null) //단순하게 null 처리 -> 실제로는 예외 발생시키는 로직으로 설계
            changeTeam(team);
    }
    public Member(String username, Integer age) {
        this.username = username;
        this.age = age;
    }

    //양방향 연관관계 매핑
    public void changeTeam(Team team){
        this.team = team;
        team.getMembers().add(this);
    }

    //setter
    public void setUsername(String username){
        this.username = username;
    }

}
