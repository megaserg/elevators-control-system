package elevators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ControlSystem implements ControlSystemInterface {
    private final Map<Integer, Set<Passenger>> queue;
    private final ElevatorGroup state;

    public ControlSystem(int numOfElevators, int buildingHeight) {
        this.queue = new HashMap<>();
        this.state = new ElevatorGroup(numOfElevators, buildingHeight);
    }

    @Override
    public void acceptRequest(Request request) {
        if (!queue.containsKey(request.sourceFloor)) {
            queue.put(request.sourceFloor, new HashSet<Passenger>());
        }
        queue.get(request.sourceFloor).add(request.passenger);
    }

    // I assume that during one step, each elevator can either:
    // 1) load and unload passengers or
    // 2) move one floor up or down
    @Override
    public List<Command> makeStep() {
        List<Command> commands = new ArrayList<>();
        Set<Elevator> stepMade = new HashSet<>();

        // Assign unassigned passengers to idle elevators
        Set<Elevator> idles = getIdleElevators();
        for (Map.Entry<Integer, Set<Passenger>> entry : queue.entrySet()) {
            if (idles.size() == 0) {
                break;
            }
            int floor = entry.getKey();
            Set<Passenger> waiting = entry.getValue();
            for (Passenger passenger : waiting) {
                Elevator closest = null;
                for (Elevator idle : idles) {
                    if (closest == null || Math.abs(closest.floor() - floor) > Math.abs(idle.floor() - floor)) {
                        closest = idle;
                    }
                }
                if (closest != null) {
                    idles.remove(closest);
                    waiting.remove(passenger);
                    closest.addTarget(floor);
                }
            }
        }

        // If arrived to target, unload passengers
        for (Elevator elevator : state.elevators) {
            Set<Passenger> toUnload = new HashSet<>();
            for (Passenger passenger : elevator.passengers()) {
                if (passenger.targetFloor == elevator.floor()) {
                    toUnload.add(passenger);
                }
            }
            if (toUnload.size() > 0) {
                elevator.unloadPassengers(toUnload);
                if (elevator.isOnTarget()) {
                    elevator.makeIdle();
                }
                commands.add(new UnloadCommand(elevator.info.id, passengersToIds(toUnload)));
                stepMade.add(elevator);
            }
        }

        // If passengers want to go along, load them
        for (Elevator elevator : state.elevators) {
            if (haveRequests(elevator.floor())) {
                Set<Passenger> toLoad = new HashSet<>();
                for (Passenger passenger : queue.get(elevator.floor())) {
                    if (passenger.targetFloor > elevator.floor() && elevator.direction() == Direction.UP ||
                        passenger.targetFloor < elevator.floor() && elevator.direction() == Direction.DOWN) {
                        toLoad.add(passenger);
                        elevator.addTarget(passenger.targetFloor);
                    }
                }
                if (toLoad.size() > 0) {
                    queue.get(elevator.floor()).removeAll(toLoad);
                    elevator.loadPassengers(toLoad);
                    commands.add(new LoadCommand(elevator.info.id, passengersToIds(toLoad)));
                    stepMade.add(elevator);
                }
            }
        }

        // Move busy elevators towards their target
        for (Elevator elevator : state.elevators) {
            if (!stepMade.contains(elevator) && !elevator.isOnTarget()) {
                elevator.moveOneFloor();
                commands.add(new MoveCommand(elevator.info.id, elevator.direction()));
                stepMade.add(elevator);
            }
        }

        return commands;
    }

    private Set<Elevator> getIdleElevators() {
        Set<Elevator> idle = new HashSet<>();
        for (Elevator elevator : state.elevators) {
            if (elevator.direction() == Direction.IDLE) {
                idle.add(elevator);
            }
        }
        return idle;
    }

    private Set<Integer> passengersToIds(Set<Passenger> passengers) {
        Set<Integer> ids = new HashSet<>();
        for (Passenger passenger : passengers) {
            ids.add(passenger.id);
        }
        return ids;
    }

    private boolean haveRequests(int floor) {
        Set<Passenger> waiting = queue.get(floor);
        return waiting != null && waiting.size() > 0;
    }
}
