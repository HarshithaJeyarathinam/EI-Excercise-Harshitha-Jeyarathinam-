import java.util.*;
interface Shape { 
    void draw(); 
}

class Circle implements Shape {
    public void draw(){ 
        System.out.println("Circle drawn."); 
    }
}

class Square implements Shape {
    public void draw(){ 
        System.out.println("Square drawn."); 
    }
}

class ShapeFactory {
    public Shape getShape(String type){
        if(type.equalsIgnoreCase("circle")) return new Circle();
        if(type.equalsIgnoreCase("square")) return new Square();
        return null;
    }
}

public class FactoryDemo {
    public static void main(String[] args) {
        ShapeFactory factory = new ShapeFactory();
        
        Shape shape1 = factory.getShape("circle");
        shape1.draw();
        
        Shape shape2 = factory.getShape("square");
        shape2.draw();
    }
}
