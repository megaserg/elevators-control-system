package elevators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ControlSystem implements ControlSystemInterface {
    private final Map<Integer, Set<Passenger>> queue;
    private final Map<Passenger, Elevator> assigned;
    private final ElevatorGroup state;

    public ControlSystem(int numOfElevators, int buildingHeight) {
        this.queue = new HashMap<>();
        this.assigned = new HashMap<>();
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
                commands.add(new UnloadCommand(elevator.info.id, passengersToIds(toUnload)));
                stepMade.add(elevator);
            }
        }

        // Assign unassigned passengers to idle elevators
        Set<Elevator> idles = getIdleElevators();
        for (Map.Entry<Integer, Set<Passenger>> entry : queue.entrySet()) {
            if (idles.size() == 0) break;
            int floor = entry.getKey();
            Set<Passenger> waiting = entry.getValue();
            for (Passenger passenger : waiting) {
                if (!assigned.containsKey(passenger)) {
                    Elevator closest = null;
                    for (Elevator idle : idles) {
                        if (closest == null || Math.abs(closest.floor() - floor) > Math.abs(idle.floor() - floor)) {
                            closest = idle;
                        }
                    }
                    if (closest != null) {
                        idles.remove(closest);
                        assigned.put(passenger, closest);
                        if (closest.floor() == floor) {
                            closest.addTarget(passenger.targetFloor);
                        } else {
                            closest.addTarget(floor);
                        }
                    }
                }
            }
        }

        // If passengers want to go along, load them
        for (Elevator elevator : state.elevators) {
            if (haveRequests(elevator, elevator.floor())) {
                Set<Passenger> toLoad = new HashSet<>();

                if (elevator.isOnTarget()) {
                    for (Passenger passenger : queue.get(elevator.floor())) {
                        if (assigned.get(passenger) == elevator) {
                            toLoad.add(passenger);
                            elevator.addTarget(passenger.targetFloor);
                        }
                    }
                }

                for (Passenger passenger : queue.get(elevator.floor())) {
                    assert elevator.direction() != Direction.IDLE;
                    if (passenger.targetFloor > elevator.floor() && passenger.targetFloor <= elevator.targetFloor() && elevator.direction() == Direction.UP ||
                        passenger.targetFloor < elevator.floor() && passenger.targetFloor >= elevator.targetFloor() && elevator.direction() == Direction.DOWN) {
                        toLoad.add(passenger);
                    }
                }

                if (toLoad.size() > 0) {
                    queue.get(elevator.floor()).removeAll(toLoad);
                    for (Passenger passenger : toLoad) {
                        assigned.remove(passenger);
                    }
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

        for (Elevator elevator : state.elevators) {
            if (elevator.isOnTarget()) {
                elevator.makeIdle();
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

    private boolean haveRequests(Elevator elevator, int floor) {
        Set<Passenger> waiting = queue.get(floor);
        if (waiting != null && waiting.size() > 0) {
            for (Passenger passenger : waiting) {
                if (isAssignedTo(elevator, passenger)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isAssignedTo(Elevator elevator, Passenger passenger) {
        return !assigned.containsKey(passenger) || assigned.get(passenger) == elevator;
    }
}
