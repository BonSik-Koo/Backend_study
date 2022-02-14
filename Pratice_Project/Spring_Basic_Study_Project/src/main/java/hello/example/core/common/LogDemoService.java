package hello.example.core.common;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogDemoService {

    //private final ObjectProvider<MyLogger> myLoggerProvider;
    private final MyLogger myLogger;

//    @Autowired  //-> "ObjectProvider" 사용
//    public LogDemoService(ObjectProvider<MyLogger> myLogger) {
//        this.myLoggerProvider = myLogger;
//    }

    @Autowired  // -> "Proxy" 사용
    public LogDemoService(MyLogger myLogger) {
        this.myLogger = myLogger;
    }

    public void logic(String id) {
       // MyLogger myLogger = myLoggerProvider.getObject(); //-> "ObjectProvider" 사용
        myLogger.log("service id = " + id);
    }
}
