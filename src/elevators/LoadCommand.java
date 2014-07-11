package elevators;

import java.util.List;

public class LoadCommand implements Command {
    public final int elevatorId;
    public final List<Integer> passengerIds;

    public LoadCommand(int elevatorId, List<Integer> passengerIds) {
        this.elevatorId = elevatorId;
        this.passengerIds = passengerIds;
    }

    @Override
    public String toString() {
        return "Load into elevator #" + elevatorId + " passengers " + passengerIds;
    }
}
