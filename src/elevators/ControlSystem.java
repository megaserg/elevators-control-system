package elevators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ControlSystem {
    private final Map<Integer, List<Passenger>> queue;
    private final ElevatorGroup state;

    public ControlSystem(int numOfElevators, int buildingHeight) {
        this.queue = new HashMap<>();
        this.state = new ElevatorGroup(numOfElevators, buildingHeight);
    }

    public void acceptRequest(Request request) {
        if (!queue.containsKey(request.sourceFloor)) {
            queue.put(request.sourceFloor, new ArrayList<Passenger>());
        }
        queue.get(request.sourceFloor).add(request.passenger);
    }

    // I assume that during one step, each elevator can either:
    // 1) load and unload passengers or
    // 2) move one floor up or down
    public List<Command> step() {
        return new ArrayList<>();
    }
}
