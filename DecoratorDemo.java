import java.util.*;
interface Coffee { 
    String getDescription(); 
    int cost(); 
}

class SimpleCoffee implements Coffee {
    public String getDescription(){ return "Simple Coffee"; }
    public int cost(){ return 50; }
}

class MilkDecorator implements Coffee {
    private Coffee coffee;
    MilkDecorator(Coffee coffee){ this.coffee = coffee; }
    
    public String getDescription(){ 
        return coffee.getDescription() + ", Milk"; 
    }
    public int cost(){ 
        return coffee.cost() + 20; 
    }
}

class SugarDecorator implements Coffee {
    private Coffee coffee;
    SugarDecorator(Coffee coffee){ this.coffee = coffee; }
    
    public String getDescription(){ 
        return coffee.getDescription() + ", Sugar"; 
    }
    public int cost(){ 
        return coffee.cost() + 10; 
    }
}

public class DecoratorDemo {
    public static void main(String[] args) {
        Coffee coffee = new SimpleCoffee();
        System.out.println(coffee.getDescription() + " => Rs." + coffee.cost());
        
        coffee = new MilkDecorator(coffee);
        System.out.println(coffee.getDescription() + " => Rs." + coffee.cost());
        
        coffee = new SugarDecorator(coffee);
        System.out.println(coffee.getDescription() + " => Rs." + coffee.cost());
    }
}
