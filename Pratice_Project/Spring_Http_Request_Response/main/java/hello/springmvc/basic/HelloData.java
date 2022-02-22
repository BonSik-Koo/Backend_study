package hello.springmvc.basic;

import lombok.Data;
import lombok.Getter;

// "parameter"를 "Model"받는 객체
@Data
public class HelloData {

    private String username;
    private int age;
}
