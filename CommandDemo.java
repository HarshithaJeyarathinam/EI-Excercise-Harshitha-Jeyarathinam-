import java.util.*;
// Command interface
interface Command {
    void execute();
}

// Receiver (Light)
class Light {
    void on() { System.out.println("Light is ON"); }
    void off() { System.out.println("Light is OFF"); }
}

// Concrete Command for ON
class LightOnCommand implements Command {
    private Light light;
    LightOnCommand(Light light) { this.light = light; }
    public void execute() { light.on(); }
}

// Concrete Command for OFF
class LightOffCommand implements Command {
    private Light light;
    LightOffCommand(Light light) { this.light = light; }
    public void execute() { light.off(); }
}

public class CommandDemo {
    public static void main(String[] args) {
        Light light = new Light();

        // Call ON command
        Command on = new LightOnCommand(light);
        on.execute();

        // Call OFF command
        Command off = new LightOffCommand(light);
        off.execute();
    }
}
