package hello.hellospring.controller;

public class MemberForm {
    private String name; //스프링에서 name은 "private"이기 때문에 메소드를 호출하여 저장!!!! ->private인데 해당 메소드 없으면 저장할 수 없게 된다!!!!!!!!!

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
