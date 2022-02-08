package hello.hellospring.domain;

import javax.persistence.*;
@Entity //JPA과 관리하는 entity
public class Member { //회원들의 정보

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //@Column(name="name")
    private String name;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id=id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name){
        this.name= name;
    }

}
