import java.util.*;
interface PaymentStrategy {
    void pay(int amount);
}

class CreditCardPayment implements PaymentStrategy {
    public void pay(int amount) { 
        System.out.println("Paid " + amount + " via Credit Card."); 
    }
}

class UpiPayment implements PaymentStrategy {
    public void pay(int amount) { 
        System.out.println("Paid " + amount + " via UPI."); 
    }
}

class PaymentContext {
    private PaymentStrategy strategy;
    
    void setStrategy(PaymentStrategy strategy){ this.strategy = strategy; }
    
    void checkout(int amount){ 
        strategy.pay(amount); 
    }
}

public class StrategyDemo {
    public static void main(String[] args) {
        PaymentContext context = new PaymentContext();
        
        context.setStrategy(new CreditCardPayment());
        context.checkout(500);
        
        context.setStrategy(new UpiPayment());
        context.checkout(200);
    }
}
