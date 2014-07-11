package elevators;

import java.util.List;

public class MainTest {
    public static void main(String[] args) {
        simpleTest();
    }

    private static int timer = 0;

    private static void simpleTest() {
        ControlSystemInterface control = new ControlSystem(3, 10);
        control.acceptRequest(new Request(1, new Passenger(100, 5)));
        print(control.makeStep());
        control.acceptRequest(new Request(1, new Passenger(200, 6)));
        print(control.makeStep());
        control.acceptRequest(new Request(4, new Passenger(300, 1)));
        print(control.makeStep());
        // Let the elevators finish the job
        print(control.makeStep());
        print(control.makeStep());
        print(control.makeStep());
        print(control.makeStep());
        print(control.makeStep());
        print(control.makeStep());
        print(control.makeStep());
        print(control.makeStep());
        print(control.makeStep());
        print(control.makeStep());
        // Should be enough
    }

    private static void print(List<Command> commands) {
        timer++;
        System.out.println("At moment " + timer + ":");
        for (Command command : commands) {
            System.out.println(command);
        }
    }
}
