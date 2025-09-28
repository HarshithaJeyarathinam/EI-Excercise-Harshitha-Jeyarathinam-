import java.util.*;
class Logger {
    private static Logger instance;
    
    private Logger() {}
    
    public static Logger getInstance(){
        if(instance == null){
            instance = new Logger();
        }
        return instance;
    }
    
    public void log(String msg){
        System.out.println("[LOG] " + msg);
    }
}

public class SingletonDemo {
    public static void main(String[] args) {
        Logger logger1 = Logger.getInstance();
        Logger logger2 = Logger.getInstance();
        
        logger1.log("Application started");
        logger2.log("User logged in");
        
        System.out.println("Same instance? " + (logger1 == logger2));
    }
}
