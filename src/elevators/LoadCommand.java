package elevators;

import java.util.Set;

public class LoadCommand implements Command {
    public final int elevatorId;
    public final Set<Integer> passengerIds;

    public LoadCommand(int elevatorId, Set<Integer> passengerIds) {
        this.elevatorId = elevatorId;
        this.passengerIds = passengerIds;
    }

    @Override
    public String toString() {
        return "Load into elevator #" + elevatorId + " passengers " + passengerIds;
    }
}
