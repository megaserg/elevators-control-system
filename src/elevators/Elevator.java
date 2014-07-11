package elevators;

import java.util.ArrayList;
import java.util.List;

public final class Elevator {
    public static final class ElevatorInfo {
        public final int minFloor;
        public final int maxFloor;
        public final int capacity;

        public ElevatorInfo(int minFloor, int maxFloor, int capacity) {
            this.minFloor = minFloor;
            this.maxFloor = maxFloor;
            this.capacity = capacity;
        }
    }

    private final ElevatorInfo info;
    private int position;
    private Direction direction;
    private boolean doorsOpen;
    List<Passenger> passengers;

    public Elevator(ElevatorInfo info) {
        this.info = info;
        this.position = 0;
        this.direction = Direction.STILL;
        this.doorsOpen = false;
        this.passengers = new ArrayList<>();
    }
}
