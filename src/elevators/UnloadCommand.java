package elevators;

import java.util.List;

public class UnloadCommand implements Command {
    public final int elevatorId;
    public final List<Integer> passengerIds;

    public UnloadCommand(int elevatorId, List<Integer> passengerIds) {
        this.elevatorId = elevatorId;
        this.passengerIds = passengerIds;
    }

    @Override
    public String toString() {
        return "Unload from elevator #" + elevatorId + " passengers " + passengerIds;
    }
}
