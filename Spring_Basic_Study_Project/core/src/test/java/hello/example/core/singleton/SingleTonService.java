package hello.example.core.singleton;

public class SingleTonService {

    //해당 클래스를 생성하면 딱 하나로 자기자신을 참조하는 객체를 생성하여 가지고있는다. static로 선언하여 해당 클래스를 생성해도 해당 static변수는 하나만 생성되어 있다.
    private static final SingleTonService singletonservice = new SingleTonService();

    public static SingleTonService getInstance() {return singletonservice;}

    private SingleTonService() {} //외부에서 클래스 객체를 생성하지 못하도록 한다.
}
