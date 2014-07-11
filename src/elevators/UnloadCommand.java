package elevators;

import java.util.Set;

public class UnloadCommand implements Command {
    public final int elevatorId;
    public final Set<Integer> passengerIds;

    public UnloadCommand(int elevatorId, Set<Integer> passengerIds) {
        this.elevatorId = elevatorId;
        this.passengerIds = passengerIds;
    }

    @Override
    public String toString() {
        return "Unload from elevator #" + elevatorId + " passengers " + passengerIds;
    }
}
