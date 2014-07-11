package elevators;

public class MoveCommand implements Command {
    public final int elevatorId;
    public final Direction direction;

    public MoveCommand(int elevatorId, Direction direction) {
        this.elevatorId = elevatorId;
        this.direction = direction;
    }

    @Override
    public String toString() {
        return "Move elevator #" + elevatorId + " in direction: " + direction;
    }
}
