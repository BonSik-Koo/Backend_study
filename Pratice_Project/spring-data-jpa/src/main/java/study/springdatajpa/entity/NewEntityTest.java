package study.springdatajpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Persistable;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NewEntityTest extends BaseTimeEntity implements Persistable<String> {

    @Id
    //@GeneratedValue
    private String id;

    public NewEntityTest(String id){
        this.id = id;
    }

    @Override
    public boolean isNew() {
        return getCreatedDate() == null;
    }
}
