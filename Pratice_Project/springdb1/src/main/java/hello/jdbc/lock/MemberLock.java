package hello.jdbc.lock;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class MemberLock {

    @Id
    private String memberId;

    private Integer money;

    @Version
    private Integer version;

    public MemberLock(String memberId, int money){
        this.memberId = memberId;
        this.money = money;
    }

    public void upMoney(){
        this.money++;
    }
}
