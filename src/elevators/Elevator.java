package elevators;

import java.util.HashSet;
import java.util.Set;

public final class Elevator {
    public static final class ElevatorInfo {
        public final int id;
        public final int minFloor;
        public final int maxFloor;

        public ElevatorInfo(int id, int minFloor, int maxFloor) {
            this.id = id;
            this.minFloor = minFloor;
            this.maxFloor = maxFloor;
        }
    }

    public final ElevatorInfo info;
    private final Set<Passenger> passengers;
    private int floor;
    private int targetFloor;
    private Direction direction;

    public Elevator(ElevatorInfo info) {
        this.info = info;
        this.passengers = new HashSet<>();
        this.floor = info.minFloor;
        this.targetFloor = floor;
        this.direction = Direction.IDLE;
    }

    public boolean hasPassengers() {
        return passengers.size() > 0;
    }

    public Set<Passenger> passengers() {
        return passengers;
    }

    public void loadPassengers(Set<Passenger> toLoad) {
        passengers.addAll(toLoad);
    }

    public void unloadPassengers(Set<Passenger> toUnload) {
        passengers.removeAll(toUnload);
    }

    public int floor() {
        return floor;
    }

    public Direction direction() {
        return direction;
    }

    public void direction(Direction d) {
        direction = d;
    }

    public void addTarget(int target) {
        if (target > floor) {
            targetFloor = Math.max(targetFloor, target);
            direction = Direction.UP;
        } else if (target < floor) {
            targetFloor = Math.min(targetFloor, target);
            direction = Direction.DOWN;
        }
    }

    public boolean isOnTarget() {
        return floor == targetFloor;
    }

    public boolean isIdle() {
        return direction == Direction.IDLE;
    }

    public void makeIdle() {
        direction = Direction.IDLE;
    }

    public void moveOneFloor() {
        if (direction == Direction.UP) {
            floor++;
        } else if (direction == Direction.DOWN) {
            floor--;
        } else {
            // This should never happen
            throw new RuntimeException("Cannot move in an idle state");
        }
    }
}
