package hello.servlet.bash;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter  //필드의 get, set 와 같은 기능 ->lombok 이용해서 간단히 가능
public class HelloData {

    private String username;
    private int age;
}
